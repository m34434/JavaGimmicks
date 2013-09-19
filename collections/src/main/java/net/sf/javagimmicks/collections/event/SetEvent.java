package net.sf.javagimmicks.collections.event;

public interface SetEvent<E>
{
   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();

   ObservableEventSet<E> getSource();

}