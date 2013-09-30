package net.sf.javagimmicks.jpa.testing;

import javax.persistence.EntityManager;

import net.sf.javagimmicks.jpa.testing.JpaHsqlDbTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class JpaHsqlDbTestRuleTest
{
   @Rule
   public JpaHsqlDbTestRule _jpa = new JpaHsqlDbTestRule();

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
