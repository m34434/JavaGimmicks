package net.sf.javagimmicks.collections.transformer;

import java.util.Set;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingSet<F, T>
   extends TransformingSet<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingSet(Set<F> set, BidiFunction<F, T> transformer)
   {
      super(set, transformer);
   }
   
   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>)getTransformerFunction();
   }

   @Override
   public boolean add(T e)
   {
      return _internalSet.add(transformBack(e));
   }

   @SuppressWarnings("unchecked")
   @Override
   /**
    * Try to transform back the value, because the internal
    * {@link Set} might be faster performing this method.
    */
   public boolean contains(Object o)
   {
      try
      {
         return _internalSet.contains(transformBack((T)o));
      }
      catch (ClassCastException e)
      {
         return super.contains(o);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   /**
    * Try to transform back the value, because the internal
    * {@link Set} might be faster performing this method.
    */
   public boolean remove(Object o)
   {
      try
      {
         return _internalSet.remove(transformBack((T)o));
      }
      catch (ClassCastException e)
      {
         return super.remove(o);
      }
   }
   
   protected F transformBack(T element)
   {
      return getTransformerBidiFunction().applyReverse(element);
   }
}
