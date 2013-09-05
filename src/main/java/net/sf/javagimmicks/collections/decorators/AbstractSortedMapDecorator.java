package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.SortedMap;

public abstract class AbstractSortedMapDecorator<K, V> extends AbstractMapDecorator<K, V> implements SortedMap<K, V>
{
   private static final long serialVersionUID = -6446717166131956321L;

   protected AbstractSortedMapDecorator(SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>)super.getDecorated();
   }

   public Comparator<? super K> comparator()
   {
      return getDecorated().comparator();
   }

   public K firstKey()
   {
      return getDecorated().firstKey();
   }

   public SortedMap<K, V> headMap(K toKey)
   {
      return getDecorated().headMap(toKey);
   }

   public K lastKey()
   {
      return getDecorated().lastKey();
   }

   public SortedMap<K, V> subMap(K fromKey, K toKey)
   {
      return getDecorated().subMap(fromKey, toKey);
   }

   public SortedMap<K, V> tailMap(K fromKey)
   {
      return getDecorated().tailMap(fromKey);
   }
}
