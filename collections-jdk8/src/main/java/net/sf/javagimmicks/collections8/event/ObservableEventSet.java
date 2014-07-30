package net.sf.javagimmicks.collections8.event;

import java.util.Set;

import net.sf.javagimmicks.collections8.event.SetEvent.Type;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

/**
 * A {@link Set} decorator that serves as an {@link Observable} for
 * {@link SetEvent}s.
 */
public class ObservableEventSet<E> extends AbstractEventSet<E> implements Observable<SetEvent<E>>
{
   private static final long serialVersionUID = 4799365684601532982L;

   protected final ObservableBase<SetEvent<E>> _helper = new ObservableBase<SetEvent<E>>();

   /**
    * Wraps a new {@link ObservableEventSet} around a given {@link Set}.
    * 
    * @param decorated
    *           the {@link Set} to wrap around
    */
   public ObservableEventSet(final Set<E> decorated)
   {
      super(decorated);
   }

   @Override
   public <L extends EventListener<SetEvent<E>>> void addEventListener(final L listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public <L extends EventListener<SetEvent<E>>> void removeEventListener(final L listener)
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
