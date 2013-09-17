package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertEquals;
import net.sf.javagimmicks.cdi.injectable.A;
import net.sf.javagimmicks.cdi.injectable.B;

import org.junit.Test;

public class WeldTestHelperTest extends WeldTestHelper
{
   @Test
   public void testWeldTestHelper()
   {
      // @Inject private A lookup
      final A lookup = lookup(A.class);

      assertEquals(B.MESSAGE, lookup.callB());
   }
}
