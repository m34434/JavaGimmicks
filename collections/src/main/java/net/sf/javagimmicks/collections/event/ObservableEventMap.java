package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.javagimmicks.collections.event.MapEvent.Type;

public class ObservableEventMap<K, V> extends AbstractEventMap<K, V>
{
   private static final long serialVersionUID = 8006998141057065129L;

   protected transient List<EventMapListener<K, V>> _listeners;

   public ObservableEventMap(final Map<K, V> decorated)
   {
      super(decorated);
   }

   public void addEventMapListener(final EventMapListener<K, V> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventMapListener<K, V>>();
      }

      _listeners.add(listener);
   }

   public void removeEventMapListener(final EventMapListener<K, V> listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   protected void fireEntryAdded(final K key, final V value)
   {
      fireEvent(new MapEventImpl(Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(final K key, final V value)
   {
      fireEvent(new MapEventImpl(Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
   {
      fireEvent(new MapEventImpl(Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(final MapEvent<K, V> event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class MapEventImpl implements MapEvent<K, V>
   {
      protected final Type _type;
      protected final K _key;
      protected final V _value;
      protected final V _newValue;

      public MapEventImpl(final Type type, final K key, final V value, final V newValue)
      {
         _type = type;
         _key = key;
         _value = value;
         _newValue = newValue;
      }

      public MapEventImpl(final Type type, final K key, final V value)
      {
         this(type, key, value, null);
      }

      @Override
      public ObservableEventMap<K, V> getSource()
      {
         return ObservableEventMap.this;
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
   }
}
