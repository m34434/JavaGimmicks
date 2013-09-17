package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;



import net.sf.javagimmicks.cdi.injectable.A;

import org.junit.Test;

public class BootstrappingTest extends WeldTestHelper
{
   @Test
   public void testLookup()
   {
      // @Inject private A lookup
      final A lookup = lookup(A.class);

      assertEquals("Something", lookup.callB());
   }
}
