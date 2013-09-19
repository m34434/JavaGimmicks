package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.Event;

public interface MapEvent<K, V> extends Event<MapEvent<K, V>, EventMapListener<K, V>>
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