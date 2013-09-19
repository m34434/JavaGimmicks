package net.sf.javagimmicks.collections.event.cdi;

import java.util.NavigableSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableSetListener;
import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

/**
 * An implementation of {@link EventNavigableSetListener} that re-fires the
 * received {@link NavigableSetEvent}s as {@link CDINavigableSetEvent} via CDI.
 */
public class CDIBridgeNavigableSetEventListener<E> extends CDIBrigeBase implements EventNavigableSetListener<E>
{
   private final Event<CDINavigableSetEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeNavigableSetEventListener} within the given
    * {@link ObservableEventNavigableSet}.
    * 
    * @param set
    *           the {@link ObservableEventNavigableSet} where to register the
    *           {@link CDIBridgeNavigableSetEventListener}
    * @return the registered {@link CDIBridgeNavigableSetEventListener}
    */
   public static <E> CDIBridgeNavigableSetEventListener<E> install(final ObservableEventNavigableSet<E> set)
   {
      final CDIBridgeNavigableSetEventListener<E> result = new CDIBridgeNavigableSetEventListener<E>();

      set.addEventNavigableSetListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventNavigableSet} around the given
    * {@link NavigableSet} and registers a
    * {@link CDIBridgeNavigableSetEventListener} within.
    * 
    * @param set
    *           the base {@link NavigableSet} to wrap
    * @return the CDI-enabled {@link ObservableEventNavigableSet}
    */
   public static <E> ObservableEventNavigableSet<E> createAndInstall(final NavigableSet<E> set)
   {
      final ObservableEventNavigableSet<E> result = new ObservableEventNavigableSet<E>(set);
      install(result);
      return result;
   }

   public CDIBridgeNavigableSetEventListener()
   {
      _eventHandle = buildEvent(CDINavigableSetEvent.class);
   }

   @Override
   public void eventOccured(final NavigableSetEvent<E> event)
   {
      _eventHandle.fire(new CDINavigableSetEvent(event));
   }
}
