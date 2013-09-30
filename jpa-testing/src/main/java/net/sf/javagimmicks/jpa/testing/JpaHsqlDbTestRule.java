package net.sf.javagimmicks.jpa.testing;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.javagimmicks.sql.testing.HsqlDbTestRule;

import org.junit.rules.ExternalResource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class JpaHsqlDbTestRule extends ExternalResource
{
   private final HsqlDbTestRule _db = new HsqlDbTestRule();

   private LocalContainerEntityManagerFactoryBean _localEmf;

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
      configureEntityManagerFactoryBean(_localEmf);

      _localEmf.setDataSource(_db.getDataSource());
      _localEmf.setJpaVendorAdapter(createVendorAdapter());
      _localEmf.setPackagesToScan("net.sf.javagimmicks.jpa.testing");

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

   protected void configureEntityManagerFactoryBean(final LocalContainerEntityManagerFactoryBean factoryBean)
   {}

   protected JpaVendorAdapter createVendorAdapter()
   {
      final HibernateJpaVendorAdapter hibernate = new HibernateJpaVendorAdapter();
      hibernate.setDatabase(Database.HSQL);
      hibernate.setGenerateDdl(true);
      hibernate.setShowSql(false);

      return hibernate;
   }
}
