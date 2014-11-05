package net.sf.javagimmicks.collections.transformer;

import java.util.ListIterator;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingListIterator<F, T>
   extends TransformingListIterator<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingListIterator(ListIterator<F> iterator,
         BidiFunction<F, T> transformer)
   {
      super(iterator, transformer);
   }

   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>)getTransformerFunction();
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
      return getTransformerBidiFunction().applyReverse(element);
   }
}
