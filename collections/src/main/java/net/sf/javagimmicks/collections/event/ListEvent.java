package net.sf.javagimmicks.collections.event;

import java.util.List;

public interface ListEvent<E> extends Event<ListEvent<E>, EventListListener<E>>
{
   enum Type
   {
      ADDED, UPDATED, REMOVED
   }

   Type getType();

   int getFromIndex();

   int getToIndex();

   List<E> getElements();

   List<E> getNewElements();
}