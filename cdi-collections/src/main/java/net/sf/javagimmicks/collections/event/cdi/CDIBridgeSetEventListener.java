package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSet;
import net.sf.javagimmicks.collections.event.SetEvent;

/**
 * An implementation of {@link EventSetListener} that re-fires the received
 * {@link SetEvent}s as {@link CDISetEvent} via CDI.
 * 
 * @param <E>
 *           the element type of the treated events
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
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the registered {@link CDIBridgeSetEventListener}
    */
   public static <E> CDIBridgeSetEventListener<E> install(final ObservableEventSet<E> set,
         final Annotation... annotations)
   {
      final CDIBridgeSetEventListener<E> result = new CDIBridgeSetEventListener<E>(annotations);

      set.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventSet} around the given {@link Set} and
    * registers a {@link CDIBridgeSetEventListener} within.
    * 
    * @param set
    *           the base {@link Set} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the CDI-enabled {@link ObservableEventSet}
    */
   public static <E> ObservableEventSet<E> createAndInstall(final Set<E> set, final Annotation... annotations)
   {
      final ObservableEventSet<E> result = new ObservableEventSet<E>(set);
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
   public CDIBridgeSetEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDISetEvent.class, annotations);
   }

   @Override
   public void eventOccured(final SetEvent<E> event)
   {
      _eventHandle.fire(new CDISetEvent(event));
   }
}
