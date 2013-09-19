package net.sf.javagimmicks.collections.event.cdi;

import java.util.List;

import net.sf.javagimmicks.collections.event.ListEvent;
import net.sf.javagimmicks.collections.event.ObservableEventList;

/**
 * A CDI compatible wrapper around a {@link ListEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link ListEvent}.
 */
public class CDIListEvent implements ListEvent<Object>
{
   private final ListEvent<Object> _origin;

   /**
    * Create a new instance for the given {@link ListEvent}.
    * 
    * @param origin
    *           the original {@link ListEvent}
    */
   @SuppressWarnings("unchecked")
   public CDIListEvent(final ListEvent<?> origin)
   {
      _origin = (ListEvent<Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link ListEvent}
    * 
    * @return the wrapped {@link ListEvent}
    */
   public ListEvent<Object> getWrappedEvent()
   {
      return _origin;
   }

   @Override
   public Type getType()
   {
      return _origin.getType();
   }

   @Override
   public int getFromIndex()
   {
      return _origin.getFromIndex();
   }

   @Override
   public int getToIndex()
   {
      return _origin.getToIndex();
   }

   @Override
   public List<Object> getElements()
   {
      return _origin.getElements();
   }

   @Override
   public List<Object> getNewElements()
   {
      return _origin.getNewElements();
   }

   @Override
   public ObservableEventList<Object> getSource()
   {
      return _origin.getSource();
   }
}
