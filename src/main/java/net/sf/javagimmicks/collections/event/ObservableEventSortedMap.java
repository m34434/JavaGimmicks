package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import net.sf.javagimmicks.collections.event.SortedMapEvent.Type;

public class ObservableEventSortedMap<K, V> extends AbstractEventSortedMap<K, V>
{
   private static final long serialVersionUID = -4377528012758388630L;

   protected transient List<EventSortedMapListener<K, V>> _listeners;
   
   public ObservableEventSortedMap(SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   public void addEventSortedMapListener(EventSortedMapListener<K, V> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventSortedMapListener<K,V>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventSortedMapListener(EventSortedMapListener<K, V> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
   }
   
   @Override
   public ObservableEventSortedMap<K, V> headMap(K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().headMap(toKey));
   }

   @Override
   public ObservableEventSortedMap<K, V> subMap(K fromKey, K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().subMap(fromKey, toKey));
   }

   @Override
   public ObservableEventSortedMap<K, V> tailMap(K fromKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().tailMap(fromKey));
   }

   @Override
   protected void fireEntryAdded(K key, V value)
   {
      fireEvent(new SortedMapEvent<K, V>(this, Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(K key, V value)
   {
      fireEvent(new SortedMapEvent<K, V>(this, Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(K key, V oldValue, V newValue)
   {
      fireEvent(new SortedMapEvent<K, V>(this, Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(SortedMapEvent<K, V> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventSortedMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
   
   protected static class ObservableEventSubSortedMap<K, V> extends ObservableEventSortedMap<K, V>
   {
      private static final long serialVersionUID = 8431554623263356463L;

      protected final ObservableEventSortedMap<K, V> _parent;
      
      protected ObservableEventSubSortedMap(ObservableEventSortedMap<K, V> parent, SortedMap<K, V> decorated)
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
