package net.sf.javagimmicks.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * A helper class for performing JDBC commands by concentrating on the central
 * database logic without the need to take care about standard JDBC API glue
 * code.
 */
public class JDBCExecutor
{
   private final ThreadLocal<Connection> _txCachedConnection = new ThreadLocal<Connection>();
   private final ConnectionProvider _connectionProvider;

   /**
    * Creates a new instance around the given {@link ConnectionProvider}.
    * 
    * @param connectionProvider
    *           the {@link ConnectionProvider} to use for getting JDBC
    *           {@link Connection}s.
    */
   public JDBCExecutor(final ConnectionProvider connectionProvider)
   {
      _connectionProvider = connectionProvider;
   }

   /**
    * Creates a new instance around the given {@link DataSource}.
    * 
    * @param connectionProvider
    *           the {@link DataSource} to use for getting JDBC
    *           {@link Connection}s.
    * @param autoClose
    *           determines if released {@link Connection}s should be
    *           automatically closed by this {@link JDBCExecutor}
    */
   public JDBCExecutor(final DataSource dataSource, final boolean autoClose)
   {
      this(new DataSourceConnectionProviderAdapter(dataSource, autoClose));
   }

   /**
    * Creates a new instance around the given {@link DataSource} which
    * automatically closed no longer used {@link Connection}s.
    * 
    * @param connectionProvider
    *           the {@link DataSource} to use for getting JDBC
    *           {@link Connection}s.
    */
   public JDBCExecutor(final DataSource dataSource)
   {
      this(dataSource, true);
   }

   /**
    * Returns if there is an internal transaction active.
    * 
    * @return if there is an internal transaction active
    * @see #beginTransaction()
    */
   public boolean isTransactionActive()
   {
      return _txCachedConnection.get() != null;
   }

   /**
    * Begins a new transaction which is internally managed by this instance -
    * don't use it if you are inside of some application server that has it's
    * own transaction manager!
    * <p>
    * In particular this is achieve by performing the following steps:
    * <ul>
    * <li>Retrieve a new {@link Connection} from the internal
    * {@link ConnectionProvider} or {@link DataSource}</li>
    * <li>Set {@link Connection#setAutoCommit(boolean)} to {@code false}</li>
    * <li>Store the {@link Connection} for later usage within a
    * {@link ThreadLocal}</li>
    * <li>Use the stored {@link Connection} for all succeeding calls from the
    * same {@link Thread} until {@link #commitTransaction()} or
    * {@link #rollbackTransaction()} is called</li>
    * <li>Also don' call {@link ConnectionProvider#release(Connection)} within
    * this time</li>
    * </ul>
    * <p>
    * This procedure allows to perform multiple operations on the same
    * {@link Connection} without committing.
    * 
    * @throws SQLException
    *            if the transaction cannot be started for some reason
    * @throws IllegalStateException
    *            if there is already an internal transaction active
    */
   public void beginTransaction() throws SQLException
   {
      if (_txCachedConnection.get() != null)
      {
         throw new IllegalStateException("Transaction already active!");
      }

      final Connection connection = _connectionProvider.borrow();
      connection.setAutoCommit(false);
      _txCachedConnection.set(connection);
   }

   /**
    * Commits the current internal transaction.
    * <p>
    * In particular this is achieve by performing the following steps:
    * <ul>
    * <li>Retrieve the internally cached {@link Connection}</li>
    * <li>Remove it from the cache</li>
    * <li>Call {@link Connection#commit()}</li>
    * <li>Set {@link Connection#setAutoCommit(boolean)} to {@code true}</li>
    * <li>Call {@link ConnectionProvider#release(Connection)} on the
    * {@link Connection}</li>
    * </ul>
    * 
    * @throws SQLException
    *            if the transaction cannot be committed for some reason
    * @throws IllegalStateException
    *            if there is no internal transaction active
    */
   public void commitTransaction() throws SQLException
   {
      final Connection connection = _txCachedConnection.get();
      if (connection == null)
      {
         throw new IllegalStateException("Transaction not active!");
      }

      _txCachedConnection.set(null);

      connection.commit();
      connection.setAutoCommit(true);

      release(connection);
   }

