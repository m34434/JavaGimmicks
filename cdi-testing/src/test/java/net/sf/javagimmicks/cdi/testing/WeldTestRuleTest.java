package net.sf.javagimmicks.cdi.testing;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;

public class WeldTestRuleTest
{
   @Rule
   public WeldTestRule _weld = new WeldTestRule();

   @Test
   public void testDirect()
   {
      // @Inject private ClassToLookup lookup
      final ClassToLookup lookup = _weld.lookup(ClassToLookup.class);

      assertNotNull(lookup);
   }

   @Test
   public void testProducer()
   {
      // @Inject private SomeClass some
      final SomeClass some = _weld.lookup(SomeClass.class);

      assertNotNull(some);
   }

   public static class ClassToLookup
   {}
}
