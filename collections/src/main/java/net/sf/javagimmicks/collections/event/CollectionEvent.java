package net.sf.javagimmicks.collections.event;

import java.util.Collection;

public interface CollectionEvent<E>
{

   enum Type
   {
      ADDED, REMOVED
   }

   Type getType();

   Collection<E> getElements();

   ObservableEventCollection<E> getSource();

}