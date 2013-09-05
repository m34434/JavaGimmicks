package net.sf.javagimmicks.collections.event;

public interface EventListListener<E>
{
   public void eventOccured(ListEvent<E> event);
}
