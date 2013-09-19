package net.sf.javagimmicks.collections.event.cdi;

import java.util.NavigableMap;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableMapListener;
import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableMap;

/**
 * An implementation of {@link EventNavigableMapListener} that re-fires the
 * received {@link NavigableMapEvent}s as {@link CDINavigableMapEvent} via CDI.
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
    * @return the registered {@link CDIBridgeNavigableMapEventListener}
    */
   public static <K, V> CDIBridgeNavigableMapEventListener<K, V> install(final ObservableEventNavigableMap<K, V> map)
   {
      final CDIBridgeNavigableMapEventListener<K, V> result = new CDIBridgeNavigableMapEventListener<K, V>();

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
    * @return the CDI-enabled {@link ObservableEventNavigableMap}
    */
   public static <K, V> ObservableEventNavigableMap<K, V> createAndInstall(final NavigableMap<K, V> map)
   {
      final ObservableEventNavigableMap<K, V> result = new ObservableEventNavigableMap<K, V>(map);
      install(result);
      return result;
   }

   public CDIBridgeNavigableMapEventListener()
   {
      _eventHandle = buildEvent(CDINavigableMapEvent.class);
   }

   @Override
   public void eventOccured(final NavigableMapEvent<K, V> event)
   {
      _eventHandle.fire(new CDINavigableMapEvent(event));
   }
}
