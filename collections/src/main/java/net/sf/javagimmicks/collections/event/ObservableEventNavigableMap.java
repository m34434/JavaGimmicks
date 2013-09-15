package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

import net.sf.javagimmicks.collections.event.NavigableMapEvent.Type;

public class ObservableEventNavigableMap<K, V> extends AbstractEventNavigableMap<K, V>
{
   private static final long serialVersionUID = -4936595637793434597L;

   protected transient List<EventNavigableMapListener<K, V>> _listeners;
   
   public ObservableEventNavigableMap(NavigableMap<K, V> decorated)
   {
      super(decorated);
   }
   
   public void addEventNavigableMapListener(EventNavigableMapListener<K, V> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventNavigableMapListener<K,V>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventNavigableMapListener(EventNavigableMapListener<K, V> listener)
   {
      if(_listeners != null)
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
   public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
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
   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
   {
      // TODO Auto-generated method stub
      return super.subMap(fromKey, fromInclusive, toKey, toInclusive);
   }

   @Override
   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
   {
      // TODO Auto-generated method stub
      return super.tailMap(fromKey, inclusive);
   }

   @Override
   public SortedMap<K, V> headMap(K toKey)
   {
      // TODO Auto-generated method stub
      return super.headMap(toKey);
   }

   @Override
   public SortedMap<K, V> subMap(K fromKey, K toKey)
   {
      // TODO Auto-generated method stub
      return super.subMap(fromKey, toKey);
   }

   @Override
   public SortedMap<K, V> tailMap(K fromKey)
   {
      // TODO Auto-generated method stub
      return super.tailMap(fromKey);
   }

   @Override
   protected void fireEntryAdded(K key, V value)
   {
      fireEvent(new NavigableMapEvent<K, V>(this, Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(K key, V value)
   {
      fireEvent(new NavigableMapEvent<K, V>(this, Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(K key, V oldValue, V newValue)
   {
      fireEvent(new NavigableMapEvent<K, V>(this, Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(NavigableMapEvent<K, V> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventNavigableMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
   
   protected static class ObservableEventSubNavigableMap<K, V> extends ObservableEventNavigableMap<K, V>
   {
      private static final long serialVersionUID = -863678987488740776L;

      protected final ObservableEventNavigableMap<K, V> _parent;

      protected ObservableEventSubNavigableMap(ObservableEventNavigableMap<K, V> parent, NavigableMap<K, V> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      protected void fireEntryAdded(K key, V value)
      {
         super.fireEntryAdded(key, value);
         _parent.fireEntryAdded(key, value);
      }

      @Override
      protected void fireEntryRemoved(K key, V value)
      {
         super.fireEntryRemoved(key, value);
         _parent.fireEntryRemoved(key, value);
      }

      @Override
      protected void fireEntryUpdated(K key, V oldValue, V newValue)
      {
         super.fireEntryUpdated(key, oldValue, newValue);
         _parent.fireEntryUpdated(key, oldValue, newValue);
      }
   }
}
