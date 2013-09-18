package net.sf.javagimmicks.collections.event.cdi;

import java.util.HashSet;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.ObservableEventSet;
import net.sf.javagimmicks.collections.event.SetEvent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeSetEventListenerTest
{
   private static CDISetEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventSet<String> set = new ObservableEventSet<String>(new HashSet<String>());
      CDIBridgeSetEventListener.install(set);

      final String addedString = "Foo";
      set.add(addedString);

      final Object element = _event.getElement();

      Assert.assertNotNull(_event);
      Assert.assertSame(set, _event.getSource());
      Assert.assertSame(SetEvent.Type.ADDED, _event.getType());
      Assert.assertNotNull(element);
      Assert.assertSame(addedString, element);
   }

   public void receiveEvent(@Observes final CDISetEvent event)
   {
      _event = event;
   }
}
