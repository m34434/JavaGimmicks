package net.sf.javagimmicks.collections.event.cdi;

import java.util.Collection;

import javax.enterprise.event.Event;

import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.EventCollectionListener;
import net.sf.javagimmicks.collections.event.ObservableEventCollection;

/**
 * An implementation of {@link EventCollectionListener} that re-fires the
 * received {@link CollectionEvent}s as {@link CDICollectionEvent} via CDI.
 */
public class CDIBridgeCollectionEventListener<E> extends CDIBrigeBase implements EventCollectionListener<E>
{
   private final Event<CDICollectionEvent> _eventHandle;

   /**
    * Registers a {@link CDIBridgeCollectionEventListener} within the given
    * {@link ObservableEventCollection}.
    * 
    * @param collection
    *           the {@link ObservableEventCollection} where to register the
    *           {@link CDIBridgeCollectionEventListener}
    * @return the registered {@link CDIBridgeCollectionEventListener}
    */
   public static <E> CDIBridgeCollectionEventListener<E> install(final ObservableEventCollection<E> collection)
   {
      final CDIBridgeCollectionEventListener<E> result = new CDIBridgeCollectionEventListener<E>();

      collection.addEventCollectionListener(result);

      return result;
   }

   /**
    * Wraps a new {@link ObservableEventCollection} around the given
    * {@link Collection} and registers a
    * {@link CDIBridgeCollectionEventListener} within.
    * 
    * @param collection
    *           the base {@link Collection} to wrap
    * @return the CDI-enabled {@link ObservableEventCollection}
    */
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
