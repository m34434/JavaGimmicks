package net.sf.javagimmicks.collections.bidimap;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;

public class DualSortedBidiMap<K, V> extends DualBidiMap<K, V> implements SortedBidiMap<K, V>
{
   DualSortedBidiMap(SortedMap<K, V> forwardMap, SortedMap<V, K> reverseMap)
   {
      super(forwardMap, reverseMap);
   }

   public SortedBidiMap<V, K> inverseBidiSortedMap()
   {
      return new InverseDualSortedBidiMap(getReverseMap(), getForwardMap());
   }
   
   public SortedMap<K, V> headMap(K toKey)
   {
      return new SortedSubMapDecorator(getForwardMap().headMap(toKey));
   }

   public SortedMap<K, V> subMap(K fromKey, K toKey)
   {
      return new SortedSubMapDecorator(getForwardMap().subMap(fromKey, toKey));
   }

   public SortedMap<K, V> tailMap(K fromKey)
   {
      return new SortedSubMapDecorator(getForwardMap().tailMap(fromKey));
   }

   public Comparator<? super K> comparator()
   {
      return getForwardMap().comparator();
   }

   public K firstKey()
   {
      return getForwardMap().firstKey();
   }

   public K lastKey()
   {
      return getForwardMap().lastKey();
   }

   @Override
   protected SortedMap<K, V> getForwardMap()
   {
      return (SortedMap<K, V>)super.getForwardMap();
   }

   @Override
   protected SortedMap<V, K> getReverseMap()
   {
      return (SortedMap<V, K>)super.getReverseMap();
   }
   
   protected class InverseDualSortedBidiMap extends DualSortedBidiMap<V, K>
   {
      protected InverseDualSortedBidiMap(SortedMap<V, K> reverseMap, SortedMap<K, V> forwardMap)
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

      protected SortedSubMapDecorator(SortedMap<K, V> oSubMap)
      {
         m_oSubMap = oSubMap;
      }
      
      @Override
      public Set<Entry<K, V>> entrySet()
      {
         return new DualBidiEntrySet(m_oSubMap.entrySet());
      }

      public SortedMap<K, V> headMap(K toKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.headMap(toKey));
      }

      public SortedMap<K, V> subMap(K fromKey, K toKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.subMap(fromKey, toKey));
      }

      public SortedMap<K, V> tailMap(K fromKey)
      {
         return new SortedSubMapDecorator(m_oSubMap.tailMap(fromKey));
      }
      
      @Override
      public V put(K key, V value)
      {
         checkValue(value);

         V oldValue = m_oSubMap.put(key, value);
         K oldKey = getReverseMap().put(value, key);
         
         getForwardMap().remove(oldKey);
         getReverseMap().remove(oldValue);
         
         return oldValue;
      }

      public void clear()
      {
         m_oSubMap.clear();
      }

      public Comparator<? super K> comparator()
      {
         return m_oSubMap.comparator();
      }

      public boolean containsKey(Object key)
      {
         return m_oSubMap.containsKey(key);
      }

      public boolean containsValue(Object value)
      {
         return m_oSubMap.containsValue(value);
      }

      public K firstKey()
      {
         return m_oSubMap.firstKey();
      }

      public V get(Object key)
      {
         return m_oSubMap.get(key);
      }

      public boolean isEmpty()
      {
         return m_oSubMap.isEmpty();
      }

      public K lastKey()
      {
         return m_oSubMap.lastKey();
      }

      public int size()
      {
         return m_oSubMap.size();
      }
   }
}
