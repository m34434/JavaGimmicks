package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertNotNull;
import net.sf.javagimmicks.cdi.injectable.B;

import org.junit.Test;

public class InjectionFactoryTest extends WeldTestHelper
{
   @Test
   public void test()
   {
      // The factory is instantiated via normal constructor but the instances it
      // creates are created from CDI
      final B b = new InjectionFactory<B>(B.class).create();
      assertNotNull(b);
   }
}
