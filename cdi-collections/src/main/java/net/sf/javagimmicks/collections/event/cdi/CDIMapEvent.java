package net.sf.javagimmicks.collections.event.cdi;

import net.sf.javagimmicks.collections.event.MapEvent;
import net.sf.javagimmicks.collections.event.MapEvent.Type;
import net.sf.javagimmicks.collections.event.ObservableEventMap;

public class CDIMapEvent
{
   private final MapEvent<?, ?> _origin;

   public CDIMapEvent(final MapEvent<?, ?> origin)
   {
      _origin = origin;
   }

   public ObservableEventMap<?, ?> getSource()
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

   public MapEvent<?, ?> getOriginalEvent()
   {
      return _origin;
   }
}
