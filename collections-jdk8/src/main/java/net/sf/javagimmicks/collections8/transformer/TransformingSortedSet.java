package net.sf.javagimmicks.collections8.transformer;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Function;

class TransformingSortedSet<F, T> extends TransformingSet<F, T> implements SortedSet<T>
{
   TransformingSortedSet(SortedSet<F> set, Function<F, T> transformer)
   {
      super(set, transformer);
   }
   
   public Comparator<? super T> comparator()
   {
      throw new UnsupportedOperationException();
   }

   public T first()
   {
      return transform(getSortedSet().first());
   }

   public SortedSet<T> headSet(T toElement)
   {
      throw new UnsupportedOperationException();
   }

   public T last()
   {
      return transform(getSortedSet().last());
   }

   public SortedSet<T> subSet(T fromElement, T toElement)
   {
      throw new UnsupportedOperationException();
   }

   public SortedSet<T> tailSet(T fromElement)
   {
      throw new UnsupportedOperationException();
   }

   protected SortedSet<F> getSortedSet()
   {
      return (SortedSet<F>)_internalSet;
   }
   
   protected T transform(F element)
   {
      return getTransformer().apply(element);
   }
   
}
