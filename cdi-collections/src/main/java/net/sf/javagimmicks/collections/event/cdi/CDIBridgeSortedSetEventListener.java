package net.sf.javagimmicks.collections.event.cdi;

import java.util.SortedSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSortedSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;

public class CDIBridgeSortedSetEventListener<E> extends CDIBrigeBase implements EventSortedSetListener<E>
{
   private final Event<CDISortedSetEvent> _eventHandle;

   public static <E> CDIBridgeSortedSetEventListener<E> install(final ObservableEventSortedSet<E> set)
   {
      final CDIBridgeSortedSetEventListener<E> result = new CDIBridgeSortedSetEventListener<E>();

      set.addEventSortedSetListener(result);

      return result;
   }

   public static <E> ObservableEventSortedSet<E> createAndInstall(final SortedSet<E> set)
   {
      final ObservableEventSortedSet<E> result = new ObservableEventSortedSet<E>(set);
      install(result);
      return result;
   }

   public CDIBridgeSortedSetEventListener()
   {
      _eventHandle = buildEvent(CDISortedSetEvent.class);
   }

   @Override
   public void eventOccured(final SortedSetEvent<E> event)
   {
      _eventHandle.fire(new CDISortedSetEvent(event));
   }
}
