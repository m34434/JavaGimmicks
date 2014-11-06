package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.NavigableMap;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableMapListener;
import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableMap;

/**
 * An implementation of {@link EventNavigableMapListener} that re-fires the
 * received {@link NavigableMapEvent}s as {@link CDINavigableMapEvent} via CDI.
 * 
 * @param <K>
 *           the key element type of the treated events
 * @param <V>
 *           the value element type of the treated events
 */
public class CDIBridgeNavigableMapEventListener<K, V> extends CDIBrigeBase implements EventNavigableMapListener<K, V>
{
   private final Event<CDINavigableMapEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeNavigableMapEventListener} within the given
    * {@link ObservableEventNavigableMap}.
    * 
    * @param map
    *           the {@link ObservableEventNavigableMap} where to register the
    *           {@link CDIBridgeNavigableMapEventListener}
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the registered {@link CDIBridgeNavigableMapEventListener}
    */
   public static <K, V> CDIBridgeNavigableMapEventListener<K, V> install(final ObservableEventNavigableMap<K, V> map,
         final Annotation... annotations)
   {
      final CDIBridgeNavigableMapEventListener<K, V> result = new CDIBridgeNavigableMapEventListener<K, V>(annotations);

      map.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventNavigableMap} around the given
    * {@link NavigableMap} and registers a
    * {@link CDIBridgeNavigableMapEventListener} within.
    * 
    * @param map
    *           the base {@link NavigableMap} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <K>
    *           the key element type of the treated events
    * @param <V>
    *           the value element type of the treated events
    * @return the CDI-enabled {@link ObservableEventNavigableMap}
    */
   public static <K, V> ObservableEventNavigableMap<K, V> createAndInstall(final NavigableMap<K, V> map,
         final Annotation... annotations)
   {
      final ObservableEventNavigableMap<K, V> result = new ObservableEventNavigableMap<K, V>(map);
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
   public CDIBridgeNavigableMapEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDINavigableMapEvent.class, annotations);
   }

   @Override
   public void eventOccured(final NavigableMapEvent<K, V> event)
   {
      _eventHandle.fire(new CDINavigableMapEvent(event));
   }
}
