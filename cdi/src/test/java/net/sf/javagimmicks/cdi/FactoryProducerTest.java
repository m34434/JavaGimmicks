package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.NiceClass;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.lang.Factory;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class FactoryProducerTest
{
   private static final String DEFAULT_NICENESS = "Nice!";

   @Inject
   private NiceClass _nice;

   @Test
   public void test() throws Exception
   {
      assertNotNull(_nice);
      assertEquals(DEFAULT_NICENESS, _nice.getNiceness());
   }

   public static class NiceClassFactory implements Factory<NiceClass>
   {
      @Override
      @Produces
      public NiceClass create()
      {
         return new NiceClass(DEFAULT_NICENESS);
      }
   }
}
