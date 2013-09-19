package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.Event;

public interface NavigableSetEvent<E> extends Event<NavigableSetEvent<E>, EventNavigableSetListener<E>>
{
   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();
}