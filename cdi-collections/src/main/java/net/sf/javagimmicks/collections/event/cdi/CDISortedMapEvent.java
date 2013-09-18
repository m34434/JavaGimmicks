package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.ObservableEventSortedMap;
import net.sf.javagimmicks.collections.event.SortedMapEvent;
import net.sf.javagimmicks.collections.event.SortedMapEvent.Type;

public class CDISortedMapEvent
{
   private final SortedMapEvent<?, ?> _origin;

   public CDISortedMapEvent(final SortedMapEvent<?, ?> origin)
   {
      _origin = origin;
   }

   public ObservableEventSortedMap<?, ?> getSource()
   {
      return _origin.getSource();
   }

   public Type getType()
   {
      return _origin.getType();
   }

   public Object getKey()
   {
      return _origin.getKey();
   }

   public Object getValue()
   {
      return _origin.getValue();
   }

   public Object getNewValue()
   {
      return _origin.getNewValue();
   }

   public SortedMapEvent<?, ?> getOriginalEvent()
   {
      return _origin;
   }
}
