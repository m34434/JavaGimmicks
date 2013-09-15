package net.sf.javagimmicks.collections.event;

public interface EventNavigableMapListener<K, V>
{  
   public void eventOccured(NavigableMapEvent<K, V> event);
}
