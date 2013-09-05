package net.sf.javagimmicks.collections.event;

import java.util.Comparator;
import java.util.SortedSet;

public abstract class AbstractEventSortedSet<E> extends AbstractEventSet<E> implements SortedSet<E>
{
   private static final long serialVersionUID = 3408305529028504791L;

   public AbstractEventSortedSet(SortedSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedSet<E> getDecorated()
   {
      return (SortedSet<E>)super.getDecorated();
   }

   public Comparator<? super E> comparator()
   {
      return getDecorated().comparator();
   }

   public E first()
   {
      return getDecorated().first();
   }

   public E last()
   {
      return getDecorated().last();
   }

   public SortedSet<E> headSet(E toElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().headSet(toElement));
   }

   public SortedSet<E> subSet(E fromElement, E toElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().subSet(fromElement, toElement));
   }

   public SortedSet<E> tailSet(E fromElement)
   {
      return new EventSubSortedSet<E>(this, getDecorated().tailSet(fromElement));
   }
 
   protected static class EventSubSortedSet<E> extends AbstractEventSortedSet<E>
   {
      private static final long serialVersionUID = 7103404017576141323L;

      protected final AbstractEventSortedSet<E> _parent;
      
      protected EventSubSortedSet(AbstractEventSortedSet<E> parent, SortedSet<E> decorated)
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
