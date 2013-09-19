package net.sf.javagimmicks.collections.event;

public interface SetEvent<E>
{
   public static enum Type
   {
      ADDED, READDED, REMOVED
   }

   Type getType();

   E getElement();

   ObservableEventSet<E> getSource();

}