package net.sf.javagimmicks.collections.event;

import java.util.Set;

import net.sf.javagimmicks.collections.event.SetEvent.Type;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

public class ObservableEventSet<E> extends AbstractEventSet<E> implements Observable<SetEvent<E>, EventSetListener<E>>
{
   private static final long serialVersionUID = 4799365684601532982L;

   protected final ObservableBase<SetEvent<E>, EventSetListener<E>> _helper = new ObservableBase<SetEvent<E>, EventSetListener<E>>();

   public ObservableEventSet(final Set<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventSetListener<E> listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public void removeEventListener(final EventSetListener<E> listener)
   {
      _helper.removeEventListener(listener);
   }

   @Override
   protected void fireElementAdded(final E element)
   {
      _helper.fireEvent(new SetEventImpl(Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      _helper.fireEvent(new SetEventImpl(Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      _helper.fireEvent(new SetEventImpl(Type.REMOVED, element));
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
