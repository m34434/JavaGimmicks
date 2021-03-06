package net.sf.javagimmicks.collections.transformer;

import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;

class BidiTransformingList<F, T>
	extends TransformingList<F, T>
	implements BidiTransforming<F, T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingList(List<F> list, BidiFunction<F, T> transformer)
   {
      super(list, transformer);
   }

   public BidiFunction<F, T> getTransformerBidiFunction()
   {
      return (BidiFunction<F, T>)getTransformerFunction();
   }

   @Override
   public void add(int index, T element)
   {
      _internalList.add(index, transformBack(element));
   }

   @Override
   public T set(int index, T element)
   {
      return transform(_internalList.set(index, transformBack(element)));
   }
   
   @Override
   public ListIterator<T> listIterator()
   {
      return TransformerUtils.decorate(_internalList.listIterator(), getTransformerBidiFunction());
   }
    
   @Override
   public ListIterator<T> listIterator(int index)
   {
      return TransformerUtils.decorate(_internalList.listIterator(index), getTransformerBidiFunction());
   }

   protected F transformBack(T element)
   {
      return getTransformerBidiFunction().applyReverse(element);
   }
}
