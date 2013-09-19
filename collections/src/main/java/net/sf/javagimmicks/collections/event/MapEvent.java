package net.sf.javagimmicks.collections.event;

public interface MapEvent<K, V>
{

   enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   ObservableEventMap<K, V> getSource();

   Type getType();

   K getKey();

   V getValue();

   V getNewValue();

}