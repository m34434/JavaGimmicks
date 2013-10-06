package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.lang.Transformer;
import net.sf.javagimmicks.lang.Transforming;

class TransformingList<F, T>
	extends AbstractList<T>
	implements Transforming<F, T>
{
   protected final List<F> _internalList;
   private final Transformer<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingList(List<F> list, Transformer<F, T> transformer)
   {
      _internalList = list;
      _transformer = transformer;
   }
   
   public Transformer<F, T> getTransformer()
	{
		return _transformer;
	}
	
	@Override
   public T get(int index)
   {
      return transform(_internalList.get(index));
   }
   
   @Override
   public T remove(int index)
   {
      return transform(_internalList.remove(index));
   }

   @Override
   public int size()
   {
      return _internalList.size();
   }
   
   @Override
   public Iterator<T> iterator()
   {
      return TransformerUtils.decorate(_internalList.iterator(), getTransformer());
   }
    
   @Override
   public ListIterator<T> listIterator()
   {
      return TransformerUtils.decorate(_internalList.listIterator(), getTransformer());
   }
    
   @Override
   public ListIterator<T> listIterator(int index)
   {
      return TransformerUtils.decorate(_internalList.listIterator(index), getTransformer());
   }
   
   @Override
   public void clear()
   {
      _internalList.clear();
   }

   protected T transform(F element)
   {
      return getTransformer().transform(element);
   }
}
