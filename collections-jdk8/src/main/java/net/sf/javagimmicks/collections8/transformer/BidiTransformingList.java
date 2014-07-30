package net.sf.javagimmicks.collections8.transformer;

import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.transform8.BidiFunction;
import net.sf.javagimmicks.transform8.BidiTransforming;

class BidiTransformingList<F, T>
	extends TransformingList<F, T>
	implements BidiTransforming<F, T>
{
   BidiTransformingList(List<F> list, BidiFunction<F, T> transformer)
   {
      super(list, transformer);
   }

   public BidiFunction<F, T> getBidiTransformer()
   {
      return (BidiFunction<F, T>)getTransformer();
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
      return TransformerUtils.decorate(_internalList.listIterator(), getBidiTransformer());
   }
    
   @Override
   public ListIterator<T> listIterator(int index)
   {
      return TransformerUtils.decorate(_internalList.listIterator(index), getBidiTransformer());
   }

   protected F transformBack(T element)
   {
      return getBidiTransformer().applyReverse(element);
   }
}
