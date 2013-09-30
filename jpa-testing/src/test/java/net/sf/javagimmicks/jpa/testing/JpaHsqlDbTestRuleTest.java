package net.sf.javagimmicks.jpa.testing;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class JpaHsqlDbTestRuleTest
{
   @Rule
   public JpaHsqlDbTestRule _jpa = new JpaHsqlDbTestRule(null, null, null, getClass().getPackage().getName());

   @Test
   public void test()
   {
      final EntityManager em = _jpa.createEntityManager();

      Assert.assertNotNull(em);

      final FooEntity foo = new FooEntity();
      foo.setId(1);
      foo.setValue("foo");

      em.persist(foo);
   }
}
