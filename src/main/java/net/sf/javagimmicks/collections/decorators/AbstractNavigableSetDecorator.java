package net.sf.javagimmicks.collections.decorators;

import java.util.Iterator;
import java.util.NavigableSet;

public abstract class AbstractNavigableSetDecorator<E> extends AbstractSortedSetDecorator<E> implements NavigableSet<E>
{
   private static final long serialVersionUID = 6737555614624695847L;

   protected AbstractNavigableSetDecorator(NavigableSet<E> decorated)
   {
      super(decorated);
   }

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
      return getDecorated().descendingIterator();
   }

   public NavigableSet<E> descendingSet()
   {
      return getDecorated().descendingSet();
   }

   public E floor(E e)
   {
      return getDecorated().floor(e);
   }

   public NavigableSet<E> headSet(E toElement, boolean inclusive)
   {
      return getDecorated().headSet(toElement, inclusive);
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
      return getDecorated().pollFirst();
   }

   public E pollLast()
   {
      return getDecorated().pollLast();
   }

   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
         E toElement, boolean toInclusive)
   {
      return getDecorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
   }

   public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
   {
      return getDecorated().tailSet(fromElement, inclusive);
   }
}
