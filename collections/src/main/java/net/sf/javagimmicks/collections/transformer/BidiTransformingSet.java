package net.sf.javagimmicks.collections.transformer;

import java.util.Set;

import net.sf.javagimmicks.lang.BidiTransformer;
import net.sf.javagimmicks.lang.BidiTransforming;

class BidiTransformingSet<F, T>
   extends TransformingSet<F, T>
   implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingSet(Set<F> set, BidiTransformer<F, T> transformer)
   {
      super(set, transformer);
   }
   
   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
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
      return getBidiTransformer().transformBack(element);
   }
}
