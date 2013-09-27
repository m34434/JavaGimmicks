package net.sf.javagimmicks.sql.testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class HsqlDbTestRuleTest
{
   @Rule
   public HsqlDbTestRule _db = new HsqlDbTestRule();

   @Test
   public void test() throws SQLException
   {
      Assert.assertTrue(_db.getDatabaseFolder().exists());

      final Connection connection = _db.getConnection();
      Assert.assertNotNull(connection);

      final Statement stmtCreate = connection.createStatement();
      Assert.assertNotNull(stmtCreate);

      stmtCreate.execute("CREATE TABLE temp (key INTEGER, value VARCHAR(255))");
      stmtCreate.close();

      final Statement stmtInsert = connection.createStatement();
      Assert.assertNotNull(stmtInsert);

      stmtInsert.execute("INSERT INTO temp VALUES (1, 'test')");
      stmtInsert.close();

      connection.close();
   }
}
