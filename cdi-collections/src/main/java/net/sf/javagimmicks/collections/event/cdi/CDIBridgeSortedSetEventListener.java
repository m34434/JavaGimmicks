package net.sf.javagimmicks.collections.event.cdi;

import java.util.SortedSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSortedSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;

/**
 * An implementation of {@link EventSortedSetListener} that re-fires the
 * received {@link SortedSetEvent}s as {@link CDISortedSetEvent} via CDI.
 */
public class CDIBridgeSortedSetEventListener<E> extends CDIBrigeBase implements EventSortedSetListener<E>
{
   private final Event<CDISortedSetEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeSortedSetEventListener} within the given
    * {@link ObservableEventSortedSet}.
    * 
    * @param set
    *           the {@link ObservableEventSortedSet} where to register the
    *           {@link CDIBridgeSortedSetEventListener}
    * @return the registered {@link CDIBridgeSortedSetEventListener}
    */
   public static <E> CDIBridgeSortedSetEventListener<E> install(final ObservableEventSortedSet<E> set)
   {
      final CDIBridgeSortedSetEventListener<E> result = new CDIBridgeSortedSetEventListener<E>();

      set.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventSortedSet} around the given
    * {@link SortedSet} and registers a {@link CDIBridgeSortedSetEventListener}
    * within.
    * 
    * @param set
    *           the base {@link SortedSet} to wrap
    * @return the CDI-enabled {@link ObservableEventSortedSet}
    */
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
