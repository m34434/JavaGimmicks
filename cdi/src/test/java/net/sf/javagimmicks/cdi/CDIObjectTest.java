package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.A;
import net.sf.javagimmicks.cdi.injectable.B;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIObjectTest
{
   @Test
   public void test()
   {
      assertEquals(B.MESSAGE, new NonCDIGeneratedClass().callA());
   }

   private static class NonCDIGeneratedClass extends CDIObject
   {
      @Inject
      private A a;

      public String callA()
      {
         return a.callB();
      }
   }
}
