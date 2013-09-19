package net.sf.javagimmicks.collections.event;

public interface SetEvent<E> extends Event<SetEvent<E>, EventSetListener<E>>
{
   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();
}