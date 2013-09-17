package net.sf.javagimmicks.cdi.injectable;

import javax.inject.Inject;


public class A
{
   @Inject
   private B _b;

   public String callB()
   {
      return _b.saySomething();
   }
}