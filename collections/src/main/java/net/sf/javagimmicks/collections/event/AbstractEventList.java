package net.sf.javagimmicks.collections.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableListDecorator;

/**
 * A base {@link List} wrapper that reports changes to internal callback methods
 * - these must be overwritten by concrete implementations in order to react in
 * any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementsAdded(int, Collection)}</li>
 * <li>{@link #fireElementUpdated(int, Object, Object)}</li>
 * <li>{@link #fireElementRemoved(int, Object)}</li>
 * </ul>
 * <p>
 * Methods that <b>may</b> be overwritten:
 * <ul>
 * <li>{@link #fireElementAdded(int, Object)}</li>
 * </ul>
 */
public abstract class AbstractEventList<E> extends AbstractUnmodifiableListDecorator<E>
{
   private static final long serialVersionUID = 7192864561064933670L;

   /**
    * Wraps a new instance around a given {@link List}
    * 
    * @param decorated
    *           the {@link List} to wrap
    */
   public AbstractEventList(final List<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void add(final int index, final E element)
   {
      getDecorated().add(index, element);

      fireElementAdded(index, element);
   }

   @Override
   public boolean addAll(final int index, final Collection<? extends E> c)
   {
      final boolean result = getDecorated().addAll(index, c);

      fireElementsAdded(index, c);

      return result;
   }

   @Override
   public Iterator<E> iterator()
   {
      return listIterator();
   }

   @Override
   public ListIterator<E> listIterator(final int index)
   {
      return new EventListListIterator<E>(this, index);
   }

   @Override
   public E remove(final int index)
   {
      final E element = getDecorated().remove(index);

      fireElementRemoved(index, element);

      return element;
   }

   @Override
   public E set(final int index, final E element)
   {
      final E oldElement = getDecorated().set(index, element);

      fireElementUpdated(index, oldElement, element);

      return oldElement;
   }

   @Override
   public List<E> subList(final int fromIndex, final int toIndex)
   {
      return new EventSubList<E>(this, fromIndex, toIndex);
   }

   abstract protected void fireElementsAdded(int index, Collection<? extends E> elements);

   abstract protected void fireElementUpdated(int index, E element, E newElement);

   abstract protected void fireElementRemoved(int index, E element);

   protected void fireElementAdded(final int index, final E element)
   {
      fireElementsAdded(index, Collections.singletonList(element));
   }

   protected static class EventListListIterator<E> implements ListIterator<E>
   {
      protected final AbstractEventList<E> _parent;
      protected final ListIterator<E> _decorated;
      protected int _lastIndex;

      public EventListListIterator(final AbstractEventList<E> parent, final int index)
      {
         _parent = parent;
         _decorated = _parent._decorated.listIterator(index);
      }

      @Override
      public void add(final E e)
      {
         _decorated.add(e);

         final int index = _decorated.previousIndex();
         _parent.fireElementAdded(index, e);
      }

      @Override
      public void remove()
      {
         final E element = _parent.get(_lastIndex);
         _decorated.remove();

         final int index = _decorated.nextIndex();
         _parent.fireElementRemoved(index, element);
      }

      @Override
      public void set(final E e)
      {
         final E oldElement = _parent.get(_lastIndex);
         _decorated.set(e);

         _parent.fireElementUpdated(_lastIndex, oldElement, e);
      }

      @Override
      public E next()
      {
         final E result = _decorated.next();

         _lastIndex = _decorated.previousIndex();

         return result;
      }

      @Override
      public E previous()
      {
         final E result = _decorated.previous();

         _lastIndex = _decorated.nextIndex();

         return result;
      }

      @Override
      public boolean hasNext()
      {
         return _decorated.hasNext();
      }

      @Override
      public boolean hasPrevious()
      {
         return _decorated.hasPrevious();
      }

      @Override
      public int nextIndex()
      {
         return _decorated.nextIndex();
      }

      @Override
      public int previousIndex()
      {
         return _decorated.previousIndex();
      }
   }

   protected static class EventSubList<E> extends AbstractEventList<E>
   {
      private static final long serialVersionUID = -3091453601436750348L;

      protected final AbstractEventList<E> _parent;
      protected final int _offset;

      protected EventSubList(final AbstractEventList<E> parent, final int fromIndex, final int toIndex)
      {
         super(parent._decorated.subList(fromIndex, toIndex));

         _parent = parent;
         _offset = fromIndex;
      }

      @Override
      protected void fireElementsAdded(final int index, final Collection<? extends E> elements)
      {
         _parent.fireElementsAdded(index + _offset, elements);
      }

      @Override
      protected void fireElementRemoved(final int index, final E element)
      {
         _parent.fireElementRemoved(index + _offset, element);
      }

      @Override
      protected void fireElementUpdated(final int index, final E element, final E newElement)
      {
         _parent.fireElementUpdated(index + _offset, element, newElement);
      }
   }
}
