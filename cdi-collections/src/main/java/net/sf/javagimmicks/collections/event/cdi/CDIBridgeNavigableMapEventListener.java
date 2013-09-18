package net.sf.javagimmicks.collections.event.cdi;

import java.util.NavigableMap;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableMapListener;
import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableMap;

public class CDIBridgeNavigableMapEventListener<K, V> extends CDIBrigeBase implements EventNavigableMapListener<K, V>
{
   private final Event<CDINavigableMapEvent> _eventHandle;

   public static <K, V> CDIBridgeNavigableMapEventListener<K, V> install(final ObservableEventNavigableMap<K, V> set)
   {
      final CDIBridgeNavigableMapEventListener<K, V> result = new CDIBridgeNavigableMapEventListener<K, V>();

      set.addEventNavigableMapListener(result);

      return result;
   }

   public static <K, V> ObservableEventNavigableMap<K, V> createAndInstall(final NavigableMap<K, V> set)
   {
      final ObservableEventNavigableMap<K, V> result = new ObservableEventNavigableMap<K, V>(set);
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
