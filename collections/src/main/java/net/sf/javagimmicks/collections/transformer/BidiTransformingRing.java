package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.BidiTransformer;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingRing<F, T>
   extends TransformingRing<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingRing(Ring<F> ring, BidiTransformer<F, T> tansformer)
   {
      super(ring, tansformer);
   }
   
   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
   }

   @Override
   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRing.cursor(), getBidiTransformer());
   }
}
