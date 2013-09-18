package net.sf.javagimmicks.collections.event.cdi;

import java.util.HashSet;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIEventSetTest
{
   private static CDISetEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final CDIEventSet<String> set = new CDIEventSet<String>(new HashSet<String>());

      final String addedString = "Foo";
      set.add(addedString);

      final Object element = _event.getElement();

      Assert.assertNotNull(_event);
      Assert.assertSame(set, _event.getSource());
      Assert.assertSame(CDISetEvent.Type.ADDED, _event.getType());
      Assert.assertNotNull(element);
      Assert.assertSame(addedString, element);
   }

   public void receiveEvent(@Observes final CDISetEvent event)
   {
      _event = event;
   }
}
