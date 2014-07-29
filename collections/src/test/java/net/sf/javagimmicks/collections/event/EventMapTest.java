package net.sf.javagimmicks.collections.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.collections.event.MapEvent.Type;
import net.sf.javagimmicks.event.EventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class EventMapTest
{
   private Map<String, String> _map;
   private ObservableEventMap<String, String> _eventMap;
   private EventListener<MapEvent<String, String>> _listener;

   @SuppressWarnings("unchecked")
   @Before
   public void setup()
   {
      // Prepare base and event map
      _map = new TreeMap<String, String>();
      _eventMap = new ObservableEventMap<String, String>(_map);

      // Create mock listener and register it
      _listener = Mockito.mock(EventListener.class);
      _eventMap.addEventListener(_listener);
   }

   @After
   public void tearDown()
   {
      _eventMap.removeEventListener(_listener);

      _listener = null;
      _eventMap = null;

      _map.clear();
      _map = null;
   }

   @Test
   public void testBasicMapStuff()
   {
      // Do basic operations
      assertNull(_eventMap.put("1", "A"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.ADDED, "1", "A");

      assertNull(_eventMap.put("2", "B"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.ADDED, "2", "B");

      assertNull(_eventMap.put("3", "C"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.ADDED, "3", "C");

      assertEquals("A", _eventMap.entrySet().iterator().next().setValue("B"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.UPDATED, "1", "A", "B");

      assertEquals("B", _eventMap.put("2", "A"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.UPDATED, "2", "B", "A");

      assertTrue(_eventMap.keySet().remove("1"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.REMOVED, "1", "B");

      assertEquals("A", _eventMap.remove("2"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.REMOVED, "2", "A");

      assertTrue(_eventMap.values().remove("C"));
      assertEquals(_eventMap, _map);
      verifyEvent(Type.REMOVED, "3", "C");

      Mockito.verifyNoMoreInteractions(_listener);
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private void verifyEvent(final Type type, final String key, final String value)
   {
      final ArgumentCaptor<MapEvent> arg = ArgumentCaptor.forClass(
            MapEvent.class);
      Mockito.verify(_listener, Mockito.atLeastOnce()).eventOccured(arg.capture());
      assertEquals(type, arg.getValue().getType());
      assertEquals(key, arg.getValue().getKey());
      assertEquals(value, arg.getValue().getValue());
      assertNull(arg.getValue().getNewValue());
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private void verifyEvent(final Type type, final String key, final String oldValue, final String newValue)
   {
      final ArgumentCaptor<MapEvent> arg = ArgumentCaptor.forClass(MapEvent.class);
      Mockito.verify(_listener, Mockito.atLeastOnce()).eventOccured(arg.capture());
      assertEquals(type, arg.getValue().getType());
      assertEquals(key, arg.getValue().getKey());
      assertEquals(oldValue, arg.getValue().getValue());
      assertEquals(newValue, arg.getValue().getNewValue());
   }

}
