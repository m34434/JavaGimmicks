package net.sf.javagimmicks.collections.event.cdi;

import java.util.Set;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventSetListener;
import net.sf.javagimmicks.collections.event.ObservableEventSet;
import net.sf.javagimmicks.collections.event.SetEvent;

public class CDIBridgeSetEventListener<E> extends CDIBrigeBase implements EventSetListener<E>
{
   private final Event<CDISetEvent> _eventHandle;

   public static <E> CDIBridgeSetEventListener<E> install(final ObservableEventSet<E> set)
   {
      final CDIBridgeSetEventListener<E> result = new CDIBridgeSetEventListener<E>();

      set.addEventSetListener(result);

      return result;
   }

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
