package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.NavigableSetEvent;
import net.sf.javagimmicks.collections.event.NavigableSetEvent.Type;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableSet;

public class CDINavigableSetEvent
{
   private final NavigableSetEvent<?> _origin;

   public CDINavigableSetEvent(final NavigableSetEvent<?> origin)
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

   public ObservableEventNavigableSet<?> getSource()
   {
      return _origin.getSource();
   }

   public NavigableSetEvent<?> getOriginalEvent()
   {
      return _origin;
   }
}
