package net.sf.javagimmicks.collections.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.collections.event.EventCollector.Validator;
import net.sf.javagimmicks.collections.event.MapEvent.Type;

import org.junit.Assert;
import org.junit.Test;

public class EventMapTest
{
   @Test
   public void testPutAndRemove()
   {
      // Prepare base and event map
      final Map<String, String> map = new TreeMap<String, String>();
      final ObservableEventMap<String, String> eventMap = new ObservableEventMap<String, String>(map);

      // Create mock listener and register it
      final EventCollector<MapEvent<String, String>> mockListener =
            new EventCollector<MapEvent<String, String>>(MapEvent.class, eventMap);
      eventMap.addEventListener(mockListener);

      // Do basic operations
      assertNull(eventMap.put("1", "A"));
      assertEquals(eventMap, map);

      assertNull(eventMap.put("2", "B"));
      assertEquals(eventMap, map);

      assertNull(eventMap.put("3", "C"));
      assertEquals(eventMap, map);

      assertEquals("A", eventMap.entrySet().iterator().next().setValue("B"));
      assertEquals(eventMap, map);

      assertEquals("B", eventMap.put("2", "A"));
      assertEquals(eventMap, map);

      assertTrue(eventMap.keySet().remove("1"));
      assertEquals(eventMap, map);

      assertEquals("A", eventMap.remove("2"));
      assertEquals(eventMap, map);

      assertTrue(eventMap.values().remove("C"));
      assertEquals(eventMap, map);

      // Validate events
      mockListener.assertEventOccured(new MapEventValidator(Type.ADDED, "1", "A"));
      mockListener.assertEventOccured(new MapEventValidator(Type.ADDED, "2", "B"));
      mockListener.assertEventOccured(new MapEventValidator(Type.ADDED, "3", "C"));
      mockListener.assertEventOccured(new MapEventValidator(Type.UPDATED, "1", "A", "B"));
      mockListener.assertEventOccured(new MapEventValidator(Type.UPDATED, "2", "B", "A"));
      mockListener.assertEventOccured(new MapEventValidator(Type.REMOVED, "1", "B"));
      mockListener.assertEventOccured(new MapEventValidator(Type.REMOVED, "2", "A"));
      mockListener.assertEventOccured(new MapEventValidator(Type.REMOVED, "3", "C"));
      mockListener.assertEmpty();
   }

   public static class MapEventValidator implements Validator<MapEvent<String, String>>
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
         Assert.assertSame("Type does not match", _type, event.getType());
         Assert.assertEquals("Key does not match", _key, event.getKey());
         Assert.assertEquals("Value does not match", _value, event.getValue());
         Assert.assertEquals("New value does not match", _newValue, event.getNewValue());
      }
   }
}
