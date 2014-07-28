package net.sf.javagimmicks.collections.event;

import java.util.SortedSet;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link SortedSet} - like
 * {@link ObservableEventSortedSet}.
 */
public interface SortedSetEvent<E> extends Event<SortedSetEvent<E>>
{
   /**
    * The possible types of {@link SortedSetEvent}s.
    */
   enum Type
   {
      /**
       * An element was added
       */
      ADDED,

      /**
       * An already contained element was added again
       */
      READDED,

      /**
       * An element was removed
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
    * Returns the element that was (re-)added or removed
    * 
    * @return the element that was (re-)added or removed
    */
   E getElement();
}