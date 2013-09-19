package net.sf.javagimmicks.collections.event;

import java.util.List;

public interface ListEvent<E>
{

   public static enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   Type getType();

   int getFromIndex();

   int getToIndex();

   List<E> getElements();

   List<E> getNewElements();

   ObservableEventList<E> getSource();

}