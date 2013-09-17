package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Produces;

import net.sf.javagimmicks.cdi.injectable.NiceClass;

import org.junit.Test;

public class FactoryProducerTest extends WeldTestHelper
{
   private static final String DEFAULT_NICENESS = "Nice!";

   @Test
   public void test() throws Exception
   {
      // @Inject private NiceClass niceClassInstance;
      final NiceClass niceClassInstance = CDIContext.lookup(NiceClass.class);

      assertNotNull(niceClassInstance);
      assertEquals(DEFAULT_NICENESS, niceClassInstance.getNiceness());
   }

   @Produces
   public static NiceClass createNice()
   {
      return new NiceClass(DEFAULT_NICENESS);
   }
}
