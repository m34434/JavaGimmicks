package net.sf.javagimmicks.collections.event;

public interface NavigableMapEvent<K, V>
{

   enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   ObservableEventNavigableMap<K, V> getSource();

   Type getType();

   K getKey();

   V getValue();

   V getNewValue();

}