package net.sf.javagimmicks.collections8.decorators;

import java.util.NavigableMap;

/**
 * A basic class for unmodifiable {@link NavigableMap} decorators that simply
 * forwards all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableNavigableMapDecorator<K, V> extends
      AbstractUnmodifiableSortedMapDecorator<K, V> implements NavigableMap<K, V>
{
   private static final long serialVersionUID = 1571236822466623659L;

   protected AbstractUnmodifiableNavigableMapDecorator(final NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public Entry<K, V> ceilingEntry(final K key)
   {
      return getDecorated().ceilingEntry(key);
   }

   @Override
   public K ceilingKey(final K key)
   {
      return getDecorated().ceilingKey(key);
   }

   @Override
   public Entry<K, V> firstEntry()
   {
      return getDecorated().firstEntry();
   }

   @Override
   public Entry<K, V> floorEntry(final K key)
   {
      return getDecorated().floorEntry(key);
   }

   @Override
   public K floorKey(final K key)
   {
      return getDecorated().floorKey(key);
   }

   @Override
   public Entry<K, V> higherEntry(final K key)
   {
      return getDecorated().higherEntry(key);
   }

   @Override
   public K higherKey(final K key)
   {
      return getDecorated().higherKey(key);
   }

   @Override
   public Entry<K, V> lastEntry()
   {
      return getDecorated().lastEntry();
   }

   @Override
   public Entry<K, V> lowerEntry(final K key)
   {
      return getDecorated().lowerEntry(key);
   }

   @Override
   public K lowerKey(final K key)
   {
      return getDecorated().lowerKey(key);
   }

   @Override
   public Entry<K, V> pollFirstEntry()
   {
      return getDecorated().pollFirstEntry();
   }

   @Override
   public Entry<K, V> pollLastEntry()
   {
      return getDecorated().pollLastEntry();
   }

   @Override
   protected NavigableMap<K, V> getDecorated()
   {
      return (NavigableMap<K, V>) super.getDecorated();
   }
}
