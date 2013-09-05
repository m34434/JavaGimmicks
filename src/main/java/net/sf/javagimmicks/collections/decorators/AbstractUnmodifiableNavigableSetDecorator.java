package net.sf.javagimmicks.collections.decorators;

import java.util.NavigableSet;

public abstract class AbstractUnmodifiableNavigableSetDecorator<E> extends AbstractUnmodifiableSortedSetDecorator<E> implements NavigableSet<E>
{
   private static final long serialVersionUID = -2004886060529930248L;

   protected AbstractUnmodifiableNavigableSetDecorator(NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public NavigableSet<E> getDecorated()
   {
      return (NavigableSet<E>) super.getDecorated();
   }

   public E ceiling(E e)
   {
      return getDecorated().ceiling(e);
   }

   public E floor(E e)
   {
      return getDecorated().floor(e);
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
}
