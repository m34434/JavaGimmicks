package net.sf.javagimmicks.collections.event.cdi;

import java.util.SortedMap;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSortedMapListener;
import net.sf.javagimmicks.collections.event.ObservableEventSortedMap;
import net.sf.javagimmicks.collections.event.SortedMapEvent;

public class CDIBridgeSortedMapEventListener<K, V> extends CDIBrigeBase implements EventSortedMapListener<K, V>
{
   private final Event<CDISortedMapEvent> _eventHandle;

   public static <K, V> CDIBridgeSortedMapEventListener<K, V> install(final ObservableEventSortedMap<K, V> set)
   {
      final CDIBridgeSortedMapEventListener<K, V> result = new CDIBridgeSortedMapEventListener<K, V>();

      set.addEventSortedMapListener(result);

      return result;
   }

   public static <K, V> ObservableEventSortedMap<K, V> createAndInstall(final SortedMap<K, V> set)
   {
      final ObservableEventSortedMap<K, V> result = new ObservableEventSortedMap<K, V>(set);
      install(result);
      return result;
   }

   public CDIBridgeSortedMapEventListener()
   {
      _eventHandle = buildEvent(CDISortedMapEvent.class);
   }

   @Override
   public void eventOccured(final SortedMapEvent<K, V> event)
   {
      _eventHandle.fire(new CDISortedMapEvent(event));
   }
}
