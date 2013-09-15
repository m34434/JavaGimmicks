package net.sf.javagimmicks.collections.event;

import java.util.Comparator;
import java.util.SortedMap;

public abstract class AbstractEventSortedMap<K, V> extends AbstractEventMap<K, V> implements SortedMap<K, V>
{
   private static final long serialVersionUID = 87301004221670854L;

   public AbstractEventSortedMap(SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>) super.getDecorated();
   }

   public Comparator<? super K> comparator()
   {
      return getDecorated().comparator();
   }

   public K firstKey()
   {
      return getDecorated().firstKey();
   }

   public K lastKey()
   {
      return getDecorated().lastKey();
   }

   public SortedMap<K, V> headMap(K toKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().headMap(toKey));
   }

   public SortedMap<K, V> subMap(K fromKey, K toKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().subMap(fromKey, toKey));
   }

   public SortedMap<K, V> tailMap(K fromKey)
   {
      return new EventSubSortedMap<K, V>(this, getDecorated().tailMap(fromKey));
   }
   
   protected static class EventSubSortedMap<K, V> extends AbstractEventSortedMap<K, V>
   {
      private static final long serialVersionUID = -6673032656718478269L;

      protected final AbstractEventSortedMap<K, V> _parent;

      protected EventSubSortedMap(AbstractEventSortedMap<K, V> parent, SortedMap<K, V> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      protected void fireEntryAdded(K key, V value)
      {
         _parent.fireEntryAdded(key, value);
      }

      @Override
      protected void fireEntryRemoved(K key, V value)
      {
         _parent.fireEntryRemoved(key, value);
      }

      @Override
      protected void fireEntryUpdated(K key, V oldValue, V newValue)
      {
         _parent.fireEntryUpdated(key, oldValue, newValue);
      }
   }
}
