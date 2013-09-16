package net.sf.javagimmicks.cdi;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

public class Bootstrapping extends WeldTestHelper
{
   @Test
   public void test()
   {
      Assert.assertEquals("Something", lookup(A.class).callB());
   }

   public static class A
   {
      @Inject
      private B _b;

      public String callB()
      {
         return _b.saySomething();
      }
   }

   public static class B
   {
      public String saySomething()
      {
         return "Something";
      }
   }
}
