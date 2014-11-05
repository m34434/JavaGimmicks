package net.sf.javagimmicks.collections.transformer;

import java.util.Iterator;

import net.sf.javagimmicks.transform.Transforming;
import net.sf.javagimmicks.util.Function;

class TransformingIterator<F, T> implements Iterator<T>, Transforming<F, T>
{
   protected final Iterator<F> _internalIterator;
   private final Function<F, T> _transformer;

   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingIterator(Iterator<F> iterator, Function<F, T> transformer)
   {
      _internalIterator = iterator;
      _transformer = transformer;
   }
   
   public Function<F, T> getTransformerFunction()
   {
      return _transformer;
   }

   public boolean hasNext()
   {
      return _internalIterator.hasNext();
   }

   public T next()
   {
      return transform(_internalIterator.next());
   }

   public void remove()
   {
      _internalIterator.remove();
   }
   
   protected T transform(F element)
   {
       return getTransformerFunction().apply(element);
   }
}
