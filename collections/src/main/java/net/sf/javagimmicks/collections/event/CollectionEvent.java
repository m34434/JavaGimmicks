package net.sf.javagimmicks.collections.event;

import java.util.Collection;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link Collection} - like
 * {@link ObservableEventCollection}.
 */
public interface CollectionEvent<E> extends Event<CollectionEvent<E>>
{
   /**
    * The possible types of {@link CollectionEvent}s.
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

   /**
    * The type of the event
    * 
    * @return the type of the event
    * @see Type
    */
   Type getType();

   /**
    * Returns the elements that were added or removed
    * 
    * @return the elements that were added or removed
    */
   Collection<E> getElements();
}