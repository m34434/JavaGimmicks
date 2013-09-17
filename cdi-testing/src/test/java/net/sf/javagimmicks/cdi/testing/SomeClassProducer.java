package net.sf.javagimmicks.cdi.testing;

import javax.enterprise.inject.Produces;

public class SomeClassProducer
{
   @Produces
   public SomeClass create()
   {
      return new SomeClass("fake");
   }
}
