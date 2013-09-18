package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.ObservableEventSortedSet;
import net.sf.javagimmicks.collections.event.SortedSetEvent;
import net.sf.javagimmicks.collections.event.SortedSetEvent.Type;

public class CDISortedSetEvent
{
   private final SortedSetEvent<?> _origin;

   public CDISortedSetEvent(final SortedSetEvent<?> origin)
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

   public ObservableEventSortedSet<?> getSource()
   {
      return _origin.getSource();
   }

   public SortedSetEvent<?> getOriginalEvent()
   {
      return _origin;
   }
}
