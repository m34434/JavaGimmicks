package net.sf.javagimmicks.collections.transformer;

import java.util.Collection;

import net.sf.javagimmicks.transform.BidiTransformer;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingCollection<F, T>
	extends TransformingCollection<F, T>
	implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingCollection(Collection<F> collection, BidiTransformer<F, T> transformer)
   {
      super(collection, transformer);
   }

   public BidiTransformer<F, T> getBidiTransformer()
   {
      return (BidiTransformer<F, T>)getTransformer();
   }

   @Override
   public boolean add(T o)
   {
      return _internalCollection.add(transformBack(o));
   }
   
   protected F transformBack(T element)
   {
      return getBidiTransformer().applyReverse(element);
   }
   
}
