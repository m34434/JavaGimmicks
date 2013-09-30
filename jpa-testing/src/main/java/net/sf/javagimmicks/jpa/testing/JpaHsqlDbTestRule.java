package net.sf.javagimmicks.jpa.testing;

import java.io.File;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.javagimmicks.sql.testing.HsqlDbTestRule;
import net.sf.javagimmicks.sql.testing.HsqlDbTestRule.DataSourceConfigurator;

import org.junit.rules.ExternalResource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class JpaHsqlDbTestRule extends ExternalResource
{
   private final String[] _entityPackages;
   private final EntityManagerFactoryConfigurator _configurator;

   private final HsqlDbTestRule _db;

   private LocalContainerEntityManagerFactoryBean _localEmf;

   public JpaHsqlDbTestRule(final EntityManagerFactoryConfigurator emfConfigurator,
         final DataSourceConfigurator dsConfigurator, final File dbFolder,
         final String... entityPackages)
   {
      _entityPackages = entityPackages;
      _configurator = emfConfigurator;
      _db = new HsqlDbTestRule(dsConfigurator, dbFolder);
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
      final EntityManagerFactory emf = getEntityManagerFactory();
      if (emf != null && emf.isOpen())
      {
         emf.close();
      }

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

   public static interface EntityManagerFactoryConfigurator
   {
      void configure(LocalContainerEntityManagerFactoryBean factoryBean);
   }
}
