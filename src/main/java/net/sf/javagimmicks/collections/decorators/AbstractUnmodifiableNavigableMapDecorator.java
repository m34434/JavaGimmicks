package net.sf.javagimmicks.collections.decorators;

import java.util.NavigableMap;

public abstract class AbstractUnmodifiableNavigableMapDecorator<K, V> extends AbstractUnmodifiableSortedMapDecorator<K, V> implements NavigableMap<K, V>
{
   private static final long serialVersionUID = 1571236822466623659L;

   protected AbstractUnmodifiableNavigableMapDecorator(NavigableMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public NavigableMap<K, V> getDecorated()
   {
      return (NavigableMap<K, V>) super.getDecorated();
   }

   public Entry<K, V> ceilingEntry(K key)
   {
      return getDecorated().ceilingEntry(key);
   }

   public K ceilingKey(K key)
   {
      return getDecorated().ceilingKey(key);
   }

   public Entry<K, V> firstEntry()
   {
      return getDecorated().firstEntry();
   }

   public Entry<K, V> floorEntry(K key)
   {
      return getDecorated().floorEntry(key);
   }

   public K floorKey(K key)
   {
      return getDecorated().floorKey(key);
   }

   public Entry<K, V> higherEntry(K key)
   {
      return getDecorated().higherEntry(key);
   }

   public K higherKey(K key)
   {
      return getDecorated().higherKey(key);
   }

   public Entry<K, V> lastEntry()
   {
      return getDecorated().lastEntry();
   }

   public Entry<K, V> lowerEntry(K key)
   {
      return getDecorated().lowerEntry(key);
   }

   public K lowerKey(K key)
   {
      return getDecorated().lowerKey(key);
   }

   public Entry<K, V> pollFirstEntry()
   {
      return getDecorated().pollFirstEntry();
   }

   public Entry<K, V> pollLastEntry()
   {
      return getDecorated().pollLastEntry();
   }
}