   /**
    * Rolls back the current internal transaction.
    * <p>
    * In particular this is achieve by performing the following steps:
    * <ul>
    * <li>Retrieve the internally cached {@link Connection}</li>
    * <li>Remove it from the cache</li>
    * <li>Call {@link Connection#rollback()}</li>
    * <li>Set {@link Connection#setAutoCommit(boolean)} to {@code true}</li>
    * <li>Call {@link ConnectionProvider#release(Connection)} on the
    * {@link Connection}</li>
    * </ul>
    * 
    * @throws SQLException
    *            if the transaction cannot be committed for some reason
    * @throws IllegalStateException
    *            if there is no internal transaction active
    */
   public void rollbackTransaction() throws SQLException
   {
      final Connection connection = _txCachedConnection.get();
      if (connection == null)
      {
         throw new IllegalStateException("Transaction not active!");
      }

      _txCachedConnection.set(null);

      connection.rollback();
      connection.setAutoCommit(true);

      release(connection);
   }

   /**
    * Performs a given {@link ConnectionCommand} on an internally managed
    * {@link Connection}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}.
    * 
    * @param command
    *           the {@link ConnectionCommand} to perform
    * @return an optional result object created by the {@link ConnectionCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withConnectionExecute(final ConnectionCommand<R> command) throws CommandException, SQLException
   {
      Connection connection = _txCachedConnection.get();
      final boolean isTxCached = connection != null;

      if (!isTxCached)
      {
         connection = _connectionProvider.borrow();
      }

      try
      {
         return command.perform(connection);
      }
      finally
      {
         if (!isTxCached)
         {
            release(connection);
         }
      }
   }

   /**
    * Performs a given {@link StatementCommand} on an internally managed
    * {@link Statement} and {@link Connection}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}. On this {@link Connection} a single {@link Statement}
    * will be created, handed to the {@link StatementCommand} and closed after
    * execution.
    * 
    * @param command
    *           the {@link StatementCommand} to perform
    * @return an optional result object created by the {@link StatementCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withStatementExecute(final StatementCommand<R> command) throws CommandException, SQLException
   {
      return withConnectionExecute(new ConnectionCommand<R>()
      {
         @Override
         public R perform(final Connection connection) throws CommandException, SQLException
         {
            final Statement statement = connection.createStatement();
            try
            {
               return command.perform(connection, statement);
            }
            finally
            {
               closeQuietly(statement);
            }
         }
      });
   }

   /**
    * Performs a given {@link PreparedStatementCommand} on an internally managed
    * {@link PreparedStatement} and {@link Connection}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}. On this {@link Connection} a single
    * {@link PreparedStatement} will be created using the given SQL
    * {@link String}, handed to the {@link PreparedStatementCommand} and closed
    * after execution. The optionally provided {@link PopulateCommand} will be
    * used to populate parameters to the {@link PreparedStatement} before
    * handing it to the {@link PreparedStatementCommand}.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementCommand} to perform
    * @param populateCommand
    *           the optional {@link PopulateCommand} used to populate parameters
    *           on the create {@link PreparedStatement}
    * @return an optional result object created by the
    *         {@link PreparedStatementCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementExecute(final String sql, final PopulateCommand populateCommand,
         final PreparedStatementCommand<R> command)
         throws CommandException, SQLException
   {
      return withConnectionExecute(new ConnectionCommand<R>()
      {
         @Override
         public R perform(final Connection connection) throws CommandException, SQLException
         {
            final PreparedStatement statement = connection.prepareStatement(sql);
            try
            {
               if (populateCommand != null)
               {
                  populateCommand.populateParameters(statement);
               }

               return command.perform(connection, statement);
            }
            finally
            {
               closeQuietly(statement);
            }
         }
      });
   }

   /**
    * Performs a given {@link PreparedStatementCommand} on an internally managed
    * {@link PreparedStatement} and {@link Connection}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}. On this {@link Connection} a single
    * {@link PreparedStatement} will be created using the given SQL
    * {@link String}, handed to the {@link PreparedStatementCommand} and closed
    * after execution.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementCommand} to perform
    * @return an optional result object created by the
    *         {@link PreparedStatementCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementExecute(final String sql, final PreparedStatementCommand<R> command)
         throws CommandException, SQLException
   {
      return withPreparedStatementExecute(sql, null, command);
   }

   /**
    * Convenience method to
    * {@link #withPreparedStatementExecute(String, PopulateCommand, PreparedStatementCommand)}
    * that takes one single {@link PreparedStatementPopulateCommand} for
    * parameter population and command execution.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementPopulateCommand} to use for
    *           parameter population and command performance
    * @return an optional result object created by the
    *         {@link PreparedStatementPopulateCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementExecute(final String sql, final PreparedStatementPopulateCommand<R> command)
         throws CommandException, SQLException
   {
      return withPreparedStatementExecute(sql, command, command);
   }

   /**
    * Executes a given SQL query on an internally managed {@link Connection} and
    * allows post-processing of the resulting {@link ResultSet} with a given
    * {@link PreparedStatementQueryCommand}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}. On this {@link Connection} a single
    * {@link PreparedStatement} will be created using the given SQL
    * {@link String}. Afterwards, parameters will be populated there using the
    * optionally provided {@link PopulateCommand}. Finally the query is
    * performed calling {@link PreparedStatement#executeQuery()} and the
    * resulting {@link ResultSet} is handed to the provided
    * {@link PreparedStatementQueryCommand} for post-processing.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementQueryCommand} for post-processing
    * @param populateCommand
    *           the optional {@link PopulateCommand} used to populate parameters
    *           on the create {@link PreparedStatement}
    * @return an optional result object created by the
    *         {@link PreparedStatementQueryCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementQueryExecute(final String sql,
         final PopulateCommand populateCommand,
         final PreparedStatementQueryCommand<R> command)
         throws CommandException, SQLException
   {
      return withPreparedStatementExecute(sql, populateCommand, new PreparedStatementCommand<R>()
      {
         @Override
         public R perform(final Connection connection, final PreparedStatement statement) throws CommandException,
               SQLException
         {
            final ResultSet resultSet = statement.executeQuery();
            try
            {
               return command.perform(connection, statement, resultSet);
            }
            finally
            {
               closeQuietly(resultSet);
            }
         }
      });
   }

   /**
    * Executes a given SQL query on an internally managed {@link Connection} and
    * allows post-processing of the resulting {@link ResultSet} with a given
    * {@link PreparedStatementQueryCommand}.
    * <p>
    * This will take care to get, prepare and cleanup the {@link Connection}
    * using the internally registered {@link ConnectionProvider} or
    * {@link DataSource}. On this {@link Connection} a single
    * {@link PreparedStatement} will be created using the given SQL
    * {@link String}, the query is performed calling
    * {@link PreparedStatement#executeQuery()} and the resulting
    * {@link ResultSet} is handed to the provided
    * {@link PreparedStatementQueryCommand} for post-processing.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementQueryCommand} for post-processing
    * @return an optional result object created by the
    *         {@link PreparedStatementQueryCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementQueryExecute(final String sql, final PreparedStatementQueryCommand<R> command)
         throws CommandException, SQLException
   {
      return withPreparedStatementQueryExecute(sql, null, command);
   }

   /**
    * Convenience method to
    * {@link #withPreparedStatementQueryExecute(String, PopulateCommand, PreparedStatementQueryCommand)}
    * that takes one single {@link PreparedStatementPopulateQueryCommand} for
    * parameter population and {@link ResultSet} post-processing.
    * 
    * @param sql
    *           the SQL command to use for creating the
    *           {@link PreparedStatement}
    * @param command
    *           the {@link PreparedStatementPopulateQueryCommand} to use for
    *           parameter population and {@link ResultSet} post-processing
    * @return an optional result object created by the
    *         {@link PreparedStatementPopulateCommand}
    * @throws SQLException
    *            if some JDBC specific operations failed within command
    *            execution
    * @throws CommandException
    *            if some non-JDBC specific operations failed within command
    *            execution
    */
   public <R> R withPreparedStatementQueryExecute(final String sql,
         final PreparedStatementPopulateQueryCommand<R> command) throws CommandException, SQLException
   {
      return withPreparedStatementQueryExecute(sql, command, command);
   }

