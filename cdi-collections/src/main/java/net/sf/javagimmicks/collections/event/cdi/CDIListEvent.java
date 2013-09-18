package net.sf.javagimmicks.collections.event.cdi;

import java.util.List;

import net.sf.javagimmicks.collections.event.ListEvent;
import net.sf.javagimmicks.collections.event.ListEvent.Type;
import net.sf.javagimmicks.collections.event.ObservableEventList;

public class CDIListEvent
{
   private final ListEvent<?> _origin;

   public CDIListEvent(final ListEvent<?> origin)
   {
      _origin = origin;
   }

   public Type getType()
   {
      return _origin.getType();
   }

   public int getFromIndex()
   {
      return _origin.getFromIndex();
   }

   public int getToIndex()
   {
      return _origin.getToIndex();
   }

   public List<?> getElements()
   {
      return _origin.getElements();
   }

   public List<?> getNewElements()
   {
      return _origin.getNewElements();
   }

   public ObservableEventList<?> getSource()
   {
      return _origin.getSource();
   }

   public ListEvent<?> getOriginalEvent()
   {
      return _origin;
   }
}
