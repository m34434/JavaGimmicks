package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import net.sf.javagimmicks.cdi.injectable.NiceClass;
import net.sf.javagimmicks.lang.Factory;

import org.junit.Test;

public class FactoryProducerTest extends WeldTestHelper
{
   @Test
   public void doTest() throws Exception
   {
      // @Inject private NiceClass niceClassInstance;
      final NiceClass niceClassInstance = CDIContext.lookup(NiceClass.class);

      assertNotNull(niceClassInstance);
      assertEquals(NiceClassFactory.DEFAULT_NICENESS, niceClassInstance.getNiceness());
   }

   public static class NiceClassFactory implements Factory<NiceClass>
   {
      public static final String DEFAULT_NICENESS = "Nice!";

      @Override
      @Produces
      @Default
      public NiceClass create()
      {
         return new NiceClass(DEFAULT_NICENESS);
      }
   }
}
