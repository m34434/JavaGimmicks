package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertNotNull;
import net.sf.javagimmicks.cdi.injectable.B;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class InjectionFactoryTest
{
   @Test
   public void testCreate()
   {
      // The factory is instantiated via normal constructor but the instances it
      // creates are created from CDI
      final B b = new InjectionFactory<B>(B.class).create();
      assertNotNull(b);
   }
}
