package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.ObservableEventSet;
import net.sf.javagimmicks.collections.event.SetEvent;
import net.sf.javagimmicks.collections.event.SetEvent.Type;

public class CDISetEvent
{
   private final SetEvent<?> _origin;

   public CDISetEvent(final SetEvent<?> origin)
   {
      _origin = origin;
   }

   public Type getType()
   {
      return _origin.getType();
   }

   public Object getElement()
   {
      return _origin.getElement();
   }

   public ObservableEventSet<?> getSource()
   {
      return _origin.getSource();
   }

   public SetEvent<?> getOriginalEvent()
   {
      return _origin;
   }
}
