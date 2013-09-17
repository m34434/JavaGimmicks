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

   @Test
   public void test()
   {
      assertNotNull(_injected);
   }

   public static class ClassToLookup
   {}
}
