package net.sf.javagimmicks.collections.event;

import java.util.Comparator;
import java.util.SortedMap;

/**
 * A base {@link SortedMap} wrapper that reports changes to internal callback
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
public abstract class AbstractEventSortedMap<K, V> extends AbstractEventMap<K, V> implements SortedMap<K, V>
{
   private static final long serialVersionUID = 87301004221670854L;

   /**
    * Wraps a new instance around a given {@link SortedMap}
    * 
    * @param decorated
    *           the {@link SortedMap} to wrap
    */
   public AbstractEventSortedMap(final SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>) super.getDecorated();
   }

   @Override
   public Comparator<? super K> comparator()
   {
      return getDecorated().comparator();
   }

   @Override
   public K firstKey()
   {
      return getDecorated().firstKey();
   }

   @Override
   public K lastKey()
   {
      return getDecorated().lastKey();
   }

   @Override
   public SortedMap<K, V> headMap(final K toKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().headMap(toKey));
   }

   @Override
   public SortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().subMap(fromKey, toKey));
   }

   @Override
   public SortedMap<K, V> tailMap(final K fromKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().tailMap(fromKey));
   }

   protected static class EventSubSortedMap<K, V> extends AbstractEventSortedMap<K, V>
   {
      private static final long serialVersionUID = -6673032656718478269L;

      protected final AbstractEventSortedMap<K, V> _parent;

      protected EventSubSortedMap(final AbstractEventSortedMap<K, V> parent, final SortedMap<K, V> decorated)
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
}
