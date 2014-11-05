package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingRingCursor<F, T>
   extends TransformingRingCursor<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingRingCursor(RingCursor<F> ringCursor, BidiFunction<F, T> transformer)
   {
      super(ringCursor, transformer);
   }
   
   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>)getTransformerFunction();
   }

   @Override
   public void insertAfter(T value)
   {
      _internalRingCursor.insertAfter(getTransformerBidiFunction().applyReverse(value));
   }

   @Override
   public void insertBefore(T value)
   {
      _internalRingCursor.insertBefore(getTransformerBidiFunction().applyReverse(value));
   }
}
