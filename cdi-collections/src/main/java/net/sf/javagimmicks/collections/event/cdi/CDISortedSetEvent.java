package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;

/**
 * A CDI compatible wrapper around a {@link SortedSetEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link SortedSetEvent}.
 */
public class CDISortedSetEvent implements SortedSetEvent<Object>
{
   private final SortedSetEvent<Object> _origin;

   @SuppressWarnings("unchecked")
   CDISortedSetEvent(final SortedSetEvent<?> origin)
   {
      _origin = (SortedSetEvent<Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link SortedSetEvent}
    * 
    * @return the wrapped {@link SortedSetEvent}
    */
   public SortedSetEvent<Object> getOriginalEvent()
   {
      return _origin;
   }

   @Override
   public Type getType()
   {
      return _origin.getType();
   }

   @Override
   public Object getElement()
   {
      return _origin.getElement();
   }

   @Override
   public ObservableEventSortedSet<Object> getSource()
   {
      return _origin.getSource();
   }
}
