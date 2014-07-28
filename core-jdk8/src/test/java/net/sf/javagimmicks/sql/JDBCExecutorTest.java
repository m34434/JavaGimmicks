package net.sf.javagimmicks.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.sql.JDBCExecutor.CommandException;
import net.sf.javagimmicks.sql.JDBCExecutor.PreparedStatementQueryCommand;
import net.sf.javagimmicks.sql.JDBCExecutor.StatementCommand;
import net.sf.javagimmicks.sql.testing.H2DbTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class JDBCExecutorTest
{
   @Rule
   public H2DbTestRule _db = new H2DbTestRule();

   private JDBCExecutor _jdbc;

   @Before
   public void setup()
   {
      _jdbc = new JDBCExecutor(_db.getDataSource());
   }

   @Test
   public void createTable() throws Exception
   {
      _jdbc.executeStatment(CREATE_TABLE_FOO);
   }

   @Test
   public void insertValues() throws Exception
   {
      createTable();
      _jdbc.executePreparedStatement(InsertFooCommand.SQL, new InsertFooCommand(1, "Foo", 2, "Bar"));
   }

   @Test
   public void selectValues() throws Exception
   {
      insertValues();

      final List<FooEntry> entries = _jdbc.executePreparedStatementQuery(ExtractFooEntriesCommand.SQL,
            new ExtractFooEntriesCommand());

      assertEquals(2, entries.size());
      assertEquals(1, entries.get(0).getKey());
      assertEquals(2, entries.get(1).getKey());
      assertEquals("Foo", entries.get(0).getValue());
      assertEquals("Bar", entries.get(1).getValue());
   }

   private static final StatementCommand<Void, Statement> CREATE_TABLE_FOO = new StatementCommand<Void, Statement>() {
      @Override
      public Void perform(final Connection connection, final Statement statement) throws CommandException,
            SQLException
      {
         statement.execute("CREATE TABLE foo(key INTEGER, value VARCHAR(255))");
         return null;
      }
   };

   private static class InsertFooCommand implements StatementCommand<Void, PreparedStatement>
   {
      public static final String SQL = "INSERT INTO foo VALUES(?, ?)";

      private final Object[] _args;

      public InsertFooCommand(final Object... args)
      {
         this._args = args;
      }

      @Override
      public Void perform(final Connection connection, final PreparedStatement statement) throws CommandException,
            SQLException
      {
         for (int cmdIndex = 0; cmdIndex < _args.length / 2; ++cmdIndex)
         {
            statement.setInt(1, (Integer) _args[cmdIndex * 2]);
            statement.setString(2, (String) _args[cmdIndex * 2 + 1]);

            statement.executeUpdate();
         }

         return null;
      }
   }

   private static class ExtractFooEntriesCommand implements
         PreparedStatementQueryCommand<List<FooEntry>, PreparedStatement>
   {
      public static final String SQL = "SELECT * FROM foo";

      @Override
      public List<FooEntry> perform(final Connection connection, final PreparedStatement statement,
            final ResultSet resultSet)
            throws CommandException, SQLException
      {
         final List<FooEntry> result = new ArrayList<FooEntry>();

         while (resultSet.next())
         {
            result.add(FooEntry.fromResultSet(resultSet));
         }

         Collections.sort(result);

         return result;
      }
   }

   private static class FooEntry implements Comparable<FooEntry>
   {
      private final int _key;
      private final String _value;

      public static FooEntry fromResultSet(final ResultSet resultSet) throws SQLException
      {
         return new FooEntry(resultSet.getInt(1), resultSet.getString(2));
      }

      public FooEntry(final int key, final String value)
      {
         this._key = key;
         this._value = value;
      }

      public int getKey()
      {
         return _key;
      }

      public String getValue()
      {
         return _value;
      }

      @Override
      public int compareTo(final FooEntry o)
      {
         if (o == null)
         {
            return Integer.MIN_VALUE;
         }

         return this.getKey() - o.getKey();
      }
   }
}
