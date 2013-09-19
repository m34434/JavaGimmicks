package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.ObservableEventSortedMap;
import net.sf.javagimmicks.collections.event.SortedMapEvent;

/**
 * A CDI compatible wrapper around a {@link SortedMapEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link SortedMapEvent}.
 */
public class CDISortedMapEvent implements SortedMapEvent<Object, Object>
{
   private final SortedMapEvent<Object, Object> _origin;

   @SuppressWarnings("unchecked")
   CDISortedMapEvent(final SortedMapEvent<?, ?> origin)
   {
      _origin = (SortedMapEvent<Object, Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link SortedMapEvent}
    * 
    * @return the wrapped {@link SortedMapEvent}
    */
   public SortedMapEvent<Object, Object> getWrappedEvent()
   {
      return _origin;
   }

   @Override
   public ObservableEventSortedMap<Object, Object> getSource()
   {
      return _origin.getSource();
   }

   @Override
   public Type getType()
   {
      return _origin.getType();
   }

   @Override
   public Object getKey()
   {
      return _origin.getKey();
   }

   @Override
   public Object getValue()
   {
      return _origin.getValue();
   }

   @Override
   public Object getNewValue()
   {
      return _origin.getNewValue();
   }
}
