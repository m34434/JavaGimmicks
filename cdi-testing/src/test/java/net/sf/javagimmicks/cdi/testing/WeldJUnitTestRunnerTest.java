package net.sf.javagimmicks.cdi.testing;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class WeldJUnitTestRunnerTest
{
   @Inject
   private ClassToLookup _injected;

   @Inject
   private SomeClass _some;

   @Test
   public void testDirect()
   {
      assertNotNull(_injected);
   }

   @Test
   public void testProducer()
   {
      assertNotNull(_some);
   }

   public static class ClassToLookup
   {}
}
