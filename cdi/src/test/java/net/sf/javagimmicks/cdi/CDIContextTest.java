package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.javagimmicks.cdi.injectable.NamedClass;
import net.sf.javagimmicks.cdi.injectable.NiceClass;

import org.junit.Test;

public class CDIContextTest extends WeldTestHelper
{
   @Test
   public void testGetBeanManager()
   {
      assertNotNull(CDIContext.getBeanManager());
   }

   @Test
   public void testLookup()
   {
      final Object o = CDIContext.lookup(NiceClass.class);

      assertNotNull(o);
      assertTrue(o instanceof NiceClass);
   }

   @Test
   public void testLookupByName()
   {
      final Object o = CDIContext.lookup(NamedClass.NAME);

      assertNotNull(o);
      assertTrue(o instanceof NamedClass);
   }
}
