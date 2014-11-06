package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.SortedSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSortedSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;

/**
 * An implementation of {@link EventSortedSetListener} that re-fires the
 * received {@link SortedSetEvent}s as {@link CDISortedSetEvent} via CDI.
 * 
 * @param <E>
 *           the element type of the treated events
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
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the registered {@link CDIBridgeSortedSetEventListener}
    */
   public static <E> CDIBridgeSortedSetEventListener<E> install(final ObservableEventSortedSet<E> set,
         final Annotation... annotations)
   {
      final CDIBridgeSortedSetEventListener<E> result = new CDIBridgeSortedSetEventListener<E>(annotations);

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
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the CDI-enabled {@link ObservableEventSortedSet}
    */
   public static <E> ObservableEventSortedSet<E> createAndInstall(final SortedSet<E> set,
         final Annotation... annotations)
   {
      final ObservableEventSortedSet<E> result = new ObservableEventSortedSet<E>(set);
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
   public CDIBridgeSortedSetEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDISortedSetEvent.class, annotations);
   }

   @Override
   public void eventOccured(final SortedSetEvent<E> event)
   {
      _eventHandle.fire(new CDISortedSetEvent(event));
   }
}
