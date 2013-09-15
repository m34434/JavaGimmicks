package net.sf.javagimmicks.collections.bidimap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DualBidiMap<K, V> extends AbstractMap<K, V> implements BidiMap<K, V>
{
   protected final Map<K, V> _forwardMap;
   protected final Map<V, K> _reverseMap;
   
   DualBidiMap(Map<K, V> forwardMap, Map<V, K> reverseMap)
   {
      _forwardMap = forwardMap;
      _reverseMap = reverseMap;
   }

   @Override
   public Set<Entry<K, V>> entrySet()
   {
      return new DualBidiEntrySet(getForwardMap().entrySet());
   }
   
   @Override
   public V put(K key, V value)
   {
      checkValue(value);

      V oldValue = getForwardMap().put(key, value);
      K oldKey = getReverseMap().put(value, key);
      
      getForwardMap().remove(oldKey);
      getReverseMap().remove(oldValue);
      
      return oldValue;
   }

   @Override
   public V remove(Object key)
   {
      V value = getForwardMap().remove(key);
      getReverseMap().remove(value);
      
      return value;
   }

   public BidiMap<V, K> inverseBidiMap()
   {
      return new InverseDualBidiMap(getReverseMap(), getForwardMap());
   }
   
   @Override
   public V get(Object key)
   {
      return getForwardMap().get(key);
   }
   
   public K getKey(V value)
   {
      return getReverseMap().get(value);
   }

   protected Map<K, V> getForwardMap()
   {
      return _forwardMap;
   }
   
   protected Map<V, K> getReverseMap()
   {
      return _reverseMap;
   }
   
   protected static <V extends Object> void checkValue(V value)
   {
      if(value == null)
      {
         throw new IllegalArgumentException("Null values not allowed in BidiMaps!");
      }
   }

   protected class DualBidiEntry implements Entry<K, V>
   {
      protected final Entry<K, V> internalEntry;

      protected DualBidiEntry(Entry<K, V> internalEntry)
      {
         this.internalEntry = internalEntry;
      }

      public K getKey()
      {
         return internalEntry.getKey();
      }

      public V getValue()
      {
         return internalEntry.getValue();
      }

      public V setValue(V value)
      {
         return put(getKey(), value);
      }
   }

   protected class DualBidiEntryIterator implements Iterator<Entry<K, V>>
   {
      protected final Iterator<Entry<K, V>> internalIterator;
      protected Entry<K, V> _lastEntry;

      protected DualBidiEntryIterator(Iterator<Entry<K, V>> internalIterator)
      {
         this.internalIterator = internalIterator;
      }

      public boolean hasNext()
      {
         return internalIterator.hasNext();
      }

      public Entry<K, V> next()
      {
         _lastEntry = internalIterator.next();
         return new DualBidiEntry(_lastEntry);
      }

      public void remove()
      {
         internalIterator.remove();
         _reverseMap.remove(_lastEntry.getValue());
      }
   }

   protected class DualBidiEntrySet extends AbstractSet<Entry<K, V>>
   {
      private final Set<Entry<K, V>> internalEntrySet;

      protected DualBidiEntrySet(Set<Entry<K, V>> internalEntrySet)
      {
         this.internalEntrySet = internalEntrySet;
      }

      @Override
      public Iterator<Entry<K, V>> iterator()
      {
         return new DualBidiEntryIterator(internalEntrySet.iterator());
      }

      @Override
      public int size()
      {
         return internalEntrySet.size();
      }
   }

   protected class InverseDualBidiMap extends DualBidiMap<V, K>
   {
      protected InverseDualBidiMap(Map<V, K> reverseMap, Map<K, V> forwardMap)
      {
         super(reverseMap, forwardMap);
      }

      @Override
      public BidiMap<K, V> inverseBidiMap()
      {
         return DualBidiMap.this;
      }
   }
}
