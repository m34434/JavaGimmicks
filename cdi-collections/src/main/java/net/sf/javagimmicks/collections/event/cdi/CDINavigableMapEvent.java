package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.event.Observable;

/**
 * A CDI compatible wrapper around a {@link NavigableMapEvent}.
 * <p>
 * CDI event objects may not have type parameters, so the type information needs
 * to be erased for the wrapped {@link NavigableMapEvent}.
 */
public class CDINavigableMapEvent implements NavigableMapEvent<Object, Object>
{
   private final NavigableMapEvent<Object, Object> _origin;

   @SuppressWarnings("unchecked")
   CDINavigableMapEvent(final NavigableMapEvent<?, ?> origin)
   {
      _origin = (NavigableMapEvent<Object, Object>) origin;
   }

   /**
    * Provides access to the wrapped {@link NavigableMapEvent}
    * 
    * @return the wrapped {@link NavigableMapEvent}
    */
   public NavigableMapEvent<Object, Object> getWrappedEvent()
   {
      return _origin;
   }

   @Override
   public Observable<NavigableMapEvent<Object, Object>> getSource()
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
