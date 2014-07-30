package net.sf.javagimmicks.collections8.decorators;

import java.util.Comparator;
import java.util.SortedMap;

/**
 * A basic class for unmodifiable {@link SortedMap} decorators that simply
 * forwards all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableSortedMapDecorator<K, V> extends AbstractUnmodifiableMapDecorator<K, V>
      implements SortedMap<K, V>
{
   private static final long serialVersionUID = 6155129365977806691L;

   protected AbstractUnmodifiableSortedMapDecorator(final SortedMap<K, V> decorated)
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
   public K lastKey()
   {
      return getDecorated().lastKey();
   }

   @Override
   protected SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>) super.getDecorated();
   }
}
