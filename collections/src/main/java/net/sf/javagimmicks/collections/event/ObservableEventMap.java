package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.javagimmicks.collections.event.MapEvent.Type;

public class ObservableEventMap<K, V> extends AbstractEventMap<K, V>
{
   private static final long serialVersionUID = 8006998141057065129L;

   protected transient List<EventMapListener<K, V>> _listeners;
   
   public ObservableEventMap(Map<K, V> decorated)
   {
      super(decorated);
   }  

   public void addEventMapListener(EventMapListener<K, V> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventMapListener<K,V>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventMapListener(EventMapListener<K, V> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
   }
   
   @Override
   protected void fireEntryAdded(K key, V value)
   {
      fireEvent(new MapEvent<K, V>(this, Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(K key, V value)
   {
      fireEvent(new MapEvent<K, V>(this, Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(K key, V oldValue, V newValue)
   {
      fireEvent(new MapEvent<K, V>(this, Type.UPDATED, key, oldValue, newValue));
   }

   private void fireEvent(MapEvent<K, V> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventMapListener<K, V> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
}
