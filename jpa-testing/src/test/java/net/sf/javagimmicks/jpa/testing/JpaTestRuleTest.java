package net.sf.javagimmicks.jpa.testing;

import java.util.Collections;
import java.util.List;

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
   public void testPersistTx()
   {
      final EntityManager em = _jpa.createEntityManager();

      Assert.assertNotNull(em);

      em.getTransaction().begin();
      Assert.assertTrue(em.getTransaction().isActive());

      em.persist(new FooEntity(1, "foo"));
      em.persist(new FooEntity(2, "foo"));
      em.persist(new FooEntity(3, "bar"));

      em.getTransaction().commit();
      Assert.assertFalse(em.getTransaction().isActive());

      em.close();
   }

   @Test
   public void testImplicitMerge()
   {
      testPersistTx();

      EntityManager em = _jpa.createEntityManager();
      em.getTransaction().begin();

      FooEntity foo1 = em.find(FooEntity.class, 1);
      Assert.assertNotNull(foo1);

      foo1.setValue("bar");

      em.getTransaction().commit();
      em.close();

      em = _jpa.createEntityManager();
      foo1 = em.find(FooEntity.class, 1);
      Assert.assertNotNull(foo1);

      Assert.assertEquals("bar", foo1.getValue());
      em.close();
   }

   @Test
   public void testExplicitMerge()
   {
      testPersistTx();

      EntityManager em = _jpa.createEntityManager();
      em.getTransaction().begin();

      em.merge(new FooEntity(1, "bar"));

      em.getTransaction().commit();
      em.close();

      em = _jpa.createEntityManager();
      final FooEntity foo1 = em.find(FooEntity.class, 1);
      Assert.assertNotNull(foo1);

      Assert.assertEquals("bar", foo1.getValue());
      em.close();
   }

   @SuppressWarnings("unchecked")
   @Test
   public void testInserted()
   {
      testPersistTx();

      final EntityManager em = _jpa.createEntityManager();

      final List<FooEntity> foos = em.createQuery("SELECT f from foo f").getResultList();

      Assert.assertEquals(3, foos.size());

      Collections.sort(foos);
      Assert.assertEquals(1, foos.get(0).getId());
      Assert.assertEquals(2, foos.get(1).getId());
      Assert.assertEquals(3, foos.get(2).getId());

      Assert.assertEquals("foo", foos.get(0).getValue());
      Assert.assertEquals("foo", foos.get(1).getValue());
      Assert.assertEquals("bar", foos.get(2).getValue());

      em.close();
   }

}