   /**
    * A callback command interface for performing any JDBC operations on a given
    * {@link Connection}.
    * 
    * @param <R>
    *           the type of an optional result object that the command can
    *           produce
    */
   public static interface ConnectionCommand<R>
   {
      /**
       * Performs any operations on the given {@link Connection}.
       * 
       * @param connection
       *           the {@link Connection} to perform operations on
       * @return an optional result object (will be returned by any methods
       *         receiving this as a parameter)
       * @throws SQLException
       *            may be thrown if any internal JDBC operations fail
       * @throws CommandException
       *            may be thrown if any internal other operations fail
       * @see JDBCExecutor#withConnectionExecute(ConnectionCommand)
       */
      R perform(final Connection connection) throws CommandException, SQLException;
   }

   /**
    * A callback command interface for performing any JDBC operations on a given
    * {@link Statement} and it's parent {@link Connection}.
    * 
    * @param <R>
    *           the type of an optional result object that the command can
    *           produce
    */
   public static interface StatementCommand<R>
   {
      /**
       * Performs any operations on the given {@link Statement} and it's parent
       * {@link Connection}.
       * 
       * @param connection
       *           the parent {@link Connection} of the given {@link Statement}
       * @param statement
       *           the {@link Statement} for perform operations on
       * @return an optional result object (will be returned by any methods
       *         receiving this as a parameter)
       * @throws SQLException
       *            may be thrown if any internal JDBC operations fail
       * @throws CommandException
       *            may be thrown if any internal other operations fail
       * @see JDBCExecutor#withStatementExecute(StatementCommand)
       */
      R perform(final Connection connection, final Statement statement) throws CommandException, SQLException;
   }

