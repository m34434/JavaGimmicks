package net.sf.javagimmicks.collections8.transformer;

import java.util.Comparator;
import java.util.Spliterator;

import net.sf.javagimmicks.transform8.BidiFunction;
import net.sf.javagimmicks.transform8.BidiTransforming;

class BidiTransformingSpliterator<F, T> extends TransformingSpliterator<F, T> implements BidiTransforming<F, T>
{
   BidiTransformingSpliterator(final Spliterator<F> internalSpliterator, final BidiFunction<F, T> transformer)
   {
      super(internalSpliterator, transformer);
   }

   @Override
   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>) getTransformerFunction();
   }

   @Override
   public Comparator<? super T> getComparator()
   {
      return TransformerUtils.decorate(_internalSpliterator.getComparator(), getTransformerBidiFunction().invert());
   }
}
