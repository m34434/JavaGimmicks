package net.sf.javagimmicks.collections.event;

import java.util.NavigableMap;
import java.util.NavigableSet;

import net.sf.javagimmicks.collections.decorators.AbstractEntryDecorator;

/**
 * A base {@link NavigableMap} wrapper that reports changes to internal callback
 * methods - these must be overwritten by concrete implementations in order to
 * react in any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireEntryAdded(Object, Object)}</li>
 * <li>{@link #fireEntryUpdated(Object, Object, Object)}</li>
 * <li>{@link #fireEntryRemoved(Object, Object)}</li>
 * </ul>
 */
public abstract class AbstractEventNavigableMap<K, V> extends AbstractEventSortedMap<K, V> implements
      NavigableMap<K, V>
{
   private static final long serialVersionUID = 7570207692375842675L;

   /**
    * Wraps a new instance around a given {@link NavigableMap}
    * 
    * @param decorated
    *           the {@link NavigableMap} to wrap
    */
   public AbstractEventNavigableMap(final NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public NavigableMap<K, V> getDecorated()
   {
      return (NavigableMap<K, V>) super.getDecorated();
   }

   @Override
   public Entry<K, V> ceilingEntry(final K key)
   {
      return new EventEntry<K, V>(this, getDecorated().ceilingEntry(key));
   }

   @Override
   public K ceilingKey(final K key)
   {
      return getDecorated().ceilingKey(key);
   }

   @Override
   public Entry<K, V> firstEntry()
   {
      return new EventEntry<K, V>(this, getDecorated().firstEntry());
   }

   @Override
   public Entry<K, V> floorEntry(final K key)
   {
      return new EventEntry<K, V>(this, getDecorated().floorEntry(key));
   }

   @Override
   public K floorKey(final K key)
   {
      return getDecorated().floorKey(key);
   }

   @Override
   public Entry<K, V> higherEntry(final K key)
   {
      return new EventEntry<K, V>(this, getDecorated().higherEntry(key));
   }

   @Override
   public K higherKey(final K key)
   {
      return getDecorated().higherKey(key);
   }

   @Override
   public Entry<K, V> lastEntry()
   {
      return new EventEntry<K, V>(this, getDecorated().lastEntry());
   }

   @Override
   public Entry<K, V> lowerEntry(final K key)
   {
      return new EventEntry<K, V>(this, getDecorated().lowerEntry(key));
   }

   @Override
   public K lowerKey(final K key)
   {
      return getDecorated().lowerKey(key);
   }

   @Override
   public Entry<K, V> pollFirstEntry()
   {
      final Entry<K, V> firstEntry = getDecorated().pollFirstEntry();

      if (firstEntry != null)
      {
         fireEntryRemoved(firstEntry.getKey(), firstEntry.getValue());
      }

      return firstEntry;
   }

   @Override
   public Entry<K, V> pollLastEntry()
   {
      final Entry<K, V> lastEntry = getDecorated().pollLastEntry();

      if (lastEntry != null)
      {
         fireEntryRemoved(lastEntry.getKey(), lastEntry.getValue());
      }

      return lastEntry;
   }

   @Override
   public NavigableSet<K> descendingKeySet()
   {
      return descendingMap().navigableKeySet();
   }

   @Override
   public NavigableSet<K> navigableKeySet()
   {
      return new NavigableMapKeySetDecorator<K, V>(this);
   }

   @Override
   public NavigableMap<K, V> descendingMap()
   {
      return new EventSubNavigableMap<K, V>(this, getDecorated().descendingMap());
   }

   @Override
   public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive)
   {
      return new EventSubNavigableMap<K, V>(this, getDecorated().headMap(toKey, inclusive));
   }

   @Override
   public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
         final boolean toInclusive)
   {
      return new EventSubNavigableMap<K, V>(this, getDecorated().subMap(fromKey, fromInclusive, toKey, toInclusive));
   }

   @Override
   public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive)
   {
      return new EventSubNavigableMap<K, V>(this, getDecorated().tailMap(fromKey, inclusive));
   }

   protected static class EventSubNavigableMap<K, V> extends AbstractEventNavigableMap<K, V>
   {
      private static final long serialVersionUID = 8445308257944385932L;

      protected final AbstractEventNavigableMap<K, V> _parent;

      protected EventSubNavigableMap(final AbstractEventNavigableMap<K, V> parent, final NavigableMap<K, V> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      protected void fireEntryAdded(final K key, final V value)
      {
         _parent.fireEntryAdded(key, value);
      }

      @Override
      protected void fireEntryRemoved(final K key, final V value)
      {
         _parent.fireEntryRemoved(key, value);
      }

      @Override
      protected void fireEntryUpdated(final K key, final V oldValue, final V newValue)
      {
         _parent.fireEntryUpdated(key, oldValue, newValue);
      }
   }

   protected static class EventEntry<K, V> extends AbstractEntryDecorator<K, V>
   {
      private static final long serialVersionUID = 5721131374089977796L;

      protected final AbstractEventNavigableMap<K, V> _parent;

      public EventEntry(final AbstractEventNavigableMap<K, V> parent, final Entry<K, V> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      public V setValue(final V value)
      {
         final V oldValue = super.setValue(value);

         _parent.fireEntryUpdated(getKey(), oldValue, value);

         return oldValue;
      }
   }
}
