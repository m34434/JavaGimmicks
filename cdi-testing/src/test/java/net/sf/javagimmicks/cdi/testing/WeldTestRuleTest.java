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
      // @Inject private SomeClass lookup
      final SomeClass lookup = _weld.lookup(SomeClass.class);

      assertNotNull(lookup);
   }

   public static class ClassToLookup
   {}
}
