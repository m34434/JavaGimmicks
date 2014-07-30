package net.sf.javagimmicks.collections8.decorators;

import java.util.Iterator;
import java.util.NavigableSet;

/**
 * A basic class for {@link NavigableSet} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public abstract class AbstractNavigableSetDecorator<E> extends AbstractSortedSetDecorator<E> implements NavigableSet<E>
{
   private static final long serialVersionUID = 6737555614624695847L;

   protected AbstractNavigableSetDecorator(final NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public E ceiling(final E e)
   {
      return getDecorated().ceiling(e);
   }

   @Override
   public Iterator<E> descendingIterator()
   {
      return getDecorated().descendingIterator();
   }

   @Override
   public NavigableSet<E> descendingSet()
   {
      return getDecorated().descendingSet();
   }

   @Override
   public E floor(final E e)
   {
      return getDecorated().floor(e);
   }

   @Override
   public NavigableSet<E> headSet(final E toElement, final boolean inclusive)
   {
      return getDecorated().headSet(toElement, inclusive);
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
      return getDecorated().pollFirst();
   }

   @Override
   public E pollLast()
   {
      return getDecorated().pollLast();
   }

   @Override
   public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive,
         final E toElement, final boolean toInclusive)
   {
      return getDecorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
   }

   @Override
   public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive)
   {
      return getDecorated().tailSet(fromElement, inclusive);
   }

   @Override
   protected NavigableSet<E> getDecorated()
   {
      return (NavigableSet<E>) super.getDecorated();
   }
}
