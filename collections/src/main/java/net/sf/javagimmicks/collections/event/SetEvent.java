package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.Event;

public interface SetEvent<E> extends Event<SetEvent<E>, EventSetListener<E>>
{
   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();
}