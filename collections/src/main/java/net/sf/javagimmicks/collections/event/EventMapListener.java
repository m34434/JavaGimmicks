package net.sf.javagimmicks.collections.event;

public interface EventMapListener<K, V>
{  
   public void eventOccured(MapEvent<K, V> event);
}
