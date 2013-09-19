package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.javagimmicks.collections.event.SetEvent.Type;

public class ObservableEventSet<E> extends AbstractEventSet<E>
{
   private static final long serialVersionUID = 4799365684601532982L;

   protected transient List<EventSetListener<E>> _listeners;

   public ObservableEventSet(final Set<E> decorated)
   {
      super(decorated);
   }

   public void addEventSetListener(final EventSetListener<E> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventSetListener<E>>();
      }

      _listeners.add(listener);
   }

   public void removeEventSetListener(final EventSetListener<E> listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   protected void fireElementAdded(final E element)
   {
      fireEvent(new SetEventImpl(Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      fireEvent(new SetEventImpl(Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      fireEvent(new SetEventImpl(Type.REMOVED, element));
   }

   private void fireEvent(final SetEventImpl event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventSetListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class SetEventImpl implements SetEvent<E>
   {
      protected final Type _type;
      protected final E _element;

      public SetEventImpl(final Type type, final E element)
      {
         _type = type;
         _element = element;
      }

      @Override
      public Type getType()
      {
         return _type;
      }

      @Override
      public E getElement()
      {
         return _element;
      }

      @Override
      public ObservableEventSet<E> getSource()
      {
         return ObservableEventSet.this;
      }
   }
}
