package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.transform.Transforming;
import net.sf.javagimmicks.util.Function;

class TransformingList<F, T>
	extends AbstractList<T>
	implements Transforming<F, T>
{
   protected final List<F> _internalList;
   private final Function<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingList(List<F> list, Function<F, T> transformer)
   {
      _internalList = list;
      _transformer = transformer;
   }
   
   public Function<F, T> getTransformerFunction()
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
      return TransformerUtils.decorate(_internalList.iterator(), getTransformerFunction());
   }
    
   @Override
   public ListIterator<T> listIterator()
   {
      return TransformerUtils.decorate(_internalList.listIterator(), getTransformerFunction());
   }
    
   @Override
   public ListIterator<T> listIterator(int index)
   {
      return TransformerUtils.decorate(_internalList.listIterator(index), getTransformerFunction());
   }
   
   @Override
   public void clear()
   {
      _internalList.clear();
   }

   protected T transform(F element)
   {
      return getTransformerFunction().apply(element);
   }
}
