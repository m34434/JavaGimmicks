package net.sf.javagimmicks.collections.event.cdi;

import java.util.Set;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSet;
import net.sf.javagimmicks.collections.event.SetEvent;

/**
 * An implementation of {@link EventSetListener} that re-fires the received
 * {@link SetEvent}s as {@link CDISetEvent} via CDI.
 */
public class CDIBridgeSetEventListener<E> extends CDIBrigeBase implements EventSetListener<E>
{
   private final Event<CDISetEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeSetEventListener} within the given
    * {@link ObservableEventSet}.
    * 
    * @param set
    *           the {@link ObservableEventSet} where to register the
    *           {@link CDIBridgeSetEventListener}
    * @return the registered {@link CDIBridgeSetEventListener}
    */
   public static <E> CDIBridgeSetEventListener<E> install(final ObservableEventSet<E> set)
   {
      final CDIBridgeSetEventListener<E> result = new CDIBridgeSetEventListener<E>();

      set.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventSet} around the given {@link Set} and
    * registers a {@link CDIBridgeSetEventListener} within.
    * 
    * @param set
    *           the base {@link Set} to wrap
    * @return the CDI-enabled {@link ObservableEventSet}
    */
   public static <E> ObservableEventSet<E> createAndInstall(final Set<E> set)
   {
      final ObservableEventSet<E> result = new ObservableEventSet<E>(set);
      install(result);
      return result;
   }

   public CDIBridgeSetEventListener()
   {
      _eventHandle = buildEvent(CDISetEvent.class);
   }

   @Override
   public void eventOccured(final SetEvent<E> event)
   {
      _eventHandle.fire(new CDISetEvent(event));
   }
}
