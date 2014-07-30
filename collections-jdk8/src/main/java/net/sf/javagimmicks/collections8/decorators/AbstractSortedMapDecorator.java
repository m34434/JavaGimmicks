package net.sf.javagimmicks.collections8.decorators;

import java.util.Comparator;
import java.util.SortedMap;

/**
 * A basic class for {@link SortedMap} decorators that simply forwards all calls
 * to an internal delegate instance.
 */
public abstract class AbstractSortedMapDecorator<K, V> extends AbstractMapDecorator<K, V> implements SortedMap<K, V>
{
   private static final long serialVersionUID = -6446717166131956321L;

   protected AbstractSortedMapDecorator(final SortedMap<K, V> decorated)
   {
      super(decorated);
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
   public SortedMap<K, V> headMap(final K toKey)
   {
      return getDecorated().headMap(toKey);
   }

   @Override
   public K lastKey()
   {
      return getDecorated().lastKey();
   }

   @Override
   public SortedMap<K, V> subMap(final K fromKey, final K toKey)
   {
      return getDecorated().subMap(fromKey, toKey);
   }

   @Override
   public SortedMap<K, V> tailMap(final K fromKey)
   {
      return getDecorated().tailMap(fromKey);
   }

   @Override
   protected SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>) super.getDecorated();
   }
}
