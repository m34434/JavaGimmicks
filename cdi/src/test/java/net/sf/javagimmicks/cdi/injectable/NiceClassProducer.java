package net.sf.javagimmicks.cdi.injectable;

import javax.enterprise.inject.Produces;

import net.sf.javagimmicks.lang.Factory;

public class NiceClassProducer implements Factory<NiceClass>
{
   public static final String DEFAULT_NICENESS = "Nice!";

   @Override
   @Produces
   public NiceClass create()
   {
      return new NiceClass(DEFAULT_NICENESS);
   }
}