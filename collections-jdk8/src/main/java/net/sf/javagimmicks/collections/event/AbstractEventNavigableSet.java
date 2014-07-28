package net.sf.javagimmicks.collections.event;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

/**
 * A base {@link NavigableSet} wrapper that reports changes to internal callback
 * methods - these must be overwritten by concrete implementations in order to
 * react in any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementAdded(Object)}</li>
 * <li>{@link #fireElementReadded(Object)}</li>
 * <li>{@link #fireElementRemoved(Object)}</li>
 * </ul>
 */
public abstract class AbstractEventNavigableSet<E> extends AbstractEventSortedSet<E> implements NavigableSet<E>
{
   private static final long serialVersionUID = 5422669944595136215L;

   /**
    * Wraps a new instance around a given {@link SortedSet}
    * 
    * @param decorated
    *           the {@link NavigableSet} to wrap
    */
   public AbstractEventNavigableSet(final NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public NavigableSet<E> getDecorated()
   {
      return (NavigableSet<E>) super.getDecorated();
   }

   @Override
   public E ceiling(final E e)
   {
      return getDecorated().ceiling(e);
   }

   @Override
   public Iterator<E> descendingIterator()
   {
      return new EventSetIterator(getDecorated().descendingIterator());
   }

   @Override
   public NavigableSet<E> descendingSet()
   {
      return new EventSubNavigableSet<E>(this, getDecorated().descendingSet());
   }

   @Override
   public E floor(final E e)
   {
      return getDecorated().floor(e);
   }

   @Override
   public NavigableSet<E> headSet(final E toElement, final boolean inclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated().headSet(toElement, inclusive));
   }

   @Override
   public E higher(final E e)
   {
      return getDecorated().higher(e);
   }

   @Override
   public E lower(final E e)
   {
      return getDecorated().lower(e);
   }

   @Override
   public E pollFirst()
   {
      final E result = getDecorated().pollFirst();

      fireElementRemoved(result);

      return result;
   }

   @Override
   public E pollLast()
   {
      final E result = getDecorated().pollLast();

      fireElementRemoved(result);

      return result;
   }

   @Override
   public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement,
         final boolean toInclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated()
            .subSet(fromElement, fromInclusive, toElement, toInclusive));
   }

   @Override
   public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated().tailSet(fromElement, inclusive));
   }

   protected static class EventSubNavigableSet<E> extends AbstractEventNavigableSet<E>
   {
      private static final long serialVersionUID = 7026414183073208442L;

      protected final AbstractEventNavigableSet<E> _parent;

      protected EventSubNavigableSet(final AbstractEventNavigableSet<E> parent, final NavigableSet<E> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      protected void fireElementAdded(final E element)
      {
         _parent.fireElementAdded(element);
      }

      @Override
      protected void fireElementReadded(final E element)
      {
         _parent.fireElementReadded(element);
      }

      @Override
      protected void fireElementRemoved(final E element)
      {
         _parent.fireElementRemoved(element);
      }
   }
}
