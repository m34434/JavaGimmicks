package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.A;
import net.sf.javagimmicks.cdi.injectable.B;
import net.sf.javagimmicks.cdi.injectable.NamedClass;
import net.sf.javagimmicks.cdi.injectable.NiceClass;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIContextTest
{
   @Test
   public void testGetBeanManager()
   {
      assertNotNull(CDIContext.getBeanManager());
   }

   @Test
   public void testLookup()
   {
      final NiceClass o = CDIContext.lookup(NiceClass.class);

      assertNotNull(o);
      assertTrue(o instanceof NiceClass);
   }

   @Test
   public void testLookupByName()
   {
      final NamedClass o = CDIContext.lookup(NamedClass.NAME);

      assertNotNull(o);
      assertTrue(o instanceof NamedClass);
   }

   @Test
   public void testWithinClass()
   {
      assertEquals(B.MESSAGE, new NonCDIGeneratedClass().callA());
   }

   @Test
   public void testInitBean()
   {
      final AnotherNonCDIGeneratedClass o = new AnotherNonCDIGeneratedClass();
      assertNull(o.getA());
      assertFalse(o.isPostConstructed());

      CDIContext.initBean(o);
      assertNotNull(o.getA());
      assertTrue(o.isPostConstructed());
   }

   private static class NonCDIGeneratedClass
   {
      // @Inject private A a;
      private final A a = CDIContext.lookup(A.class);

      public String callA()
      {
         return a.callB();
      }
   }

   private static class AnotherNonCDIGeneratedClass
   {
      @Inject
      private A _a;

      private boolean _postConstructed = false;

      @PostConstruct
      public void init()
      {
         _postConstructed = true;
      }

      public A getA()
      {
         return _a;
      }

      public boolean isPostConstructed()
      {
         return _postConstructed;
      }
   }
}
