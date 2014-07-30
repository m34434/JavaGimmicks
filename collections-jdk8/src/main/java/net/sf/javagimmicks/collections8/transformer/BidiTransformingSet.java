package net.sf.javagimmicks.collections8.transformer;

import java.util.Set;

import net.sf.javagimmicks.transform8.BidiFunction;
import net.sf.javagimmicks.transform8.BidiTransforming;

class BidiTransformingSet<F, T>
   extends TransformingSet<F, T>
   implements BidiTransforming<F, T>
{
   BidiTransformingSet(Set<F> set, BidiFunction<F, T> transformer)
   {
      super(set, transformer);
   }
   
   public BidiFunction<F, T> getBidiTransformer()
   {
      return (BidiFunction<F, T>)getTransformer();
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
      return getBidiTransformer().applyReverse(element);
   }
}
