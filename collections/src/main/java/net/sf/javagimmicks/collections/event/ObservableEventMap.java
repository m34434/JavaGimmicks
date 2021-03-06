package net.sf.javagimmicks.collections.event;

import java.util.Map;

import net.sf.javagimmicks.collections.event.MapEvent.Type;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

/**
 * A {@link Map} decorator that serves as an {@link Observable} for
 * {@link MapEvent}s.
 */
public class ObservableEventMap<K, V> extends AbstractEventMap<K, V> implements
      Observable<MapEvent<K, V>>
{
   private static final long serialVersionUID = 8006998141057065129L;

   protected final ObservableBase<MapEvent<K, V>> _helper = new ObservableBase<MapEvent<K, V>>();

   /**
    * Wraps a new {@link ObservableEventMap} around a given {@link Map}.
    * 
    * @param decorated
    *           the {@link Map} to wrap around
    */
   public ObservableEventMap(final Map<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public <L extends EventListener<MapEvent<K, V>>> void addEventListener(final L listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public <L extends EventListener<MapEvent<K, V>>> void removeEventListener(final L listener)
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
