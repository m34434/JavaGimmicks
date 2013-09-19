package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

import net.sf.javagimmicks.collections.event.NavigableMapEvent.Type;

public class ObservableEventNavigableMap<K, V> extends AbstractEventNavigableMap<K, V> implements
      Observable<NavigableMapEvent<K, V>, EventNavigableMapListener<K, V>>
{
   private static final long serialVersionUID = -4936595637793434597L;

   protected transient List<EventNavigableMapListener<K, V>> _listeners;

   public ObservableEventNavigableMap(final NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventNavigableMapListener<K, V> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventNavigableMapListener<K, V>>();
      }

      _listeners.add(listener);
   }

   @Override
   public void removeEventListener(final EventNavigableMapListener<K, V> listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   public NavigableSet<K> descendingKeySet()
   {
      // TODO Auto-generated method stub
      return super.descendingKeySet();
   }

   @Override
   public NavigableMap<K, V> descendingMap()
   {
      // TODO Auto-generated method stub
      return super.descendingMap();
   }

   @Override
   public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive)
   {
      // TODO Auto-generated method stub
      return super.headMap(toKey, inclusive);
   }

   @Override
   public NavigableSet<K> navigableKeySet()
   {
      // TODO Auto-generated method stub
      return super.navigableKeySet();
   }

   @Override
   public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
         final boolean toInclusive)
   {
      // TODO Auto-generated method stub
      return super.subMap(fromKey, fromInclusive, toKey, toInclusive);
   }

   @Override
   public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive)
   {
      // TODO Auto-generated method stub
      return super.tailMap(fromKey, inclusive);
   }

   @Override
   public SortedMap<K, V> headMap(final K toKey)
   {
      // TODO Auto-generated method stub
      return super.headMap(toKey);
   }

   @Override
   public SortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      // TODO Auto-generated method stub
      return super.subMap(fromKey, toKey);
   }

   @Override
   public SortedMap<K, V> tailMap(final K fromKey)
   {
      // TODO Auto-generated method stub
      return super.tailMap(fromKey);
   }

   @Override
   protected void fireEntryAdded(final K key, final V value)
   {
      fireEvent(new NavigableMapEventImpl(Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(final K key, final V value)
   {
      fireEvent(new NavigableMapEventImpl(Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
   {
      fireEvent(new NavigableMapEventImpl(Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(final NavigableMapEvent<K, V> event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventNavigableMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class NavigableMapEventImpl implements NavigableMapEvent<K, V>
   {
      protected final Type _type;
      protected final K _key;
      protected final V _value;
      protected final V _newValue;

      public NavigableMapEventImpl(final Type type, final K key, final V value, final V newValue)
      {
         _type = type;
         _key = key;
         _value = value;
         _newValue = newValue;
      }

      public NavigableMapEventImpl(final Type type, final K key, final V value)
      {
         this(type, key, value, null);
      }

      @Override
      public ObservableEventNavigableMap<K, V> getSource()
      {
         return ObservableEventNavigableMap.this;
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

   protected static class ObservableEventSubNavigableMap<K, V> extends ObservableEventNavigableMap<K, V>
   {
      private static final long serialVersionUID = -863678987488740776L;

      protected final ObservableEventNavigableMap<K, V> _parent;

      protected ObservableEventSubNavigableMap(final ObservableEventNavigableMap<K, V> parent,
            final NavigableMap<K, V> decorated)
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
