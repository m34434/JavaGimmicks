package net.sf.javagimmicks.collections.event.cdi;

import java.util.TreeSet;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeNavigableSetEventListenerTest
{
   private static CDINavigableSetEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventNavigableSet<String> set = new ObservableEventNavigableSet<String>(new TreeSet<String>());
      CDIBridgeNavigableSetEventListener.install(set);

      final String addedString = "Foo";
      set.add(addedString);

      final Object element = _event.getElement();

      Assert.assertNotNull(_event);
      Assert.assertSame(set, _event.getSource());
      Assert.assertSame(NavigableSetEvent.Type.ADDED, _event.getType());
      Assert.assertNotNull(element);
      Assert.assertSame(addedString, element);
   }

   public void receiveEvent(@Observes final CDINavigableSetEvent event)
   {
      _event = event;
   }
}
