package net.sf.javagimmicks.collections8.event;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

import net.sf.javagimmicks.collections8.event.NavigableMapEvent.Type;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

/**
 * A {@link NavigableMap} decorator that serves as an {@link Observable} for
 * {@link NavigableMapEvent}s.
 */
public class ObservableEventNavigableMap<K, V> extends AbstractEventNavigableMap<K, V> implements
      Observable<NavigableMapEvent<K, V>>
{
   private static final long serialVersionUID = -4936595637793434597L;

   protected final ObservableBase<NavigableMapEvent<K, V>> _helper = new ObservableBase<NavigableMapEvent<K, V>>();

   /**
    * Wraps a new {@link ObservableEventNavigableMap} around a given
    * {@link NavigableMap}.
    * 
    * @param decorated
    *           the {@link NavigableMap} to wrap around
    */
   public ObservableEventNavigableMap(final NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public <L extends EventListener<NavigableMapEvent<K, V>>> void addEventListener(final L listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public <L extends EventListener<NavigableMapEvent<K, V>>> void removeEventListener(final L listener)
   {
      _helper.removeEventListener(listener);
   }

   @Override
   public NavigableSet<K> descendingKeySet()
   {
      // TODO: Wrap result
      return super.descendingKeySet();
   }

   @Override
   public NavigableMap<K, V> descendingMap()
   {
      return new ObservableEventSubNavigableMap<K, V>(this, getDecorated().descendingMap());
   }

   @Override
   public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive)
   {
      return new ObservableEventSubNavigableMap<K, V>(this, getDecorated().headMap(toKey, inclusive));
   }

   @Override
   public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
         final boolean toInclusive)
   {
      return new ObservableEventSubNavigableMap<K, V>(this, getDecorated().subMap(fromKey, fromInclusive, toKey,
            toInclusive));
   }

   @Override
   public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive)
   {
      return new ObservableEventSubNavigableMap<K, V>(this, getDecorated().tailMap(fromKey, inclusive));
   }

   @Override
   public SortedMap<K, V> headMap(final K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().headMap(toKey));
   }

   @Override
   public SortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().subMap(fromKey, toKey));
   }

   @Override
   public SortedMap<K, V> tailMap(final K fromKey)
   {
      return new ObservableEventSubSortedMap<K, V>(this, getDecorated().tailMap(fromKey));
   }

   @Override
   public NavigableSet<K> navigableKeySet()
   {
      // TODO: Wrap result
      return super.navigableKeySet();
   }

   @Override
   protected void fireEntryAdded(final K key, final V value)
   {
      _helper.fireEvent(new NavigableMapEventImpl(Type.ADDED, key, value));
   }

   @Override
   protected void fireEntryRemoved(final K key, final V value)
   {
      _helper.fireEvent(new NavigableMapEventImpl(Type.REMOVED, key, value));
   }

   @Override
   protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
   {
      _helper.fireEvent(new NavigableMapEventImpl(Type.UPDATED, key, oldValue, newValue));
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

   protected static class ObservableEventSubSortedMap<K, V> extends ObservableEventSortedMap<K, V>
   {
      private static final long serialVersionUID = 8307521297661725017L;

      protected final ObservableEventNavigableMap<K, V> _parent;

      protected ObservableEventSubSortedMap(final ObservableEventNavigableMap<K, V> parent,
            final SortedMap<K, V> decorated)
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
