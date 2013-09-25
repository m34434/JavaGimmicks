package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventListListener;
import net.sf.javagimmicks.collections.event.ListEvent;
import net.sf.javagimmicks.collections.event.ObservableEventList;

/**
 * An implementation of {@link EventListListener} that re-fires the received
 * {@link ListEvent}s as {@link CDIListEvent} via CDI.
 */
public class CDIBridgeListEventListener<E> extends CDIBrigeBase implements EventListListener<E>
{
   private final Event<CDIListEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeListEventListener} within the given
    * {@link ObservableEventList}.
    * 
    * @param list
    *           the {@link ObservableEventList} where to register the
    *           {@link CDIBridgeListEventListener}
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @return the registered {@link CDIBridgeListEventListener}
    */
   public static <E> CDIBridgeListEventListener<E> install(final ObservableEventList<E> list,
         final Annotation... annotations)
   {
      final CDIBridgeListEventListener<E> result = new CDIBridgeListEventListener<E>(annotations);

      list.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventList} around the given {@link List} and
    * registers a {@link CDIBridgeListEventListener} within.
    * 
    * @param list
    *           the base {@link List} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @return the CDI-enabled {@link ObservableEventList}
    */
   public static <E> ObservableEventList<E> createAndInstall(final List<E> list, final Annotation... annotations)
   {
      final ObservableEventList<E> result = new ObservableEventList<E>(list);
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
   public CDIBridgeListEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDIListEvent.class, annotations);
   }

   @Override
   public void eventOccured(final ListEvent<E> event)
   {
      _eventHandle.fire(new CDIListEvent(event));
   }
}
