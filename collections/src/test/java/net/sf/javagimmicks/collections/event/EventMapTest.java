package net.sf.javagimmicks.collections.event;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.collections.event.MapEvent.Type;
import net.sf.javagimmicks.lang.LangUtils;

import org.junit.Test;

public class EventMapTest
{
   @SuppressWarnings("unchecked")
   @Test
   public void testPutAndRemove()
   {
      // Prepare base and event map
      final Map<String, String> map = new TreeMap<String, String>();
      final ObservableEventMap<String, String> eventMap = new ObservableEventMap<String, String>(map);

      // Create mock listener and register it
      final EventMapListener<String, String> mockListener = createStrictMock(EventMapListener.class);
      eventMap.addEventMapListener(mockListener);

      // Record expected listener calls
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.ADDED, "1", "A"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.ADDED, "2", "B"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.ADDED, "3", "C"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.UPDATED, "1", "A", "B"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.UPDATED, "2", "B", "A"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.REMOVED, "1", "B"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.REMOVED, "2", "A"));
      mockListener.eventOccured(new MapEventImpl<String, String>(eventMap, Type.REMOVED, "3", "C"));

      // Start replay
      replay(mockListener);

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

      // Finally verify the mock
      verify(mockListener);
   }

   public static class MapEventImpl<K, V> implements MapEvent<K, V>
   {
      protected final ObservableEventMap<K, V> _source;

      protected final Type _type;
      protected final K _key;
      protected final V _value;
      protected final V _newValue;

      public MapEventImpl(final ObservableEventMap<K, V> source, final Type type, final K key, final V value,
            final V newValue)
      {
         _source = source;
         _type = type;
         _key = key;
         _value = value;
         _newValue = newValue;
      }

      public MapEventImpl(final ObservableEventMap<K, V> source, final Type type, final K key, final V value)
      {
         this(source, type, key, value, null);
      }

      @Override
      public ObservableEventMap<K, V> getSource()
      {
         return _source;
      }

      @Override
      public Type getType()
      {
         return _type;
      }

      @Override
      public K getKey()
      {
         return _key;
      }

      @Override
      public V getValue()
      {
         return _value;
      }

      @Override
      public V getNewValue()
      {
         return _newValue;
      }

      @Override
      public boolean equals(final Object o)
      {
         if (!(o instanceof MapEventImpl<?, ?>))
         {
            return false;
         }

         final MapEventImpl<?, ?> other = (MapEventImpl<?, ?>) o;
         return _source == other._source &&
               _type == other._type &&
               LangUtils.equalsNullSafe(_key, other._key) &&
               LangUtils.equalsNullSafe(_value, other._value) &&
               LangUtils.equalsNullSafe(_newValue, other._newValue);
      }
   }
}
