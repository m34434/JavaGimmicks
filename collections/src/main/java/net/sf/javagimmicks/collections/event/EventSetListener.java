package net.sf.javagimmicks.collections.event;

public interface EventSetListener<E>
{
   public void eventOccured(SetEvent<E> event);
}
