package net.sf.javagimmicks.collections.event.cdi;

import java.util.Map;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventMapListener;
import net.sf.javagimmicks.collections.event.MapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventMap;

/**
 * An implementation of {@link EventMapListener} that re-fires the received
 * {@link MapEvent}s as {@link CDIMapEvent} via CDI.
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
    * @return the registered {@link CDIBridgeMapEventListener}
    */
   public static <K, V> CDIBridgeMapEventListener<K, V> install(final ObservableEventMap<K, V> map)
   {
      final CDIBridgeMapEventListener<K, V> result = new CDIBridgeMapEventListener<K, V>();

      map.addEventMapListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventMap} around the given {@link Map} and
    * registers a {@link CDIBridgeMapEventListener} within.
    * 
    * @param map
    *           the base {@link Map} to wrap
    * @return the CDI-enabled {@link ObservableEventMap}
    */
   public static <K, V> ObservableEventMap<K, V> createAndInstall(final Map<K, V> map)
   {
      final ObservableEventMap<K, V> result = new ObservableEventMap<K, V>(map);
      install(result);
      return result;
   }

   public CDIBridgeMapEventListener()
   {
      _eventHandle = buildEvent(CDIMapEvent.class);
   }

   @Override
   public void eventOccured(final MapEvent<K, V> event)
   {
      _eventHandle.fire(new CDIMapEvent(event));
   }
}
