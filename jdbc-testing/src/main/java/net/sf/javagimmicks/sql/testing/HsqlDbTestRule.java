package net.sf.javagimmicks.sql.testing;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
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
public class HsqlDbTestRule extends ExternalResource
{
   /**
    * The name of the temporary Hypersonic SQL database that is creating during
    * test runs.
    */
   public static final String NAME_TEST_DB = "test-db";

   private final TemporaryFolder _folderRule;
   private final DataSourceConfigurator _configurator;

   private BasicDataSource _dataSource;

   /**
    * Creates a new instance.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   public HsqlDbTestRule(final DataSourceConfigurator configurator, final File dbFolder)
   {
      _configurator = configurator;
      _folderRule = new TemporaryFolder(dbFolder);
   }

   /**
    * Creates a new instance.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    */
   public HsqlDbTestRule(final DataSourceConfigurator configurator)
   {
      this(configurator, null);
   }

   /**
    * Creates a new instance.
    * 
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   public HsqlDbTestRule(final File dbFolder)
   {
      this(null, dbFolder);
   }

   /**
    * Creates a new instance.
    */
   public HsqlDbTestRule()
   {
      this(null, null);
   }

   /**
    * Returns the {@link BasicDataSource} representing the temporary Hypersonic
    * SQL database.
    * 
    * @return the {@link BasicDataSource} representing the temporary Hypersonic
    *         SQL database
    */
   public BasicDataSource getDataSource()
   {
      return _dataSource;
   }

   /**
    * Retrieves a new {@link Connection} to the temporary Hypersonic SQL
    * database.
    * 
    * @return a new {@link Connection} to the temporary Hypersonic SQL database
    */
   public Connection getConnection() throws SQLException
   {
      return getDataSource().getConnection();
   }

   /**
    * Returns the folder where the test database is or will be created.
    * 
    * @return the folder where the test database is or will be created
    */
   public File getDatabaseFolder()
   {
      return _folderRule.getRoot();
   }

   public void init() throws Throwable
   {
      before();
   }

   public void shutdown()
   {
      after();
   }

   @Override
   protected void before() throws Throwable
   {
      _folderRule.create();
      final File tempFolder = _folderRule.getRoot();

      _dataSource = new BasicDataSource();
      _dataSource.setDriverClassName(org.hsqldb.jdbcDriver.class.getName());
      _dataSource.setUrl(buildJdbcUrl(tempFolder));
      _dataSource.setUsername("sa");
      _dataSource.setPassword("");

      if (_configurator != null)
      {
         _configurator.configure(_dataSource);
      }
   }

   @Override
   protected void after()
   {
      try
      {
         _dataSource.close();
      }
      catch (final SQLException e)
      {
      }

      _dataSource = null;

      _folderRule.delete();
   }

   private static String buildJdbcUrl(final File tempFolder) throws MalformedURLException
   {
      final String url = "jdbc:hsqldb:" + tempFolder.toURI().toURL() + NAME_TEST_DB;
      return url;
   }

   /**
    * A command interface for providing custom configuration logic for the
    * internal {@link BasicDataSource}.
    */
   public static interface DataSourceConfigurator
   {
      /**
       * Configures the {@link BasicDataSource} used for testing
       * 
       * @param dataSource
       *           the {@link BasicDataSource} to configure
       */
      void configure(BasicDataSource dataSource);
   }
}
