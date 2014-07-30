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

   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>)getTransformerFunction();
   }

   @Override
   public boolean add(T o)
   {
      return _internalCollection.add(transformBack(o));
   }
   
   protected F transformBack(T element)
   {
      return getTransformerBidiFunction().applyReverse(element);
   }
   
}
