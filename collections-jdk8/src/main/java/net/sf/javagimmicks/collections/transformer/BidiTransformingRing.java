package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingRing<F, T>
   extends TransformingRing<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingRing(Ring<F> ring, BidiFunction<F, T> tansformer)
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
