package net.sf.javagimmicks.collections.event;

import java.util.Map;

import net.sf.javagimmicks.collections.event.MapEvent.Type;

public class ObservableEventMap<K, V> extends AbstractEventMap<K, V> implements
      Observable<MapEvent<K, V>, EventMapListener<K, V>>
{
   private static final long serialVersionUID = 8006998141057065129L;

   protected final ObservableBase<MapEvent<K, V>, EventMapListener<K, V>> _helper = new ObservableBase<MapEvent<K, V>, EventMapListener<K, V>>();

   public ObservableEventMap(final Map<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventMapListener<K, V> listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public void removeEventListener(final EventMapListener<K, V> listener)
   {
      _helper.removeEventListener(listener);
   }

   @Override
   protected void fireEntryAdded(final K key, final V value)
   {
      _helper.fireEvent(new MapEventImpl(Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(final K key, final V value)
   {
      _helper.fireEvent(new MapEventImpl(Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
   {
      _helper.fireEvent(new MapEventImpl(Type.UPDATED, key, oldValue, newValue));
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
