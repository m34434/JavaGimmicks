package net.sf.javagimmicks.collections8.transformer;

import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;
import net.sf.javagimmicks.transform8.BidiFunction;
import net.sf.javagimmicks.transform8.BidiTransforming;

class BidiTransformingRing<F, T>
   extends TransformingRing<F, T>
   implements BidiTransforming<F, T>
{
   BidiTransformingRing(Ring<F> ring, BidiFunction<F, T> tansformer)
   {
      super(ring, tansformer);
   }
   
   public BidiFunction<F, T> getBidiTransformer()
   {
      return (BidiFunction<F, T>)getTransformer();
   }

   @Override
   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRing.cursor(), getBidiTransformer());
   }
}
