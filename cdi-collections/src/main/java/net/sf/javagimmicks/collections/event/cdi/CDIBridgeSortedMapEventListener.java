package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.SortedMap;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSortedMapListener;
import net.sf.javagimmicks.collections.event.ObservableEventSortedMap;
import net.sf.javagimmicks.collections.event.SortedMapEvent;

/**
 * An implementation of {@link EventSortedMapListener} that re-fires the
 * received {@link SortedMapEvent}s as {@link CDISortedMapEvent} via CDI.
 * 
 * @param <K>
 *           the key element type of the treated events
 * @param <V>
 *           the value element type of the treated events
 */
public class CDIBridgeSortedMapEventListener<K, V> extends CDIBrigeBase implements EventSortedMapListener<K, V>
{
   private final Event<CDISortedMapEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeSortedMapEventListener} within the given
    * {@link ObservableEventSortedMap}.
    * 
    * @param map
    *           the {@link ObservableEventSortedMap} where to register the
    *           {@link CDIBridgeSortedMapEventListener}
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the registered {@link CDIBridgeSortedMapEventListener}
    */
   public static <K, V> CDIBridgeSortedMapEventListener<K, V> install(final ObservableEventSortedMap<K, V> map,
         final Annotation... annotations)
   {
      final CDIBridgeSortedMapEventListener<K, V> result = new CDIBridgeSortedMapEventListener<K, V>(annotations);

      map.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventSortedMap} around the given
    * {@link SortedMap} and registers a {@link CDIBridgeSortedMapEventListener}
    * within.
    * 
    * @param map
    *           the base {@link SortedMap} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the CDI-enabled {@link ObservableEventSortedMap}
    */
   public static <K, V> ObservableEventSortedMap<K, V> createAndInstall(final SortedMap<K, V> map,
         final Annotation... annotations)
   {
      final ObservableEventSortedMap<K, V> result = new ObservableEventSortedMap<K, V>(map);
      install(result, annotations);
      return result;
   }

   /**
    * Creates a new instance for the given qualifier {@link Annotation}s.
    * 
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    */
   public CDIBridgeSortedMapEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDISortedMapEvent.class, annotations);
   }

   @Override
   public void eventOccured(final SortedMapEvent<K, V> event)
   {
      _eventHandle.fire(new CDISortedMapEvent(event));
   }
}
