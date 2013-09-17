package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.sf.javagimmicks.cdi.injectable.NiceClass;
import net.sf.javagimmicks.cdi.injectable.NiceClassProducer;

import org.junit.Test;

public class FactoryProducerTest extends WeldTestHelper
{
   @Test
   public void doTest() throws Exception
   {
      // @Inject private NiceClass niceClassInstance;
      final NiceClass niceClassInstance = lookup(NiceClass.class);

      assertNotNull(niceClassInstance);
      assertEquals(NiceClassProducer.DEFAULT_NICENESS, niceClassInstance.getNiceness());
   }

}
