package net.sf.javagimmicks.collections.event.cdi;

import java.util.Collection;

import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.EventCollectionListener;
import net.sf.javagimmicks.event.Observable;

/**
 * A CDI compatible wrapper around a {@link CollectionEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link CollectionEvent}.
 */
public class CDICollectionEvent implements CollectionEvent<Object>
{
   private final CollectionEvent<Object> _origin;

   @SuppressWarnings("unchecked")
   CDICollectionEvent(final CollectionEvent<?> origin)
   {
      _origin = (CollectionEvent<Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link CollectionEvent}
    * 
    * @return the wrapped {@link CollectionEvent}
    */
   public CollectionEvent<Object> getWrappedEvent()
   {
      return _origin;
   }

   @Override
   public Type getType()
   {
      return _origin.getType();
   }

   @Override
   public Collection<Object> getElements()
   {
      return _origin.getElements();
   }

   @Override
   public Observable<CollectionEvent<Object>, EventCollectionListener<Object>> getSource()
   {
      return _origin.getSource();
   }
}
