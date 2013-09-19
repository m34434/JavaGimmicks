package net.sf.javagimmicks.collections.event;

import java.util.Collection;

/**
 * Represents a change in a {@link Observable} {@link Collection} - e.g.
 * {@link ObservableEventCollection}.
 */
public interface CollectionEvent<E> extends Event<CollectionEvent<E>, EventCollectionListener<E>>
{
   /**
    * The possible types of events.
    */
   enum Type
   {
      /**
       * One ore more elements were added
       */
      ADDED,

      /**
       * One or more elements were removed
       */
      REMOVED
   }

   Type getType();

   Collection<E> getElements();
}