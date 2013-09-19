package net.sf.javagimmicks.collections.event;

import java.util.Iterator;
import java.util.Set;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableSetDecorator;

/**
 * A base {@link Set} wrapper that reports changes to internal callback methods
 * - these must be overwritten by concrete implementations in order to react in
 * any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementAdded(Object)}</li>
 * <li>{@link #fireElementReadded(Object)}</li>
 * <li>{@link #fireElementRemoved(Object)}</li>
 * </ul>
 */
public abstract class AbstractEventSet<E> extends AbstractUnmodifiableSetDecorator<E>
{
   private static final long serialVersionUID = -7712383972215104949L;

   /**
    * Wraps a new instance around a given {@link Set}
    * 
    * @param decorated
    *           the {@link Set} to wrap
    */
   public AbstractEventSet(final Set<E> decorated)
   {
      super(decorated);
   }

   @Override
   public boolean add(final E e)
   {
      final boolean result = getDecorated().add(e);

      if (result)
      {
         fireElementAdded(e);
      }
      else
      {
         fireElementReadded(e);
      }

      return result;
   }

   @Override
   public Iterator<E> iterator()
   {
      return new EventSetIterator(getDecorated().iterator());
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean remove(final Object o)
   {
      final boolean result = getDecorated().remove(o);

      if (result)
      {
         fireElementRemoved((E) o);
      }

      return result;
   }

   protected class EventSetIterator implements Iterator<E>
   {
      protected final Iterator<E> _decorated;
      protected E _lastElement = null;

      public EventSetIterator(final Iterator<E> decorated)
      {
         _decorated = decorated;
      }

      @Override
      public boolean hasNext()
      {
         return _decorated.hasNext();
      }

      @Override
      public E next()
      {
         _lastElement = _decorated.next();

         return _lastElement;
      }

      @Override
      public void remove()
      {
         _decorated.remove();

         fireElementRemoved(_lastElement);
      }
   }

   abstract protected void fireElementAdded(E element);

   abstract protected void fireElementReadded(E element);

   abstract protected void fireElementRemoved(E element);
}