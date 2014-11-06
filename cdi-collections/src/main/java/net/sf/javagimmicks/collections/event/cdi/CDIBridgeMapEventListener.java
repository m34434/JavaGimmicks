package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventMapListener;
import net.sf.javagimmicks.collections.event.MapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventMap;

/**
 * An implementation of {@link EventMapListener} that re-fires the received
 * {@link MapEvent}s as {@link CDIMapEvent} via CDI.
 * 
 * @param <K>
 *           the key element type of the treated events
 * @param <V>
 *           the value element type of the treated events
 */
public class CDIBridgeMapEventListener<K, V> extends CDIBrigeBase implements EventMapListener<K, V>
{
   private final Event<CDIMapEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeMapEventListener} within the given
    * {@link ObservableEventMap}.
    * 
    * @param map
    *           the {@link ObservableEventMap} where to register the
    *           {@link CDIBridgeMapEventListener}
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the registered {@link CDIBridgeMapEventListener}
    */
   public static <K, V> CDIBridgeMapEventListener<K, V> install(final ObservableEventMap<K, V> map,
         final Annotation... annotations)
   {
      final CDIBridgeMapEventListener<K, V> result = new CDIBridgeMapEventListener<K, V>(annotations);

      map.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventMap} around the given {@link Map} and
    * registers a {@link CDIBridgeMapEventListener} within.
    * 
    * @param map
    *           the base {@link Map} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the CDI-enabled {@link ObservableEventMap}
    */
   public static <K, V> ObservableEventMap<K, V> createAndInstall(final Map<K, V> map, final Annotation... annotations)
   {
      final ObservableEventMap<K, V> result = new ObservableEventMap<K, V>(map);
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
   public CDIBridgeMapEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDIMapEvent.class, annotations);
   }

   @Override
   public void eventOccured(final MapEvent<K, V> event)
   {
      _eventHandle.fire(new CDIMapEvent(event));
   }
}
