package net.sf.javagimmicks.collections.decorators;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * A basic class for {@link NavigableMap} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public abstract class AbstractNavigableMapDecorator<K, V> extends AbstractSortedMapDecorator<K, V> implements
      NavigableMap<K, V>
{
   private static final long serialVersionUID = -1230904199447979932L;

   protected AbstractNavigableMapDecorator(final NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public Map.Entry<K, V> ceilingEntry(final K key)
   {
      return getDecorated().ceilingEntry(key);
   }

   @Override
   public K ceilingKey(final K key)
   {
      return getDecorated().ceilingKey(key);
   }

   @Override
   public NavigableSet<K> descendingKeySet()
   {
      return getDecorated().descendingKeySet();
   }

   @Override
   public NavigableMap<K, V> descendingMap()
   {
      return getDecorated().descendingMap();
   }

   @Override
   public Map.Entry<K, V> firstEntry()
   {
      return getDecorated().firstEntry();
   }

   @Override
   public Map.Entry<K, V> floorEntry(final K key)
   {
      return getDecorated().floorEntry(key);
   }

   @Override
   public K floorKey(final K key)
   {
      return getDecorated().floorKey(key);
   }

   @Override
   public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive)
   {
      return getDecorated().headMap(toKey, inclusive);
   }

   @Override
   public Map.Entry<K, V> higherEntry(final K key)
   {
      return getDecorated().higherEntry(key);
   }

   @Override
   public K higherKey(final K key)
   {
      return getDecorated().higherKey(key);
   }

   @Override
   public Map.Entry<K, V> lastEntry()
   {
      return getDecorated().lastEntry();
   }

   @Override
   public Map.Entry<K, V> lowerEntry(final K key)
   {
      return getDecorated().lowerEntry(key);
   }

   @Override
   public K lowerKey(final K key)
   {
      return getDecorated().lowerKey(key);
   }

   @Override
   public NavigableSet<K> navigableKeySet()
   {
      return getDecorated().navigableKeySet();
   }

   @Override
   public Map.Entry<K, V> pollFirstEntry()
   {
      return getDecorated().pollFirstEntry();
   }

   @Override
   public Map.Entry<K, V> pollLastEntry()
   {
      return getDecorated().pollLastEntry();
   }

   @Override
   public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey,
         final boolean toInclusive)
   {
      return getDecorated().subMap(fromKey, fromInclusive, toKey, toInclusive);
   }

   @Override
   public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive)
   {
      return getDecorated().tailMap(fromKey, inclusive);
   }

   @Override
   protected NavigableMap<K, V> getDecorated()
   {
      return (NavigableMap<K, V>) super.getDecorated();
   }
}
