package net.sf.javagimmicks.collections.event;

public interface SortedSetEvent<E> extends Event<SortedSetEvent<E>, EventSortedSetListener<E>>
{
   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();
}