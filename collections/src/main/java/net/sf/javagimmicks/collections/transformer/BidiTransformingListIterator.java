package net.sf.javagimmicks.collections.transformer;

import java.util.ListIterator;

import net.sf.javagimmicks.lang.BidiTransformer;
import net.sf.javagimmicks.lang.BidiTransforming;

class BidiTransformingListIterator<F, T>
   extends TransformingListIterator<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingListIterator(ListIterator<F> iterator,
         BidiTransformer<F, T> transformer)
   {
      super(iterator, transformer);
   }

   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
   }

   @Override
   public void add(T e)
   {
      getInternalIterator().add(transformBack(e));
   }

   @Override
   public void set(T e)
   {
      getInternalIterator().set(transformBack(e));
   }

   protected F transformBack(T element)
   {
      return getBidiTransformer().transformBack(element);
   }
}
