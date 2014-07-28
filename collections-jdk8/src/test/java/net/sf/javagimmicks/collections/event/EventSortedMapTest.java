package net.sf.javagimmicks.collections.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.javagimmicks.collections.event.SortedMapEvent.Type;
import net.sf.javagimmicks.event.testing.EventCollector;
import net.sf.javagimmicks.event.testing.EventCollector.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventSortedMapTest
{
   private SortedMap<String, String> _map;
   private ObservableEventSortedMap<String, String> _eventMap;
   private EventCollector<SortedMapEvent<String, String>> _listener;

   @Before
   public void setup()
   {
      // Prepare base and event map
      _map = new TreeMap<String, String>();
      _eventMap = new ObservableEventSortedMap<String, String>(_map);

      // Create mock listener and register it
      _listener = new EventCollector<SortedMapEvent<String, String>>(SortedMapEvent.class, _eventMap);
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

   @Test
   public void testBasicSortedMapStuff()
   {
      _eventMap.put("c", "3");
      _eventMap.put("e", "5");
      _eventMap.put("g", "7");
      _eventMap.put("i", "9");

      assertEquals("c", _eventMap.firstKey());
      assertEquals("i", _eventMap.lastKey());

      _listener.clear();

      testHeadMap();
      testTailMap();
      testSublMap();
   }

   private void testHeadMap()
   {
      final SortedMap<String, String> headMap = _map.headMap("h");
      final SortedMap<String, String> headEventMap = _eventMap.headMap("h");
      assertEquals(headMap, headEventMap);

      assertEquals("c", headEventMap.firstKey());
      assertEquals("g", headEventMap.lastKey());

      assertNull(headEventMap.put("a", "1"));
      assertEquals(headMap, headEventMap);

      assertEquals("3", headEventMap.put("c", "3c"));
      assertEquals(headMap, headEventMap);
      assertEquals("3c", headEventMap.get("c"));

      assertEquals("1", headEventMap.entrySet().iterator().next().setValue("1a"));
      assertEquals(headMap, headEventMap);
      assertEquals("1a", headEventMap.get("a"));

      assertEquals("3c", headEventMap.put("c", "3"));
      assertEquals(headMap, headEventMap);
      assertEquals("3", headEventMap.get("c"));

      assertEquals("1a", headEventMap.put("a", "1"));
      assertEquals(headMap, headEventMap);
      assertEquals("1", headEventMap.get("a"));

      headEventMap.remove(headEventMap.firstKey());
      assertEquals(headMap, headEventMap);

      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "a", "1"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "c", "3", "3c"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "a", "1", "1a"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "c", "3c", "3"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "a", "1a", "1"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "a", "1"));
      _listener.assertEmpty();
   }

   private void testTailMap()
   {
      final SortedMap<String, String> tailMap = _map.tailMap("d");
      final SortedMap<String, String> tailEventMap = _eventMap.tailMap("d");
      assertEquals(tailMap, tailEventMap);

      assertEquals("e", tailEventMap.firstKey());
      assertEquals("i", tailEventMap.lastKey());

      assertNull(tailEventMap.put("k", "1"));
      assertEquals(tailMap, tailEventMap);

      assertEquals("7", tailEventMap.put("g", "7g"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("7g", tailEventMap.get("g"));

      assertEquals("5", tailEventMap.entrySet().iterator().next().setValue("5e"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("5e", tailEventMap.get("e"));

      assertEquals("7g", tailEventMap.put("g", "7"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("7", tailEventMap.get("g"));

      assertEquals("5e", tailEventMap.put("e", "5"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("5", tailEventMap.get("e"));

      tailEventMap.remove(tailEventMap.lastKey());
      assertEquals(tailMap, tailEventMap);

      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "k", "1"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "g", "7", "7g"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "e", "5", "5e"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "g", "7g", "7"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "e", "5e", "5"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "k", "1"));
      _listener.assertEmpty();
   }

   private void testSublMap()
   {
      final SortedMap<String, String> subMap = _map.subMap("d", "h");
      final SortedMap<String, String> subEventMap = _eventMap.subMap("d", "h");
      assertEquals(subMap, subEventMap);

      assertEquals("e", subEventMap.firstKey());
      assertEquals("g", subEventMap.lastKey());

      assertNull(subEventMap.put("d", "4"));
      assertEquals(subMap, subEventMap);

      assertEquals("7", subEventMap.put("g", "7g"));
      assertEquals(subMap, subEventMap);
      assertEquals("7g", subEventMap.get("g"));

      assertEquals("4", subEventMap.entrySet().iterator().next().setValue("4d"));
      assertEquals(subMap, subEventMap);
      assertEquals("4d", subEventMap.get("d"));

      assertEquals("7g", subEventMap.put("g", "7"));
      assertEquals(subMap, subEventMap);
      assertEquals("7", subEventMap.get("g"));

      assertEquals("4d", subEventMap.put("d", "4"));
      assertEquals(subMap, subEventMap);
      assertEquals("5", subEventMap.get("e"));

      subEventMap.remove(subEventMap.firstKey());
      assertEquals(subMap, subEventMap);

      _listener.assertEventOccured(new MapEventValidator(Type.ADDED, "d", "4"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "g", "7", "7g"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "d", "4", "4d"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "g", "7g", "7"));
      _listener.assertEventOccured(new MapEventValidator(Type.UPDATED, "d", "4d", "4"));
      _listener.assertEventOccured(new MapEventValidator(Type.REMOVED, "d", "4"));
      _listener.assertEmpty();
   }

   private static class MapEventValidator implements Validator<SortedMapEvent<String, String>>
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
      public void validate(final SortedMapEvent<String, String> event)
      {
         assertSame("Type does not match", _type, event.getType());
         assertEquals("Key does not match", _key, event.getKey());
         assertEquals("Value does not match", _value, event.getValue());
         assertEquals("New value does not match", _newValue, event.getNewValue());
      }
   }
}
