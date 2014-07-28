package net.sf.javagimmicks.collections.transformer;

import java.util.Comparator;
import java.util.function.Function;

class TransformingComparator<F, T> implements Comparator<T>
{  
   protected final Comparator<? super F> _internalComparator;
   private final Function<T, F> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingComparator(Comparator<? super F> comparator, Function<T, F> transformer)
   {
      _internalComparator = comparator;
      _transformer = transformer;
   }

   public int compare(T o1, T o2)
   {
      return _internalComparator.compare(_transformer.apply(o1), _transformer.apply(o2));
   }
   
}
