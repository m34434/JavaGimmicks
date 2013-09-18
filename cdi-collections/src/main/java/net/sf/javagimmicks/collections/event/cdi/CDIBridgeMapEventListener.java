package net.sf.javagimmicks.collections.event.cdi;

import java.util.Map;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventMapListener;
import net.sf.javagimmicks.collections.event.MapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventMap;

public class CDIBridgeMapEventListener<K, V> extends CDIBrigeBase implements EventMapListener<K, V>
{
   private final Event<CDIMapEvent> _eventHandle;

   public static <K, V> CDIBridgeMapEventListener<K, V> install(final ObservableEventMap<K, V> set)
   {
      final CDIBridgeMapEventListener<K, V> result = new CDIBridgeMapEventListener<K, V>();

      set.addEventMapListener(result);

      return result;
   }

   public static <K, V> ObservableEventMap<K, V> createAndInstall(final Map<K, V> set)
   {
      final ObservableEventMap<K, V> result = new ObservableEventMap<K, V>(set);
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
