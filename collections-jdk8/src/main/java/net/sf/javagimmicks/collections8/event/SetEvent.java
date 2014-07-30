package net.sf.javagimmicks.collections8.event;

import java.util.Set;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link Set} - like
 * {@link ObservableEventSet}.
 */
public interface SetEvent<E> extends Event<SetEvent<E>>
{
   /**
    * The possible types of {@link SetEvent}s.
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