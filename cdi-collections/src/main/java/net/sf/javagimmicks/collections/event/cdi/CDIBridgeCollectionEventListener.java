package net.sf.javagimmicks.collections.event.cdi;

import java.util.Collection;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.EventCollectionListener;
import net.sf.javagimmicks.collections.event.ObservableEventCollection;

public class CDIBridgeCollectionEventListener<E> extends CDIBrigeBase implements EventCollectionListener<E>
{
   private final Event<CDICollectionEvent> _eventHandle;

   public static <E> CDIBridgeCollectionEventListener<E> install(final ObservableEventCollection<E> collection)
   {
      final CDIBridgeCollectionEventListener<E> result = new CDIBridgeCollectionEventListener<E>();

      collection.addEventCollectionListener(result);

      return result;
   }

   public static <E> ObservableEventCollection<E> createAndInstall(final Collection<E> collection)
   {
      final ObservableEventCollection<E> result = new ObservableEventCollection<E>(collection);
      install(result);
      return result;
   }

   public CDIBridgeCollectionEventListener()
   {
      _eventHandle = buildEvent(CDICollectionEvent.class);
   }

   @Override
   public void eventOccured(final CollectionEvent<E> event)
   {
      _eventHandle.fire(new CDICollectionEvent(event));
   }
}
