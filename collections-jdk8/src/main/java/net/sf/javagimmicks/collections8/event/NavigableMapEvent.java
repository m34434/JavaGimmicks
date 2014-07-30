package net.sf.javagimmicks.collections8.event;

import java.util.Map.Entry;
import java.util.NavigableMap;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link NavigableMap} - like
 * {@link ObservableEventNavigableMap}.
 */
public interface NavigableMapEvent<K, V> extends Event<NavigableMapEvent<K, V>>
{
   /**
    * The possible types of {@link NavigableMapEvent}s.
    */
   enum Type
   {
      /**
       * One ore more entries were added
       */
      ADDED,

      /**
       * One ore more entries were updated
       */
      UPDATED,

      /**
       * One or more entries were removed
       */
      REMOVED
   }

   /**
    * Returns the type of the event.
    * 
    * @return the type of the event
    * @see Type
    */
   Type getType();

   /**
    * Returns the key of the changed {@link Entry}.
    * 
    * @return the key of the changed {@link Entry}
    */
   K getKey();

   /**
    * Returns the value of a removed/added {@link Entry} in case of
    * {@link Type#REMOVED} or {@link Type#ADDED} - or the old value of an
    * updated {@link Entry} in case of {@link Type#UPDATED}.
    * 
    * @return the removed/added or old value of an update
    */
   V getValue();

   /**
    * In case of {@link Type#UPDATED}) returns the value of the updated
    * {@link Entry}.
    * 
    * @return the value of an updated {@link Entry}
    */
   V getNewValue();
}