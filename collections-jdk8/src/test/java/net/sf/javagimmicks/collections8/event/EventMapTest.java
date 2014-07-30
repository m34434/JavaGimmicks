package net.sf.javagimmicks.collections8.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.collections8.event.MapEvent;
import net.sf.javagimmicks.collections8.event.ObservableEventMap;
import net.sf.javagimmicks.collections8.event.MapEvent.Type;
import net.sf.javagimmicks.event.testing.EventCollector;
import net.sf.javagimmicks.event.testing.EventCollector.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventMapTest
{
   private Map<String, String> _map;
   private ObservableEventMap<String, String> _eventMap;
   private EventCollector<MapEvent<String, String>> _listener;

   @Before
   public void setup()
   {
      // Prepare base and event map
      _map = new TreeMap<String, String>();
      _eventMap = new ObservableEventMap<String, String>(_map);

      // Create mock listener and register it
      _listener = new EventCollector<MapEvent<String, String>>(MapEvent.class, _eventMap);
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

      assertNull(_eventMap.put("2", "B"));
      assertEquals(_eventMap, _map);

      assertNull(_eventMap.put("3", "C"));
      assertEquals(_eventMap, _map);

      assertEquals("A", _eventMap.entrySet().iterator().next().setValue("B"));
      assertEquals(_eventMap, _map);

      assertEquals("B", _eventMap.put("2", "A"));
      assertEquals(_eventMap, _map);

      assertTrue(_eventMap.keySet().remove("1"));
      assertEquals(_eventMap, _map);

      assertEquals("A", _eventMap.remove("2"));
      assertEquals(_eventMap, _map);

      assertTrue(_eventMap.values().remove("C"));
      assertEquals(_eventMap, _map);

      // Validate events
      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "1", "A"));
      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "2", "B"));
      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "3", "C"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "1", "A", "B"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "2", "B", "A"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "1", "B"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "2", "A"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "3", "C"));
      _listener.assertEmpty();
   }

   private static class MapEventValidator implements Validator<MapEvent<String, String>>
   {
      private final Type _type;
      private final String _key;
      private final String _value;
      private final String _newValue;

      public MapEventValidator(final Type type, final String key, final String value, final String newValue)
      {
         this._type = type;
         this._key = key;
         this._value = value;
         this._newValue = newValue;
      }

      public MapEventValidator(final Type type, final String key, final String value)
      {
         this(type, key, value, null);
      }

      @Override
      public void validate(final MapEvent<String, String> event)
      {
         assertSame("Type does not match", _type, event.getType());
         assertEquals("Key does not match", _key, event.getKey());
         assertEquals("Value does not match", _value, event.getValue());
         assertEquals("New value does not match", _newValue, event.getNewValue());
      }
   }
}
