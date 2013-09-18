package net.sf.javagimmicks.collections.event.cdi;

import java.util.TreeSet;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeSortedSetEventListenerTest
{
   private static CDISortedSetEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventSortedSet<String> set = new ObservableEventSortedSet<String>(new TreeSet<String>());
      CDIBridgeSortedSetEventListener.install(set);

      final String addedString = "Foo";
      set.add(addedString);

      final Object element = _event.getElement();

      Assert.assertNotNull(_event);
      Assert.assertSame(set, _event.getSource());
      Assert.assertSame(SortedSetEvent.Type.ADDED, _event.getType());
      Assert.assertNotNull(element);
      Assert.assertSame(addedString, element);
   }

   public void receiveEvent(@Observes final CDISortedSetEvent event)
   {
      _event = event;
   }
}
