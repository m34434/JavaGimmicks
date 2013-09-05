package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.SortedSet;

/**
 * A basic class for {@link SortedSet} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractSortedSetDecorator<E> extends AbstractSetDecorator<E> implements SortedSet<E>
{
   private static final long serialVersionUID = -5870121449307425074L;

   protected AbstractSortedSetDecorator(SortedSet<E> decorated)
   {
      super(decorated);
   }

   /**
    * Returns the decorated instance (the delegate)
    */
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

   public SortedSet<E> headSet(E toElement)
   {
      return getDecorated().headSet(toElement);
   }

   public E last()
   {
      return getDecorated().last();
   }

   public SortedSet<E> subSet(E fromElement, E toElement)
   {
      return getDecorated().subSet(fromElement, toElement);
   }

   public SortedSet<E> tailSet(E fromElement)
   {
      return getDecorated().tailSet(fromElement);
   }
}
