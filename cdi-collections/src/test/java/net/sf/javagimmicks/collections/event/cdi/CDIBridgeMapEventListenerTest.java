package net.sf.javagimmicks.collections.event.cdi;

import java.util.HashMap;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.MapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeMapEventListenerTest
{
   private static CDIMapEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventMap<Integer, String> map = new ObservableEventMap<Integer, String>(
            new HashMap<Integer, String>());
      CDIBridgeMapEventListener.install(map);

      final Integer addedKey = 42;
      final String addedValue = "Foo";
      map.put(addedKey, addedValue);

      Assert.assertNotNull(_event);
      Assert.assertSame(map, _event.getSource());
      Assert.assertSame(MapEvent.Type.ADDED, _event.getType());
      Assert.assertSame(addedKey, _event.getKey());
      Assert.assertSame(addedValue, _event.getValue());
   }

   public void receiveEvent(@Observes final CDIMapEvent event)
   {
      _event = event;
   }
}
