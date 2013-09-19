package net.sf.javagimmicks.collections.event;

public interface NavigableMapEvent<K, V>
{

   public static enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   ObservableEventNavigableMap<K, V> getSource();

   Type getType();

   K getKey();

   V getValue();

   V getNewValue();

}