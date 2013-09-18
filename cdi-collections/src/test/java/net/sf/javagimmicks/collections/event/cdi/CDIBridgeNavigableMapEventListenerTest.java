package net.sf.javagimmicks.collections.event.cdi;

import java.util.TreeMap;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeNavigableMapEventListenerTest
{
   private static CDINavigableMapEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventNavigableMap<Integer, String> map = new ObservableEventNavigableMap<Integer, String>(
            new TreeMap<Integer, String>());
      CDIBridgeNavigableMapEventListener.install(map);

      final Integer addedKey = 42;
      final String addedValue = "Foo";
      map.put(addedKey, addedValue);

      Assert.assertNotNull(_event);
      Assert.assertSame(map, _event.getSource());
      Assert.assertSame(NavigableMapEvent.Type.ADDED, _event.getType());
      Assert.assertSame(addedKey, _event.getKey());
      Assert.assertSame(addedValue, _event.getValue());
   }

   public void receiveEvent(@Observes final CDINavigableMapEvent event)
   {
      _event = event;
   }
}
