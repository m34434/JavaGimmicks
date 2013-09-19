package net.sf.javagimmicks.collections.event;


public interface SortedMapEvent<K, V>
{

   enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   ObservableEventSortedMap<K, V> getSource();

   Type getType();

   K getKey();

   V getValue();

   V getNewValue();

}