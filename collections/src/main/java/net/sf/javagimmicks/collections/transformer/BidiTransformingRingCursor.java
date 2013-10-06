package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.BidiTransformer;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingRingCursor<F, T>
   extends TransformingRingCursor<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingRingCursor(RingCursor<F> ringCursor, BidiTransformer<F, T> transformer)
   {
      super(ringCursor, transformer);
   }
   
   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
   }

   @Override
   public void insertAfter(T value)
   {
      _internalRingCursor.insertAfter(getBidiTransformer().transformBack(value));
   }

   @Override
   public void insertBefore(T value)
   {
      _internalRingCursor.insertBefore(getBidiTransformer().transformBack(value));
   }
}
