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

   public JpaTestRule(final File dbFolder, final String... entityPackages)
   {
      this(null, dbFolder, entityPackages);
   }

   public JpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final String... entityPackages)
   {
      this(configurator, null, entityPackages);
   }

   public JpaTestRule(final String... entityPackages)
   {
      this(null, null, entityPackages);
   }

   public EntityManagerFactory getEntityManagerFactory()
   {
      return _localEmf.getNativeEntityManagerFactory();
   }

   public EntityManager createEntityManager()
   {
      return getEntityManagerFactory().createEntityManager();
   }

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

   public static interface EntityManagerFactoryConfigurator extends DataSourceConfigurator
   {
      void configure(LocalContainerEntityManagerFactoryBean factoryBean);
   }
}
