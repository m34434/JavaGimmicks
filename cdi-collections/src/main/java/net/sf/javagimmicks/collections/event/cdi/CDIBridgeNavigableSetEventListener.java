package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.NavigableSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableSetListener;
import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

/**
 * An implementation of {@link EventNavigableSetListener} that re-fires the
 * received {@link NavigableSetEvent}s as {@link CDINavigableSetEvent} via CDI.
 * 
 * @param <E>
 *           the element type of the treated events
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
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the registered {@link CDIBridgeNavigableSetEventListener}
    */
   public static <E> CDIBridgeNavigableSetEventListener<E> install(final ObservableEventNavigableSet<E> set,
         final Annotation... annotations)
   {
      final CDIBridgeNavigableSetEventListener<E> result = new CDIBridgeNavigableSetEventListener<E>(annotations);

      set.addEventListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventNavigableSet} around the given
    * {@link NavigableSet} and registers a
    * {@link CDIBridgeNavigableSetEventListener} within.
    * 
    * @param set
    *           the base {@link NavigableSet} to wrap
    * @param annotations
    *           the qualifier {@link Annotation}s that the fired events should
    *           have
    * @param <E>
    *           the element type of the treated events
    * @return the CDI-enabled {@link ObservableEventNavigableSet}
    */
   public static <E> ObservableEventNavigableSet<E> createAndInstall(final NavigableSet<E> set,
         final Annotation... annotations)
   {
      final ObservableEventNavigableSet<E> result = new ObservableEventNavigableSet<E>(set);
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
   public CDIBridgeNavigableSetEventListener(final Annotation... annotations)
   {
      _eventHandle = buildEvent(CDINavigableSetEvent.class, annotations);
   }

   @Override
   public void eventOccured(final NavigableSetEvent<E> event)
   {
      _eventHandle.fire(new CDINavigableSetEvent(event));
   }
}
