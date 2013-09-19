package net.sf.javagimmicks.collections.event;

public interface NavigableSetEvent<E>
{

   enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();

   ObservableEventNavigableSet<E> getSource();

}