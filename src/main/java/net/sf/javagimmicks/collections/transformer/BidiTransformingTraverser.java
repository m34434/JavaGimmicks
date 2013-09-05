package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.Traverser;

class BidiTransformingTraverser<F, T>
   extends TransformingTraverser<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingTraverser(Traverser<F> traverser, BidiTransformer<F, T> transformer)
   {
      super(traverser, transformer);
   }
   
   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
   }

   @Override
   public void insertAfter(T value)
   {
      _internalTraverser.insertAfter(getBidiTransformer().transformBack(value));
   }

   @Override
   public void insertBefore(T value)
   {
      _internalTraverser.insertBefore(getBidiTransformer().transformBack(value));
   }
}
