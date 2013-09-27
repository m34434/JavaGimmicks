package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.SortedSet;

/**
 * A basic class for {@link SortedSet} decorators that simply forwards all calls
 * to an internal delegate instance.
 */
public abstract class AbstractSortedSetDecorator<E> extends AbstractSetDecorator<E> implements SortedSet<E>
{
   private static final long serialVersionUID = -5870121449307425074L;

   protected AbstractSortedSetDecorator(final SortedSet<E> decorated)
   {
      super(decorated);
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
   public SortedSet<E> headSet(final E toElement)
   {
      return getDecorated().headSet(toElement);
   }

   @Override
   public E last()
   {
      return getDecorated().last();
   }

   @Override
   public SortedSet<E> subSet(final E fromElement, final E toElement)
   {
      return getDecorated().subSet(fromElement, toElement);
   }

   @Override
   public SortedSet<E> tailSet(final E fromElement)
   {
      return getDecorated().tailSet(fromElement);
   }

   @Override
   protected SortedSet<E> getDecorated()
   {
      return (SortedSet<E>) super.getDecorated();
   }
}
