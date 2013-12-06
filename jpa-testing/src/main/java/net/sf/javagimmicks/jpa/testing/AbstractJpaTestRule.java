package net.sf.javagimmicks.jpa.testing;

import java.io.File;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.javagimmicks.sql.testing.AbstractDbTestRule;
import net.sf.javagimmicks.sql.testing.AbstractDbTestRule.DataSourceConfigurator;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * A JUnit {@link TestRule} that creates a temporary file-based database (using
 * an {@link AbstractDbTestRule} instance) during test execution and wraps
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
 */
public abstract class AbstractJpaTestRule extends ExternalResource
{
   private final String[] _entityPackages;
   private final EntityManagerFactoryConfigurator _configurator;

   private final AbstractDbTestRule _db;

   private LocalContainerEntityManagerFactoryBean _localEmf;

   protected AbstractJpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final File dbFolder,
         final String... entityPackages)
   {
      if (entityPackages == null || entityPackages.length == 0)
      {
         throw new IllegalArgumentException("At least one entity package must be specified!");
      }

      _entityPackages = entityPackages;
      _configurator = configurator;
      _db = createDb(configurator, dbFolder);
   }

   protected AbstractJpaTestRule(final File dbFolder, final String... entityPackages)
   {
      this(null, dbFolder, entityPackages);
   }

   protected AbstractJpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final String... entityPackages)
   {
      this(configurator, null, entityPackages);
   }

   protected AbstractJpaTestRule(final String... entityPackages)
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

   abstract protected AbstractDbTestRule createDb(DataSourceConfigurator configurator, File dbFolder);

   abstract protected Database getDatabaseType();

   protected JpaVendorAdapter createVendorAdapter()
   {
      final HibernateJpaVendorAdapter hibernate = new HibernateJpaVendorAdapter();
      hibernate.setDatabase(getDatabaseType());
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
