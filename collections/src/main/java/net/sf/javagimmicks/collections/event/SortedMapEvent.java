package net.sf.javagimmicks.collections.event;

import java.util.Map.Entry;
import java.util.SortedMap;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.Observable;

/**
 * Represents a change in a {@link Observable} {@link SortedMap} - e.g.
 * {@link ObservableEventSortedMap}
 */
public interface SortedMapEvent<K, V> extends Event<SortedMapEvent<K, V>, EventSortedMapListener<K, V>>
{
   /**
    * The possible types of {@link SortedMapEvent}s.
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