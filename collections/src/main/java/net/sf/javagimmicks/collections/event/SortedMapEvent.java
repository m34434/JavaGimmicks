package net.sf.javagimmicks.collections.event;

public interface SortedMapEvent<K, V> extends Event<SortedMapEvent<K, V>, EventSortedMapListener<K, V>>
{
   enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   Type getType();

   K getKey();

   V getValue();

   V getNewValue();

}