   /**
    * A callback command interface for performing any JDBC operations on a given
    * {@link PreparedStatement} and it's parent {@link Connection}.
    * 
    * @param <R>
    *           the type of an optional result object that the command can
    *           produce
    */
   public static interface PreparedStatementCommand<R>
   {
      /**
       * Performs any operations on the given {@link PreparedStatement} and it's
       * parent {@link Connection}.
       * 
       * @param connection
       *           the parent {@link Connection} of the given
       *           {@link PreparedStatement}
       * @param statement
       *           the {@link PreparedStatement} for perform operations on
       * @return an optional result object (will be returned by any methods
       *         receiving this as a parameter)
       * @throws SQLException
       *            may be thrown if any internal JDBC operations fail
       * @throws CommandException
       *            may be thrown if any internal other operations fail
       * @see JDBCExecutor#withPreparedStatementExecute(String, PopulateCommand,
       *      PreparedStatementCommand)
       * @see JDBCExecutor#withPreparedStatementExecute(String,
       *      PreparedStatementCommand)
       */
      R perform(final Connection connection, final PreparedStatement statement) throws CommandException, SQLException;
   }

   /**
    * A callback command interface for processing JDBC query results on a given
    * {@link ResultSet}, it's parent {@link PreparedStatement} and that's parent
    * {@link Connection}.
    * 
    * @param <R>
    *           the type of an optional result object that the command can
    *           produce
    */
   public static interface PreparedStatementQueryCommand<R>
   {
      /**
       * Processes the JDBC query results on a given {@link ResultSet}, it's
       * parent {@link PreparedStatement} and that's parent {@link Connection}.
       * 
       * @param connection
       *           the parent {@link Connection} of the given
       *           {@link PreparedStatement}
       * @param statement
       *           the parent {@link PreparedStatement} of the given
       *           {@link ResultSet}
       * @param resultSet
       *           the {@link ResultSet} that should be processed
       * @return an optional result object (will be returned by any methods
       *         receiving this as a parameter)
       * @throws SQLException
       *            may be thrown if any internal JDBC operations fail
       * @throws CommandException
       *            may be thrown if any internal other operations fail
       * @see JDBCExecutor#withPreparedStatementQueryExecute(String,
       *      PopulateCommand, PreparedStatementQueryCommand)
       * @see JDBCExecutor#withPreparedStatementQueryExecute(String,
       *      PreparedStatementQueryCommand)
       * @see JDBCExecutor#withPreparedStatementQueryExecute(String,
       *      PreparedStatementPopulateQueryCommand)
       */
      R perform(final Connection connection, final PreparedStatement statement, ResultSet resultSet)
            throws CommandException, SQLException;
   }

