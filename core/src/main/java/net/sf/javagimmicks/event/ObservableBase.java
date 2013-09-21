package net.sf.javagimmicks.event;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for {@link Observable} implementations. It takes care of
 * managing {@link EventListener}s and distributing {@link Event}s to them.
 * <p>
 * The {@link #fireEvent(Event)} method is {@code public}, so the class can also
 * serve as a delegate, if sub-classing is not applicable.
 * <p>
 * <b>Attention:</b> {@link EventListener} management within this class is
 * <b>not thread-safe</b> - so you have to take care about synchronization
 * manually if necessary!
 */
public class ObservableBase<Evt extends Event<Evt>> implements Observable<Evt>
{
   protected transient List<EventListener<Evt>> _listeners;

   @Override
   public <L extends EventListener<Evt>> void addEventListener(final L listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventListener<Evt>>();
      }

      _listeners.add(listener);
   }

   @Override
   public <L extends EventListener<Evt>> void removeEventListener(final L listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   /**
    * Distributes the provided {@link Event} to all registered
    * {@link EventListener}s calling their
    * {@link EventListener#eventOccured(Event)} mtehod.
    * 
    * @param event
    *           the {@link Event} to distribute
    * 
    * @see EventListener#eventOccured(Event)
    */
   public void fireEvent(final Evt event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventListener<Evt> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
}
