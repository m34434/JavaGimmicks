package net.sf.javagimmicks.collections.event;

public interface EventCollectionListener<E>
{
   public void eventOccured(CollectionEvent<E> event);
}
