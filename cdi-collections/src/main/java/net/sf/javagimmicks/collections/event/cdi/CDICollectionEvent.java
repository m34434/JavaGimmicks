package net.sf.javagimmicks.collections.event.cdi;

import java.util.Collection;

import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.CollectionEvent.Type;
import net.sf.javagimmicks.collections.event.ObservableEventCollection;

public class CDICollectionEvent
{
   private final CollectionEvent<?> _origin;

   public CDICollectionEvent(final CollectionEvent<?> origin)
   {
      _origin = origin;
   }

   public Type getType()
   {
      return _origin.getType();
   }

   public Collection<?> getElements()
   {
      return _origin.getElements();
   }

   public ObservableEventCollection<?> getSource()
   {
      return _origin.getSource();
   }

   public CollectionEvent<?> getOriginalEvent()
   {
      return _origin;
   }
}
