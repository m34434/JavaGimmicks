package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import net.sf.javagimmicks.collections.event.SortedMapEvent.Type;

public class ObservableEventSortedMap<K, V> extends AbstractEventSortedMap<K, V>
{
   private static final long serialVersionUID = -4377528012758388630L;

   protected transient List<EventSortedMapListener<K, V>> _listeners;

   public ObservableEventSortedMap(final SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   public void addEventSortedMapListener(final EventSortedMapListener<K, V> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventSortedMapListener<K, V>>();
      }

      _listeners.add(listener);
   }

   public void removeEventSortedMapListener(final EventSortedMapListener<K, V> listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   public ObservableEventSortedMap<K, V> headMap(final K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().headMap(toKey));
   }

   @Override
   public ObservableEventSortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().subMap(fromKey, toKey));
   }

   @Override
   public ObservableEventSortedMap<K, V> tailMap(final K fromKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().tailMap(fromKey));
   }

   @Override
   protected void fireEntryAdded(final K key, final V value)
   {
      fireEvent(new SortedMapEventImpl(Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(final K key, final V value)
   {
      fireEvent(new SortedMapEventImpl(Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
   {
      fireEvent(new SortedMapEventImpl(Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(final SortedMapEvent<K, V> event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventSortedMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class SortedMapEventImpl implements SortedMapEvent<K, V>
   {
      protected final Type _type;
      protected final K _key;
      protected final V _value;
      protected final V _newValue;

      public SortedMapEventImpl(final Type type, final K key, final V value, final V newValue)
      {
         _type = type;
         _key = key;
         _value = value;
         _newValue = newValue;
      }

      public SortedMapEventImpl(final Type type, final K key, final V value)
      {
         this(type, key, value, null);
      }

      @Override
      public ObservableEventSortedMap<K, V> getSource()
      {
         return ObservableEventSortedMap.this;
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

   protected static class ObservableEventSubSortedMap<K, V> extends ObservableEventSortedMap<K, V>
   {
      private static final long serialVersionUID = 8431554623263356463L;

      protected final ObservableEventSortedMap<K, V> _parent;

      protected ObservableEventSubSortedMap(final ObservableEventSortedMap<K, V> parent, final SortedMap<K, V> decorated)
      {
         super(decorated);

         _parent = parent;
      }

      @Override
      protected void fireEntryAdded(final K key, final V value)
      {
         super.fireEntryAdded(key, value);
         _parent.fireEntryAdded(key, value);
      }

      @Override
      protected void fireEntryRemoved(final K key, final V value)
      {
         super.fireEntryRemoved(key, value);
         _parent.fireEntryRemoved(key, value);
      }

      @Override
      protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
      {
         super.fireEntryUpdated(key, oldValue, newValue);
         _parent.fireEntryUpdated(key, oldValue, newValue);
      }
   }
}
