package net.sf.javagimmicks.sql.testing;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.TestRule;

/**
 * A JUnit {@link TestRule} that creates a temporary file-based Hypersonic SQL
 * database during test execution and wraps a Commons-DBCP
 * {@link BasicDataSource} around it.
 * <p>
 * The developer can influence the temporary folder, where the database should
 * be created and/or can provide initial configuration logic for the used
 * {@link BasicDataSource} via a {@link DataSourceConfigurator}.
 * <p>
 * <b>Usage example:</b>
 * 
 * <pre>
 * public class HsqlDbTestRuleTest
 * {
 * 
 *    &#064;Rule
 *    public HsqlDbTestRule _db = new HsqlDbTestRule();
 * 
 *    &#064;Test
 *    public void test() throws SQLException
 *    {
 *       final Connection connection = _db.getConnection();
 *       assertNotNull(connection);
 * 
 *       // Do some DB operations
 * 
 *       conneciton.close();
 *    }
 * }
 * </pre>
 */
public class HsqlDbTestRule extends AbstractDbTestRule
{
   /**
    * Creates a new instance using the given {@link DataSourceConfigurator} and
    * {@link File folder} for the internal database.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   public HsqlDbTestRule(final DataSourceConfigurator configurator, final File dbFolder)
   {
      super(configurator, dbFolder);
   }

   /**
    * Creates a new instance using the given {@link DataSourceConfigurator} for
    * the internal database.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    */
   public HsqlDbTestRule(final DataSourceConfigurator configurator)
   {
      super(configurator);
   }

   /**
    * Creates a new instance using the given {@link File folder} for the
    * internal database.
    * 
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   public HsqlDbTestRule(final File dbFolder)
   {
      super(dbFolder);
   }

   /**
    * Creates a new instance with no special configuration.
    */
   public HsqlDbTestRule()
   {
      super();
   }

   @Override
   protected void configureDatasource(final BasicDataSource dataSource, final File tempFolder)
         throws MalformedURLException
   {
      dataSource.setDriverClassName(org.hsqldb.jdbcDriver.class.getName());
      dataSource.setUrl(buildJdbcUrl(tempFolder));
      dataSource.setUsername("sa");
      dataSource.setPassword("");
   }

   private static String buildJdbcUrl(final File tempFolder) throws MalformedURLException
   {
      final String url = "jdbc:hsqldb:" + tempFolder.toURI().toURL() + NAME_TEST_DB;
      return url;
   }
}
