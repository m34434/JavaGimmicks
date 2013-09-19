package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

/**
 * A CDI compatible wrapper around a {@link NavigableSetEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link NavigableSetEvent}.
 */
public class CDINavigableSetEvent implements NavigableSetEvent<Object>
{
   private final NavigableSetEvent<Object> _origin;

   /**
    * Create a new instance for the given {@link NavigableSetEvent}.
    * 
    * @param origin
    *           the original {@link NavigableSetEvent}
    */
   @SuppressWarnings("unchecked")
   public CDINavigableSetEvent(final NavigableSetEvent<?> origin)
   {
      _origin = (NavigableSetEvent<Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link NavigableSetEvent}
    * 
    * @return the wrapped {@link NavigableSetEvent}
    */
   public NavigableSetEvent<Object> getOriginalEvent()
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
   public ObservableEventNavigableSet<Object> getSource()
   {
      return _origin.getSource();
   }
}
