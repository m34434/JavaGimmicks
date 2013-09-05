package net.sf.javagimmicks.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMap2<K, V> extends AbstractMap<K, V>
{
   @Override
   abstract public Set<K> keySet();
   abstract protected V getValue(K key);

   @Override
   public Set<Map.Entry<K, V>> entrySet()
   {
      final Set<K> keySet = keySet();
      return new EntrySetProxy(keySet);
   }
   
   protected class EntrySetProxy extends AbstractSet<Entry<K, V>>
   {
      protected final Set<K> _keySet;

      protected EntrySetProxy(Set<K> keySet)
      {
         _keySet = keySet;
      }

      @Override
      public Iterator<Entry<K, V>> iterator()
      {
         final Iterator<K> keyIterator = _keySet.iterator();
         return new EntryIteratorProxy(keyIterator);
      }

      @Override
      public int size()
      {
         return _keySet.size();
      }
   }
   
   protected class EntryIteratorProxy implements Iterator<Entry<K, V>>
   {
      protected final Iterator<K> _keyIterator;

      protected EntryIteratorProxy(Iterator<K> keyIterator)
      {
         _keyIterator = keyIterator;
      }

      public boolean hasNext()
      {
         return _keyIterator.hasNext();
      }

      public Entry<K, V> next()
      {
         final K key = _keyIterator.next();
         final V value = getValue(key);
         
         return new EntryProxy(key, value);
      }

      public void remove()
      {
         _keyIterator.remove();
      }
   }

   protected final class EntryProxy implements Entry<K, V>
   {
      protected final K _key;
      protected final V _value;

      private EntryProxy(K key, V value)
      {
         _key = key;
         _value = value;
      }

      public K getKey()
      {
         return _key;
      }

      public V getValue()
      {
         return _value;
      }

      public V setValue(V value)
      {
         // !! DANGEROUS: May cause a ConcurrentModificationException when
         // continuing to iterator over the map's entries
         put(_key, value);
         
         return _value;
      }
   }

}
