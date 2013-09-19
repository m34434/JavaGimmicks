package net.sf.javagimmicks.collections.event;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link Map} - e.g.
 * {@link ObservableEventMap}
 */
public interface MapEvent<K, V> extends Event<MapEvent<K, V>, EventMapListener<K, V>>
{
   /**
    * The possible types of {@link MapEvent}s.
    */
   enum Type
   {
      /**
       * An {@link Entry} was added
       */
      ADDED,

      /**
       * An {@link Entry} was updated
       */
      UPDATED,

      /**
       * An {@link Entry} was removed
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