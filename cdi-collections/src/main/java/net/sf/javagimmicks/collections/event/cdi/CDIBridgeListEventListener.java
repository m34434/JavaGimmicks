package net.sf.javagimmicks.collections.event.cdi;

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
    * @return the registered {@link CDIBridgeListEventListener}
    */
   public static <E> CDIBridgeListEventListener<E> install(final ObservableEventList<E> list)
   {
      final CDIBridgeListEventListener<E> result = new CDIBridgeListEventListener<E>();

      list.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventList} around the given {@link List} and
    * registers a {@link CDIBridgeListEventListener} within.
    * 
    * @param list
    *           the base {@link List} to wrap
    * @return the CDI-enabled {@link ObservableEventList}
    */
   public static <E> ObservableEventList<E> createAndInstall(final List<E> list)
   {
      final ObservableEventList<E> result = new ObservableEventList<E>(list);
      install(result);
      return result;
   }

   public CDIBridgeListEventListener()
   {
      _eventHandle = buildEvent(CDIListEvent.class);
   }

   @Override
   public void eventOccured(final ListEvent<E> event)
   {
      _eventHandle.fire(new CDIListEvent(event));
   }
}
