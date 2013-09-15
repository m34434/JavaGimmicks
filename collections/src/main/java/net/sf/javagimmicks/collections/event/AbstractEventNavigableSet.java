package net.sf.javagimmicks.collections.event;

import java.util.Iterator;
import java.util.NavigableSet;

public abstract class AbstractEventNavigableSet<E> extends AbstractEventSortedSet<E> implements NavigableSet<E>
{
   private static final long serialVersionUID = 5422669944595136215L;

   public AbstractEventNavigableSet(NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public NavigableSet<E> getDecorated()
   {
      return (NavigableSet<E>)super.getDecorated();
   }

   public E ceiling(E e)
   {
      return getDecorated().ceiling(e);
   }

   public Iterator<E> descendingIterator()
   {
      return new EventSetIterator(getDecorated().descendingIterator());
   }

   public NavigableSet<E> descendingSet()
   {
      return new EventSubNavigableSet<E>(this, getDecorated().descendingSet());
   }

   public E floor(E e)
   {
      return getDecorated().floor(e);
   }

   public NavigableSet<E> headSet(E toElement, boolean inclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated().headSet(toElement, inclusive));
   }

   public E higher(E e)
   {
      return getDecorated().higher(e);
   }

   public E lower(E e)
   {
      return getDecorated().lower(e);
   }

   public E pollFirst()
   {
      E result = getDecorated().pollFirst();
      
      fireElementRemoved(result);
      
      return result;
   }

   public E pollLast()
   {
      E result = getDecorated().pollLast();
      
      fireElementRemoved(result);
      
      return result;
   }

   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated().subSet(fromElement, fromInclusive, toElement, toInclusive));
   }

   public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
   {
      return new EventSubNavigableSet<E>(this, getDecorated().tailSet(fromElement, inclusive));
   }
   
   protected static class EventSubNavigableSet<E> extends AbstractEventNavigableSet<E>
   {
      private static final long serialVersionUID = 7026414183073208442L;

      protected final AbstractEventNavigableSet<E> _parent;

      protected EventSubNavigableSet(AbstractEventNavigableSet<E> parent, NavigableSet<E> decorated)
      {
         super(decorated);
         _parent = parent;
      }

      @Override
      protected void fireElementAdded(E element)
      {
         _parent.fireElementAdded(element);
      }

      @Override
      protected void fireElementReadded(E element)
      {
         _parent.fireElementReadded(element);
      }

      @Override
      protected void fireElementRemoved(E element)
      {
         _parent.fireElementRemoved(element);
      }
   }
}
