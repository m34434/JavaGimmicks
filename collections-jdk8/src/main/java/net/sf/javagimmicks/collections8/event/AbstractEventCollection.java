package net.sf.javagimmicks.collections8.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.sf.javagimmicks.collections8.decorators.AbstractUnmodifiableCollectionDecorator;

/**
 * A base {@link Collection} wrapper that reports changes to internal callback
 * methods - these must be overwritten by concrete implementations in order to
 * react in any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementsAdded(Collection)}</li>
 * <li>{@link #fireElementRemoved(Object)}</li>
 * </ul>
 * <p>
 * Methods that <b>may</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementAdded(Object)}</li>
 * </ul>
 */
public abstract class AbstractEventCollection<E> extends AbstractUnmodifiableCollectionDecorator<E>
{
   private static final long serialVersionUID = -8335291555421718053L;

   /**
    * Wraps a new instance around a given {@link Collection}
    * 
    * @param decorated
    *           the {@link Collection} to wrap
    */
   public AbstractEventCollection(final Collection<E> decorated)
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

      return result;
   }

   @Override
   public boolean addAll(final Collection<? extends E> c)
   {
      final Collection<E> decorated = getDecorated();
      final ArrayList<E> added = new ArrayList<E>();

      for (final E element : c)
      {
         if (decorated.add(element))
         {
            added.add(element);
         }
      }

      if (!added.isEmpty())
      {
         fireElementsAdded(added);
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public Iterator<E> iterator()
   {
      return new EventCollectionIterator(getDecorated().iterator());
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

   abstract protected void fireElementsAdded(Collection<? extends E> c);

   abstract protected void fireElementRemoved(E element);

   protected void fireElementAdded(final E element)
   {
      fireElementsAdded(Collections.singleton(element));
   }

   protected class EventCollectionIterator implements Iterator<E>
   {
      protected final Iterator<E> _decorated;
      protected E _lastElement = null;

      public EventCollectionIterator(final Iterator<E> decorated)
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
}
