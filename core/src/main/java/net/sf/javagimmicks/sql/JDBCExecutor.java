package net.sf.javagimmicks.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class JDBCExecutor
{
   private final ThreadLocal<Connection> _txCachedConnection = new ThreadLocal<Connection>();
   private final ConnectionProvider _connectionProvider;

   public JDBCExecutor(final ConnectionProvider connectionProvider)
   {
      _connectionProvider = connectionProvider;
   }

   public JDBCExecutor(final DataSource dataSource)
   {
      this(new DataSourceConnectionProviderAdapter(dataSource));
   }

   public void beginInternalTransaction(final TransactionProvider txProvider) throws ConnectException
   {
      if (_txCachedConnection.get() == null)
      {
         _txCachedConnection.set(_connectionProvider.getConnection());
      }

      if (txProvider != null && !txProvider.isTransactionActive())
      {
         try
         {
            _txCachedConnection.get().setAutoCommit(false);
         }
         catch (final SQLException e)
         {
            throw new ConnectException(e);
         }
      }
   }

   public void commitInternalTransaction(final TransactionProvider txProvider) throws ConnectException
   {
      final Connection connection = _txCachedConnection.get();
      if (connection == null)
      {
         return;
      }

      if (txProvider != null && !txProvider.isTransactionActive())
      {
         try
         {
            connection.commit();
            connection.setAutoCommit(true);
         }
         catch (final SQLException e)
         {
            throw new ConnectException(e);
         }
      }

      closeQuietly(connection);
      _txCachedConnection.set(null);
   }

   public void rollbackInternalTransaction(final TransactionProvider txProvider) throws ConnectException
   {
      final Connection connection = _txCachedConnection.get();
      if (connection == null)
      {
         return;
      }

      if (txProvider != null && !txProvider.isTransactionActive())
      {
         try
         {
            connection.rollback();
            connection.setAutoCommit(true);
         }
         catch (final SQLException e)
         {
            throw new ConnectException(e);
         }
      }

      closeQuietly(connection);
      _txCachedConnection.set(null);
   }

   public <R> R withConnectionExecute(final ConnectionCommand<R> command) throws Exception
   {
      Connection connection = _txCachedConnection.get();
      final boolean isTxCached = connection != null;

      if (!isTxCached)
      {
         connection = _connectionProvider.getConnection();
      }

      try
      {
         return command.perform(connection);
      }
      finally
      {
         if (!isTxCached)
         {
            closeQuietly(connection);
         }
      }
   }

   public <R> R withStatementExecute(final StatementCommand<R> command) throws Exception
   {
      return withConnectionExecute(new ConnectionCommand<R>()
      {
         @Override
         public R perform(final Connection connection) throws Exception
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

   public <R> R withPreparedStatementExecute(final String sql, final PreparedStatementCommand<R> command)
         throws Exception
   {
      return withConnectionExecute(new ConnectionCommand<R>()
      {
         @Override
         public R perform(final Connection connection) throws Exception
         {
            final PreparedStatement statement = connection.prepareStatement(sql);
            try
            {
               command.populateParameters(statement);
               return command.perform(connection, statement);
            }
            finally
            {
               closeQuietly(statement);
            }
         }
      });
   }

   public <R> R withPreparedStatementQueryExecute(final String sql, final PreparedStatementQueryCommand<R> command)
         throws Exception
   {
      return withPreparedStatementExecute(sql, new PreparedStatementCommand<R>()
      {
         @Override
         public void populateParameters(final PreparedStatement statement) throws Exception
         {
            command.populateParameters(statement);
         }

         @Override
         public R perform(final Connection connection, final PreparedStatement statement) throws Exception
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

   public static interface ConnectionCommand<R>
   {
      public R perform(final Connection connection) throws Exception;
   }

   public static interface StatementCommand<R>
   {
      public R perform(final Connection connection, final Statement statement) throws Exception;
   }

   public static abstract class PreparedStatementCommand<R>
   {
      public void populateParameters(final PreparedStatement statement) throws Exception
      {}

      abstract public R perform(final Connection connection, final PreparedStatement statement) throws Exception;
   }

   public static interface PreparedStatementQueryCommand<R>
   {
      public void populateParameters(PreparedStatement statement) throws Exception;

      public R perform(final Connection connection, final PreparedStatement statement, ResultSet resultSet)
            throws Exception;
   }

   public static interface ConnectionProvider
   {
      public Connection getConnection() throws ConnectException;
   }

   public static interface TransactionProvider
   {
      public boolean isTransactionActive();
   }

   public static class ConnectException extends Exception
   {
      private static final long serialVersionUID = 4656300221590786760L;

      public ConnectException()
      {
         super();
      }

      public ConnectException(final String message, final Throwable cause)
      {
         super(message, cause);
      }

      public ConnectException(final String message)
      {
         super(message);
      }

      public ConnectException(final Throwable cause)
      {
         super(cause);
      }
   }

   private static class DataSourceConnectionProviderAdapter implements ConnectionProvider
   {
      private final DataSource _dataSource;

      private DataSourceConnectionProviderAdapter(final DataSource dataSource)
      {
         _dataSource = dataSource;
      }

      @Override
      public Connection getConnection() throws ConnectException
      {
         try
         {
            return _dataSource.getConnection();
         }
         catch (final SQLException e)
         {
            throw new ConnectException(e);
         }
      }
   }

   private static void closeQuietly(final Connection connection)
   {
      try
      {
         connection.close();
      }
      catch (final Exception ignore)
      {
      }
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
