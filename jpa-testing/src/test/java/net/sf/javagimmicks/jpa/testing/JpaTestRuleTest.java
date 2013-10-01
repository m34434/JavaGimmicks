package net.sf.javagimmicks.jpa.testing;

import javax.persistence.EntityManager;

import net.sf.javagimmicks.jpa.testing.entity.FooEntity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class JpaTestRuleTest
{
   @Rule
   public JpaTestRule _jpa = new JpaTestRule(FooEntity.class.getPackage().getName());

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
