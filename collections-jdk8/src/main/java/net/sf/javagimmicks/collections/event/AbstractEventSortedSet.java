package net.sf.javagimmicks.collections.event;

import java.util.Comparator;
import java.util.SortedSet;

/**
 * A base {@link SortedSet} wrapper that reports changes to internal callback
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
public abstract class AbstractEventSortedSet<E> extends AbstractEventSet<E> implements SortedSet<E>
{
   private static final long serialVersionUID = 3408305529028504791L;

   /**
    * Wraps a new instance around a given {@link SortedSet}
    * 
    * @param decorated
    *           the {@link SortedSet} to wrap
    */
   public AbstractEventSortedSet(final SortedSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedSet<E> getDecorated()
   {
      return (SortedSet<E>) super.getDecorated();
   }

   @Override
   public Comparator<? super E> comparator()
   {
      return getDecorated().comparator();
   }

   @Override
   public E first()
   {
      return getDecorated().first();
   }

   @Override
   public E last()
   {
      return getDecorated().last();
   }

   @Override
   public SortedSet<E> headSet(final E toElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().headSet(toElement));
   }

   @Override
   public SortedSet<E> subSet(final E fromElement, final E toElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().subSet(fromElement, toElement));
   }

   @Override
   public SortedSet<E> tailSet(final E fromElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().tailSet(fromElement));
   }

   protected static class EventSubSortedSet<E> extends AbstractEventSortedSet<E>
   {
      private static final long serialVersionUID = 7103404017576141323L;

      protected final AbstractEventSortedSet<E> _parent;

      protected EventSubSortedSet(final AbstractEventSortedSet<E> parent, final SortedSet<E> decorated)
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
