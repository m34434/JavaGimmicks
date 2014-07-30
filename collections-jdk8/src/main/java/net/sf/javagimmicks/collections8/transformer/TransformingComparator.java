package net.sf.javagimmicks.collections8.transformer;

import java.util.Comparator;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.Transforming;

class TransformingComparator<F, T> implements Comparator<T>, Transforming<T, F>
{
   protected final Comparator<? super F> _internalComparator;
   private final Function<T, F> _transformer;

   TransformingComparator(Comparator<? super F> comparator, Function<T, F> transformer)
   {
      _internalComparator = comparator;
      _transformer = transformer;
   }

   public int compare(T o1, T o2)
   {
      return _internalComparator.compare(_transformer.apply(o1), _transformer.apply(o2));
   }

   @Override
   public Function<T, F> getTransformerFunction()
   {
      return _transformer;
   }
}
