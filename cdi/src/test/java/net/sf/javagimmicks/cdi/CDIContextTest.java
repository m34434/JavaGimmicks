package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

   private static class NonCDIGeneratedClass
   {
      // @Inject private A a;
      private final A a = CDIContext.lookup(A.class);

      public String callA()
      {
         return a.callB();
      }
   }
}
