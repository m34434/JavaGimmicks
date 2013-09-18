package net.sf.javagimmicks.collections.event.cdi;

import java.util.List;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.EventListListener;
import net.sf.javagimmicks.collections.event.ListEvent;
import net.sf.javagimmicks.collections.event.ObservableEventList;

public class CDIBridgeListEventListener<E> extends CDIBrigeBase implements EventListListener<E>
{
   private final Event<CDIListEvent> _eventHandle;

   public static <E> CDIBridgeListEventListener<E> install(final ObservableEventList<E> set)
   {
      final CDIBridgeListEventListener<E> result = new CDIBridgeListEventListener<E>();

      set.addEventListListener(result);

      return result;
   }

   public static <E> ObservableEventList<E> createAndInstall(final List<E> set)
   {
      final ObservableEventList<E> result = new ObservableEventList<E>(set);
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
