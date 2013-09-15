package net.sf.javagimmicks.collections.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableListDecorator;

public abstract class AbstractEventList<E> extends AbstractUnmodifiableListDecorator<E>
{
   private static final long serialVersionUID = 7192864561064933670L;

   public AbstractEventList(List<E> decorated)
   {
      super(decorated);
   }
   
   @Override
   public void add(int index, E element)
   {
      getDecorated().add(index, element);
      
      fireElementAdded(index, element);
   }
   
   @Override
   public boolean addAll(int index, Collection<? extends E> c)
   {
      boolean result = getDecorated().addAll(index, c);
      
      fireElementsAdded(index, c);
      
      return result;
   }

   @Override
   public Iterator<E> iterator()
   {
      return listIterator();
   }

   @Override
   public ListIterator<E> listIterator(int index)
   {
      return new EventListListIterator<E>(this, index);
   }

   @Override
   public E remove(int index)
   {
      E element = getDecorated().remove(index);
      
      fireElementRemoved(index, element);
      
      return element;
   }

   @Override
   public E set(int index, E element)
   {
      E oldElement = getDecorated().set(index, element);
      
      fireElementUpdated(index, oldElement, element);
      
      return oldElement;
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex)
   {
      return new EventSubList<E>(this, fromIndex, toIndex);
   }

   abstract protected void fireElementsAdded(int index, Collection<? extends E> elements);
   abstract protected void fireElementUpdated(int index, E element, E newElement);
   abstract protected void fireElementRemoved(int index, E element);
   
   protected void fireElementAdded(int index, E element)
   {
      fireElementsAdded(index, Collections.singletonList(element));
   }

   protected static class EventListListIterator<E> implements ListIterator<E>
   {
      protected final AbstractEventList<E> _parent;
      protected final ListIterator<E> _decorated;
      protected int _lastIndex;
      
      public EventListListIterator(AbstractEventList<E> parent, int index)
      {
         _parent = parent;
         _decorated = _parent._decorated.listIterator(index);
      }

      public void add(E e)
      {
         _decorated.add(e);
         
         int index = _decorated.previousIndex();
         _parent.fireElementAdded(index, e);
      }

      public void remove()
      {
         E element = _parent.get(_lastIndex);
         _decorated.remove();
         
         int index = _decorated.nextIndex();
         _parent.fireElementRemoved(index, element);
      }

      public void set(E e)
      {
         E oldElement = _parent.get(_lastIndex);
         _decorated.set(e);
         
         _parent.fireElementUpdated(_lastIndex, oldElement, e);
      }
      
      public E next()
      {
         E result = _decorated.next();
         
         _lastIndex = _decorated.previousIndex();
         
         return result;
      }

      public E previous()
      {
         E result = _decorated.previous();
         
         _lastIndex = _decorated.nextIndex();
         
         return result;
      }

      public boolean hasNext()
      {
         return _decorated.hasNext();
      }

      public boolean hasPrevious()
      {
         return _decorated.hasPrevious();
      }

      public int nextIndex()
      {
         return _decorated.nextIndex();
      }

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
      
      protected EventSubList(AbstractEventList<E> parent, int fromIndex, int toIndex)
      {
         super(parent._decorated.subList(fromIndex, toIndex));
         
         _parent = parent;
         _offset = fromIndex;
      }

      @Override
      protected void fireElementsAdded(int index, Collection<? extends E> elements)
      {
         _parent.fireElementsAdded(index + _offset, elements);
      }

      @Override
      protected void fireElementRemoved(int index, E element)
      {
         _parent.fireElementRemoved(index + _offset, element);
      }

      @Override
      protected void fireElementUpdated(int index, E element, E newElement)
      {
         _parent.fireElementUpdated(index + _offset, element, newElement);
      }
   }
}
