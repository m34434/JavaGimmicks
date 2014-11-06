package net.sf.javagimmicks.sql.testing;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;

/**
 * An abstract JUnit {@link TestRule} that creates a temporary file-based
 * (embedded) database during test execution and wraps a Commons-DBCP
 * {@link BasicDataSource} around it.
 * <p>
 * The developer can influence the temporary folder, where the database should
 * be created and/or can provide initial configuration logic for the used
 * {@link BasicDataSource} via a {@link DataSourceConfigurator}.
 */
public abstract class AbstractDbTestRule extends ExternalResource
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
    * Creates a new instance using the given {@link DataSourceConfigurator} and
    * {@link File folder} for the internal database.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   protected AbstractDbTestRule(final DataSourceConfigurator configurator, final File dbFolder)
   {
      _configurator = configurator;
      _folderRule = new TemporaryFolder(dbFolder);
   }

   /**
    * Creates a new instance using the given {@link DataSourceConfigurator} for
    * the internal database.
    * 
    * @param configurator
    *           a {@link DataSourceConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource}
    */
   protected AbstractDbTestRule(final DataSourceConfigurator configurator)
   {
      this(configurator, null);
   }

   /**
    * Creates a new instance using the given {@link File folder} for the
    * internal database.
    * 
    * @param dbFolder
    *           the temporary folder where the database should be set up
    */
   protected AbstractDbTestRule(final File dbFolder)
   {
      this(null, dbFolder);
   }

   /**
    * Creates a new instance with no special configuration.
    */
   protected AbstractDbTestRule()
   {
      this(null, null);
   }

   /**
    * Returns the {@link BasicDataSource} representing the temporary embedded
    * database.
    * 
    * @return the {@link BasicDataSource} representing the temporary embedded
    *         database
    */
   public BasicDataSource getDataSource()
   {
      return _dataSource;
   }

   /**
    * Retrieves a new {@link Connection} to the temporary embedded database.
    * 
    * @return a new {@link Connection} to the temporary embedded database
    * 
    * @throws SQLException
    *            if a database access error occurs
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
      configureDatasource(_dataSource, tempFolder);

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

   abstract protected void configureDatasource(BasicDataSource dataSource, File tempFolder)
         throws Exception;

   /**
    * A command interface for providing custom configuration logic for the
    * internal {@link BasicDataSource}.
    */
   public static interface DataSourceConfigurator
   {
      /**
       * Configures the {@link BasicDataSource} used for testing.
       * 
       * @param dataSource
       *           the {@link BasicDataSource} to configure
       */
      void configure(BasicDataSource dataSource);
   }
}
