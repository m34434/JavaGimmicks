package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.NavigableMapEvent;
import net.sf.javagimmicks.collections.event.NavigableMapEvent.Type;
import net.sf.javagimmicks.collections.event.ObservableEventNavigableMap;

public class CDINavigableMapEvent
{
   private final NavigableMapEvent<?, ?> _origin;

   public CDINavigableMapEvent(final NavigableMapEvent<?, ?> origin)
   {
      _origin = origin;
   }

   public ObservableEventNavigableMap<?, ?> getSource()
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

   public NavigableMapEvent<?, ?> getOriginalEvent()
   {
      return _origin;
   }
}
