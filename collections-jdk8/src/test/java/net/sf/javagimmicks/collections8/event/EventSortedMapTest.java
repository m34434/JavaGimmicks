package net.sf.javagimmicks.collections8.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.javagimmicks.collections8.event.SortedMapEvent.Type;
import net.sf.javagimmicks.event.EventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class EventSortedMapTest
{
   private SortedMap<String, String> _map;
   private ObservableEventSortedMap<String, String> _eventMap;
   private EventListener<SortedMapEvent<String, String>> _listener;

   @SuppressWarnings("unchecked")
   @Before
   public void setup()
   {
      // Prepare base and event map
      _map = new TreeMap<String, String>();
      _eventMap = new ObservableEventSortedMap<String, String>(_map);

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

   @SuppressWarnings("unchecked")
   @Test
   public void testBasicSortedMapStuff()
   {
      _eventMap.put("c", "3");
      _eventMap.put("e", "5");
      _eventMap.put("g", "7");
      _eventMap.put("i", "9");

      assertEquals("c", _eventMap.firstKey());
      assertEquals("i", _eventMap.lastKey());

      Mockito.reset(_listener);

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
      verifyEvent(Type.ADDED, "a", "1");

      assertEquals("3", headEventMap.put("c", "3c"));
      assertEquals(headMap, headEventMap);
      assertEquals("3c", headEventMap.get("c"));
      verifyEvent(Type.UPDATED, "c", "3", "3c");

      assertEquals("1", headEventMap.entrySet().iterator().next().setValue("1a"));
      assertEquals(headMap, headEventMap);
      assertEquals("1a", headEventMap.get("a"));
      verifyEvent(Type.UPDATED, "a", "1", "1a");

      assertEquals("3c", headEventMap.put("c", "3"));
      assertEquals(headMap, headEventMap);
      assertEquals("3", headEventMap.get("c"));
      verifyEvent(Type.UPDATED, "c", "3c", "3");

      assertEquals("1a", headEventMap.put("a", "1"));
      assertEquals(headMap, headEventMap);
      assertEquals("1", headEventMap.get("a"));
      verifyEvent(Type.UPDATED, "a", "1a", "1");

      headEventMap.remove(headEventMap.firstKey());
      assertEquals(headMap, headEventMap);
      verifyEvent(Type.REMOVED, "a", "1");

      Mockito.verifyNoMoreInteractions(_listener);
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
      verifyEvent(Type.ADDED, "k", "1");

      assertEquals("7", tailEventMap.put("g", "7g"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("7g", tailEventMap.get("g"));
      verifyEvent(Type.UPDATED, "g", "7", "7g");

      assertEquals("5", tailEventMap.entrySet().iterator().next().setValue("5e"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("5e", tailEventMap.get("e"));
      verifyEvent(Type.UPDATED, "e", "5", "5e");

      assertEquals("7g", tailEventMap.put("g", "7"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("7", tailEventMap.get("g"));
      verifyEvent(Type.UPDATED, "g", "7g", "7");

      assertEquals("5e", tailEventMap.put("e", "5"));
      assertEquals(tailMap, tailEventMap);
      assertEquals("5", tailEventMap.get("e"));
      verifyEvent(Type.UPDATED, "e", "5e", "5");

      tailEventMap.remove(tailEventMap.lastKey());
      assertEquals(tailMap, tailEventMap);
      verifyEvent(Type.REMOVED, "k", "1");

      Mockito.verifyNoMoreInteractions(_listener);
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
      verifyEvent(Type.ADDED, "d", "4");

      assertEquals("7", subEventMap.put("g", "7g"));
      assertEquals(subMap, subEventMap);
      assertEquals("7g", subEventMap.get("g"));
      verifyEvent(Type.UPDATED, "g", "7", "7g");

      assertEquals("4", subEventMap.entrySet().iterator().next().setValue("4d"));
      assertEquals(subMap, subEventMap);
      assertEquals("4d", subEventMap.get("d"));
      verifyEvent(Type.UPDATED, "d", "4", "4d");

      assertEquals("7g", subEventMap.put("g", "7"));
      assertEquals(subMap, subEventMap);
      assertEquals("7", subEventMap.get("g"));
      verifyEvent(Type.UPDATED, "g", "7g", "7");

      assertEquals("4d", subEventMap.put("d", "4"));
      assertEquals(subMap, subEventMap);
      assertEquals("5", subEventMap.get("e"));
      verifyEvent(Type.UPDATED, "d", "4d", "4");

      subEventMap.remove(subEventMap.firstKey());
      assertEquals(subMap, subEventMap);
      verifyEvent(Type.REMOVED, "d", "4");

      Mockito.verifyNoMoreInteractions(_listener);
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private void verifyEvent(final Type type, final String key, final String value)
   {
      final ArgumentCaptor<SortedMapEvent> arg = ArgumentCaptor.forClass(
            SortedMapEvent.class);
      Mockito.verify(_listener, Mockito.atLeastOnce()).eventOccured(arg.capture());
      assertEquals(type, arg.getValue().getType());
      assertEquals(key, arg.getValue().getKey());
      assertEquals(value, arg.getValue().getValue());
      assertNull(arg.getValue().getNewValue());
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private void verifyEvent(final Type type, final String key, final String oldValue, final String newValue)
   {
      final ArgumentCaptor<SortedMapEvent> arg = ArgumentCaptor.forClass(SortedMapEvent.class);
      Mockito.verify(_listener, Mockito.atLeastOnce()).eventOccured(arg.capture());
      assertEquals(type, arg.getValue().getType());
      assertEquals(key, arg.getValue().getKey());
      assertEquals(oldValue, arg.getValue().getValue());
      assertEquals(newValue, arg.getValue().getNewValue());
   }

}