   /**
    * A callback command interface for populating a given
    * {@link PreparedStatement} with parameters.
    */
   public static interface PopulateCommand
   {
      /**
       * Populates parameters on a given {@link PreparedStatement}
       * 
       * @param statement
       *           the {@link PreparedStatement} to populate parameters on
       * @throws SQLException
       *            may be thrown if any internal JDBC operations fail
       * @throws CommandException
       *            may be thrown if any internal other operations fail
       */
      void populateParameters(PreparedStatement statement) throws CommandException, SQLException;
   }

   /**
    * A combined interface for {@link PreparedStatementQueryCommand} and
    * {@link PopulateCommand}
    */
   public static interface PreparedStatementPopulateQueryCommand<R> extends PreparedStatementQueryCommand<R>,
         PopulateCommand
   {}

   /**
    * A combined interface for {@link PreparedStatementCommand} and
    * {@link PopulateCommand}
    */
   public static interface PreparedStatementPopulateCommand<R> extends PreparedStatementCommand<R>, PopulateCommand
   {}

   /**
    * An interface for providers for JDBC {@link Connection} objects.
    */
   public static interface ConnectionProvider
   {
      /**
       * Called by {@link JDBCExecutor} to retrieve a new or existing JDBC
       * {@link Connection} to be used for the next DB operation.
       * 
       * @return a new or existing JDBC {@link Connection}
       * @throws SQLException
       *            if connection lookup/creation fails
       */
      Connection borrow() throws SQLException;

      /**
       * Called by {@link JDBCExecutor} after performing a DB operation or
       * committing or rolling back transaction - should cleanup any resources
       * if appropriate.
       * 
       * @param connection
       *           the {@link Connection} that was just used by
       *           {@link JDBCExecutor} and can now be released
       */
      void release(Connection connection);
   }

   /**
    * A specific exception class to be used for reporting any non-JDBC problem
    * that occur within any JDBC command performed by {@link JDBCExecutor}.
    */
   public static class CommandException extends Exception
   {
      private static final long serialVersionUID = 934499553982637916L;

      public CommandException()
      {
         super();
      }

      public CommandException(final String message, final Throwable cause)
      {
         super(message, cause);
      }

      public CommandException(final String message)
      {
         super(message);
      }

      public CommandException(final Throwable cause)
      {
         super(cause);
      }
   }

   private static class DataSourceConnectionProviderAdapter implements ConnectionProvider
   {
      private final DataSource _dataSource;
      private final boolean _autoClose;

      private DataSourceConnectionProviderAdapter(final DataSource dataSource, final boolean autoClose)
      {
         _dataSource = dataSource;
         _autoClose = autoClose;
      }

      @Override
      public Connection borrow() throws SQLException
      {
         return _dataSource.getConnection();
      }

      @Override
      public void release(final Connection connection)
      {
         if (_autoClose)
         {
            try
            {
               connection.close();
            }
            catch (final Exception ignore)
            {
            }
         }
      }
   }

   private void release(final Connection connection)
   {
      _connectionProvider.release(connection);
   }

   private static void closeQuietly(final Statement connection)
   {
      try
      {
         connection.close();
      }
      catch (final Exception ignore)
      {
      }
   }

   private static void closeQuietly(final ResultSet resultSet)
   {
      try
      {
         resultSet.close();
      }
      catch (final Exception ignore)
      {
      }
   }
}
