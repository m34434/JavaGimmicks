package net.sf.javagimmicks.collections8.transformer;

import java.util.ListIterator;
import java.util.function.Function;

class TransformingListIterator<F, T>
   extends TransformingIterator<F, T>
   implements ListIterator<T>
{
   TransformingListIterator(ListIterator<F> iterator, Function<F, T> transformer)
   {
      super(iterator, transformer);
   }
   
   public void add(T e)
   {
      throw new UnsupportedOperationException();
   }
   
   public boolean hasPrevious()
   {
      return getInternalIterator().hasPrevious();
   }
   
   public int nextIndex()
   {
      return getInternalIterator().nextIndex();
   }
   
   public T previous()
   {
      return transform(getInternalIterator().previous());
   }
   
   public int previousIndex()
   {
      return getInternalIterator().previousIndex();
   }
   
   public void set(T e)
   {
      throw new UnsupportedOperationException();
   }
   
   protected ListIterator<F> getInternalIterator()
   {
      return (ListIterator<F>) _internalIterator;
   }
}
