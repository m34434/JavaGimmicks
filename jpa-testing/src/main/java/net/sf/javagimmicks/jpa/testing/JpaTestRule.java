package net.sf.javagimmicks.jpa.testing;

import java.io.File;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.javagimmicks.sql.testing.HsqlDbTestRule;
import net.sf.javagimmicks.sql.testing.HsqlDbTestRule.DataSourceConfigurator;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * A JUnit {@link TestRule} that creates a temporary file-based Hypersonic SQL
 * database (using {@link HsqlDbTestRule}) during test execution and wraps
 * Spring/Hibernate based local JPA container around it.
 * <p>
 * The developer can influence the temporary folder, where the database should
 * be created and/or can provide initial configuration logic for the used
 * {@link BasicDataSource} and {@link LocalContainerEntityManagerFactoryBean}
 * via a {@link EntityManagerFactoryConfigurator}.
 * <p>
 * There is no need for a {@code persistence.xml} file for test execution,
 * instead a list of package names must be provided that will automatically be
 * scanned for entity classes.
 * <p>
 * <b>Usage example:</b>
 * 
 * <pre>
 * public class HsqlDbTestRuleTest
 * {
 * 
 *    &#064;Rule
 *    public JpaTestRule _jpa = new JpaTestRule(FooEntity.class.getPackage().getName());
 * 
 *    &#064;Test
 *    public void test() throws SQLException
 *    {
 *       final EntityManager em = _jpa.createEntityManager();
 *       Assert.assertNotNull(em);
 * 
 *       em.getTransaction().begin();
 * 
 *       // Do some JPA operations
 * 
 *       em.getTranscation().commit();
 * 
 *       em.close();
 *    }
 * }
 * </pre>
 */
public class JpaTestRule extends ExternalResource
{
   private final String[] _entityPackages;
   private final EntityManagerFactoryConfigurator _configurator;

   private final HsqlDbTestRule _db;

   private LocalContainerEntityManagerFactoryBean _localEmf;

   /**
    * Creates a new instance using the given
    * {@link EntityManagerFactoryConfigurator} and {@link File folder} for the
    * internal database.
    * 
    * @param configurator
    *           a {@link EntityManagerFactoryConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource} and
    *           {@link LocalContainerEntityManagerFactoryBean}
    * @param dbFolder
    *           the temporary folder where the database should be set up
    * @param entityPackages
    *           a list of names of packages to scan for entity classes
    * @throws IllegalArgumentException
    *            if the list of package names is empty
    */
   public JpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final File dbFolder,
         final String... entityPackages)
   {
      if (entityPackages == null || entityPackages.length == 0)
      {
         throw new IllegalArgumentException("At least one entity package must be specified!");
      }

      _entityPackages = entityPackages;
      _configurator = configurator;
      _db = new HsqlDbTestRule(configurator, dbFolder);
   }

   /**
    * Creates a new instance using the given {@link File folder} for the
    * internal database.
    * 
    * @param dbFolder
    *           the temporary folder where the database should be set up
    * @param entityPackages
    *           a list of names of packages to scan for entity classes
    * @throws IllegalArgumentException
    *            if the list of package names is empty
    */
   public JpaTestRule(final File dbFolder, final String... entityPackages)
   {
      this(null, dbFolder, entityPackages);
   }

   /**
    * Creates a new instance using the given
    * {@link EntityManagerFactoryConfigurator} for the internal database.
    * 
    * @param configurator
    *           a {@link EntityManagerFactoryConfigurator} for performing custom
    *           configuration logic on the internal {@link BasicDataSource} and
    *           {@link LocalContainerEntityManagerFactoryBean}
    * @param entityPackages
    *           a list of names of packages to scan for entity classes
    * @throws IllegalArgumentException
    *            if the list of package names is empty
    */
   public JpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final String... entityPackages)
   {
      this(configurator, null, entityPackages);
   }

   /**
    * Creates a new instance with no special configuration.
    * 
    * @param entityPackages
    *           a list of names of packages to scan for entity classes
    * @throws IllegalArgumentException
    *            if the list of package names is empty
    */
   public JpaTestRule(final String... entityPackages)
   {
      this(null, null, entityPackages);
   }

   /**
    * Returns the internal {@link EntityManagerFactory}.
    * 
    * @return the internal {@link EntityManagerFactory}
    */
   public EntityManagerFactory getEntityManagerFactory()
   {
      return _localEmf.getNativeEntityManagerFactory();
   }

   /**
    * Creates and returns a new {@link EntityManager} using the internal
    * {@link EntityManagerFactory}.
    * 
    * @return the new {@link EntityManager}
    */
   public EntityManager createEntityManager()
   {
      return getEntityManagerFactory().createEntityManager();
   }

   /**
    * Creates and returns a new {@link EntityManager} using the internal
    * {@link EntityManagerFactory} and applying the given {@link Map} of
    * properties.
    * <p>
    * As <i>Hibernate</i> is used as internal JPA layer, the properties need to
    * be Hibernate-specific.
    * 
    * @param properties
    *           a {@link Map} of properties to use for creating the
    *           {@link EntityManager}
    * 
    * @return the new {@link EntityManager}
    */
   @SuppressWarnings("rawtypes")
   public EntityManager createEntityManager(final Map properties)
   {
      return getEntityManagerFactory().createEntityManager(properties);
   }

   @Override
   protected void before() throws Throwable
   {
      _db.init();

      _localEmf = new LocalContainerEntityManagerFactoryBean();
      if (_configurator != null)
      {
         _configurator.configure(_localEmf);
      }

      _localEmf.setDataSource(_db.getDataSource());
      _localEmf.setJpaVendorAdapter(createVendorAdapter());
      _localEmf.setPackagesToScan(_entityPackages);

      _localEmf.afterPropertiesSet();
   }

   @Override
   protected void after()
   {
      _localEmf.destroy();
      _localEmf = null;

      _db.shutdown();
   }

   protected JpaVendorAdapter createVendorAdapter()
   {
      final HibernateJpaVendorAdapter hibernate = new HibernateJpaVendorAdapter();
      hibernate.setDatabase(Database.HSQL);
      hibernate.setGenerateDdl(true);
      hibernate.setShowSql(false);

      return hibernate;
   }

   /**
    * A command interface for providing custom configuration logic for the
    * internal {@link LocalContainerEntityManagerFactoryBean} and
    * {@link BasicDataSource}.
    */
   public static interface EntityManagerFactoryConfigurator extends DataSourceConfigurator
   {
      /**
       * Configures the {@link LocalContainerEntityManagerFactoryBean} used for
       * testing.
       * 
       * @param factoryBean
       *           the {@link LocalContainerEntityManagerFactoryBean} to
       *           configure
       */
      void configure(LocalContainerEntityManagerFactoryBean factoryBean);
   }
}
