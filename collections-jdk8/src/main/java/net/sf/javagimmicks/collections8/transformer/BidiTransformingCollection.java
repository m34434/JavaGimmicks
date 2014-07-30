package net.sf.javagimmicks.collections8.transformer;

import java.util.Collection;

import net.sf.javagimmicks.transform8.BidiFunction;
import net.sf.javagimmicks.transform8.BidiTransforming;

class BidiTransformingCollection<F, T>
	extends TransformingCollection<F, T>
	implements BidiTransforming<F, T>
{
   BidiTransformingCollection(Collection<F> collection, BidiFunction<F, T> transformer)
   {
      super(collection, transformer);
   }

   public BidiFunction<F, T> getBidiTransformer()
   {
      return (BidiFunction<F, T>)getTransformer();
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
