package net.sf.javagimmicks.collections.decorators;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * A basic class for {@link NavigableMap} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractNavigableMapDecorator<K, V> extends AbstractSortedMapDecorator<K, V> implements NavigableMap<K, V>
{
   private static final long serialVersionUID = -1230904199447979932L;

   protected AbstractNavigableMapDecorator(NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   /**
    * Returns the decorated instance (the delegate)
    */
   @Override
   public NavigableMap<K, V> getDecorated()
   {
      return (NavigableMap<K, V>)super.getDecorated();
   }

   public Map.Entry<K, V> ceilingEntry(K key)
   {
      return getDecorated().ceilingEntry(key);
   }

   public K ceilingKey(K key)
   {
      return getDecorated().ceilingKey(key);
   }

   public NavigableSet<K> descendingKeySet()
   {
      return getDecorated().descendingKeySet();
   }

   public NavigableMap<K, V> descendingMap()
   {
      return getDecorated().descendingMap();
   }

   public Map.Entry<K, V> firstEntry()
   {
      return getDecorated().firstEntry();
   }

   public Map.Entry<K, V> floorEntry(K key)
   {
      return getDecorated().floorEntry(key);
   }

   public K floorKey(K key)
   {
      return getDecorated().floorKey(key);
   }

   public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
   {
      return getDecorated().headMap(toKey, inclusive);
   }

   public Map.Entry<K, V> higherEntry(K key)
   {
      return getDecorated().higherEntry(key);
   }

   public K higherKey(K key)
   {
      return getDecorated().higherKey(key);
   }

   public Map.Entry<K, V> lastEntry()
   {
      return getDecorated().lastEntry();
   }

   public Map.Entry<K, V> lowerEntry(K key)
   {
      return getDecorated().lowerEntry(key);
   }

   public K lowerKey(K key)
   {
      return getDecorated().lowerKey(key);
   }

   public NavigableSet<K> navigableKeySet()
   {
      return getDecorated().navigableKeySet();
   }

   public Map.Entry<K, V> pollFirstEntry()
   {
      return getDecorated().pollFirstEntry();
   }

   public Map.Entry<K, V> pollLastEntry()
   {
      return getDecorated().pollLastEntry();
   }

   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey,
         boolean toInclusive)
   {
      return getDecorated().subMap(fromKey, fromInclusive, toKey, toInclusive);
   }

   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
   {
      return getDecorated().tailMap(fromKey, inclusive);
   }
}
