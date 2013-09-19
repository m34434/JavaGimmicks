package net.sf.javagimmicks.collections.event;

import java.util.NavigableSet;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link NavigableSet} - e.g.
 * {@link ObservableEventSortedSet}.
 */
public interface NavigableSetEvent<E> extends Event<NavigableSetEvent<E>, EventNavigableSetListener<E>>
{
   /**
    * The possible types of {@link NavigableSetEvent}s.
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