package net.sf.javagimmicks.collections.event.cdi;

import java.util.NavigableSet;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventNavigableSetListener;
import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

public class CDIBridgeNavigableSetEventListener<E> extends CDIBrigeBase implements EventNavigableSetListener<E>
{
   private final Event<CDINavigableSetEvent> _eventHandle;

   public static <E> CDIBridgeNavigableSetEventListener<E> install(final ObservableEventNavigableSet<E> collection)
   {
      final CDIBridgeNavigableSetEventListener<E> result = new CDIBridgeNavigableSetEventListener<E>();

      collection.addEventNavigableSetListener(result);

      return result;
   }

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
