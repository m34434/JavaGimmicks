package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.SortedSet;

public abstract class AbstractUnmodifiableSortedSetDecorator<E> extends AbstractUnmodifiableSetDecorator<E> implements SortedSet<E>
{
   private static final long serialVersionUID = -1294628897899404764L;

   protected AbstractUnmodifiableSortedSetDecorator(SortedSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedSet<E> getDecorated()
   {
      return (SortedSet<E>) super.getDecorated();
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
}
