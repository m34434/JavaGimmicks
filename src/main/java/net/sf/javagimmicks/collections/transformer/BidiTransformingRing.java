package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.Traverser;

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
   public Traverser<T> traverser()
   {
      return TransformerUtils.decorate(_internalRing.traverser(), getBidiTransformer());
   }
}
