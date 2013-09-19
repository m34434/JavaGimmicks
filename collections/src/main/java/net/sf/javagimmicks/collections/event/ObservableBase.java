package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;

public class ObservableBase<Evt extends Event<Evt, L>, L extends EventListener<Evt, L>> implements Observable<Evt, L>
{
   protected transient List<L> _listeners;

   @Override
   public void addEventListener(final L listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<L>();
      }

      _listeners.add(listener);
   }

   @Override
   public void removeEventListener(final L listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   public void fireEvent(final Evt event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final L listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
}
