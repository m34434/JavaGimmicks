package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.A;
import net.sf.javagimmicks.cdi.injectable.B;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIAwareTest
{
   @Test
   public void test()
   {
      assertEquals(B.MESSAGE, new NonCDIGeneratedClass().callA());
   }

   @Test
   public void testIlluminated()
   {
      assertEquals(B.MESSAGE, new AnotherNonCDIGeneratedClass().callA());
   }

   private static class NonCDIGeneratedClass extends CDIAware
   {
      // @Inject private A a;
      private final A a = lookup(A.class);

      public String callA()
      {
         return a.callB();
      }
   }

   private static class AnotherNonCDIGeneratedClass extends CDIAware
   {
      @Inject
      private A a;

      public AnotherNonCDIGeneratedClass()
      {
         // This performs injection
         super(true);
      }

      public String callA()
      {
         return a.callB();
      }
   }
}
