package net.sf.javagimmicks.collections.bidimap;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;

class DualSortedBidiMap<K, V> extends DualBidiMap<K, V> implements SortedBidiMap<K, V>
{
   DualSortedBidiMap(final SortedMap<K, V> forwardMap, final SortedMap<V, K> reverseMap)
   {
      super(forwardMap, reverseMap);
   }

   @Override
   public SortedBidiMap<V, K> inverseBidiSortedMap()
   {
      return new InverseDualSortedBidiMap(getReverseMap(), getForwardMap());
   }

   @Override
   public SortedMap<K, V> headMap(final K toKey)
   {
      return new SortedSubMapDecorator(getForwardMap().headMap(toKey));
   }

   @Override
   public SortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      return new SortedSubMapDecorator(getForwardMap().subMap(fromKey, toKey));
   }

   @Override
   public SortedMap<K, V> tailMap(final K fromKey)
   {
      return new SortedSubMapDecorator(getForwardMap().tailMap(fromKey));
   }

   @Override
   public Comparator<? super K> comparator()
   {
      return getForwardMap().comparator();
   }

   @Override
   public K firstKey()
   {
      return getForwardMap().firstKey();
   }

   @Override
   public K lastKey()
   {
      return getForwardMap().lastKey();
   }

   @Override
   protected SortedMap<K, V> getForwardMap()
   {
      return (SortedMap<K, V>) super.getForwardMap();
   }

   @Override
   protected SortedMap<V, K> getReverseMap()
   {
      return (SortedMap<V, K>) super.getReverseMap();
   }

   protected class InverseDualSortedBidiMap extends DualSortedBidiMap<V, K>
   {
      protected InverseDualSortedBidiMap(final SortedMap<V, K> reverseMap, final SortedMap<K, V> forwardMap)
      {
         super(reverseMap, forwardMap);
      }

      @Override
      public SortedBidiMap<K, V> inverseBidiSortedMap()
      {
         return DualSortedBidiMap.this;
      }
   }

   protected class SortedSubMapDecorator extends AbstractMap<K, V> implements SortedMap<K, V>
   {
      protected final SortedMap<K, V> m_oSubMap;

      protected SortedSubMapDecorator(final SortedMap<K, V> oSubMap)
      {
         m_oSubMap = oSubMap;
      }

      @Override
      public Set<Entry<K, V>> entrySet()
      {
         return new DualBidiEntrySet(m_oSubMap.entrySet());
      }

      @Override
      public SortedMap<K, V> headMap(final K toKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.headMap(toKey));
      }

      @Override
      public SortedMap<K, V> subMap(final K fromKey, final K toKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.subMap(fromKey, toKey));
      }

      @Override
      public SortedMap<K, V> tailMap(final K fromKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.tailMap(fromKey));
      }

      @Override
      public V put(final K key, final V value)
      {
         checkValue(value);

         final V oldValue = m_oSubMap.put(key, value);
         final K oldKey = getReverseMap().put(value, key);

         getForwardMap().remove(oldKey);
         getReverseMap().remove(oldValue);

         return oldValue;
      }

      @Override
      public void clear()
      {
         m_oSubMap.clear();
      }

      @Override
      public Comparator<? super K> comparator()
      {
         return m_oSubMap.comparator();
      }

      @Override
      public boolean containsKey(final Object key)
      {
         return m_oSubMap.containsKey(key);
      }

      @Override
      public boolean containsValue(final Object value)
      {
         return m_oSubMap.containsValue(value);
      }

      @Override
      public K firstKey()
      {
         return m_oSubMap.firstKey();
      }

      @Override
      public V get(final Object key)
      {
         return m_oSubMap.get(key);
      }

      @Override
      public boolean isEmpty()
      {
         return m_oSubMap.isEmpty();
      }

      @Override
      public K lastKey()
      {
         return m_oSubMap.lastKey();
      }

      @Override
      public int size()
      {
         return m_oSubMap.size();
      }
   }
}
