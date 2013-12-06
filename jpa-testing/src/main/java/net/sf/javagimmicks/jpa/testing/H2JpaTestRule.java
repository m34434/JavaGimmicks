package net.sf.javagimmicks.jpa.testing;

import java.io.File;

import net.sf.javagimmicks.sql.testing.AbstractDbTestRule;
import net.sf.javagimmicks.sql.testing.AbstractDbTestRule.DataSourceConfigurator;
import net.sf.javagimmicks.sql.testing.H2DbTestRule;
import net.sf.javagimmicks.sql.testing.HsqlDbTestRule;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.rules.TestRule;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;

/**
 * A JUnit {@link TestRule} that creates a temporary file-based H2 database
 * (using {@link HsqlDbTestRule}) during test execution and wraps
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
 * public class H2JpaTestRule
 * {
 * 
 *    &#064;Rule
 *    public H2JpaTestRule _jpa = new H2JpaTestRule(FooEntity.class.getPackage().getName());
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
public class H2JpaTestRule extends AbstractJpaTestRule
{
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
   public H2JpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final File dbFolder,
         final String... entityPackages)
   {
      super(configurator, dbFolder, entityPackages);
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
   public H2JpaTestRule(final File dbFolder, final String... entityPackages)
   {
      super(dbFolder, entityPackages);
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
   public H2JpaTestRule(final EntityManagerFactoryConfigurator configurator,
         final String... entityPackages)
   {
      super(configurator, entityPackages);
   }

   /**
    * Creates a new instance with no special configuration.
    * 
    * @param entityPackages
    *           a list of names of packages to scan for entity classes
    * @throws IllegalArgumentException
    *            if the list of package names is empty
    */
   public H2JpaTestRule(final String... entityPackages)
   {
      super(entityPackages);
   }

   @Override
   protected AbstractDbTestRule createDb(final DataSourceConfigurator configurator, final File dbFolder)
   {
      return new H2DbTestRule(configurator, dbFolder);
   }

   @Override
   protected Database getDatabaseType()
   {
      return Database.H2;
   }
}
