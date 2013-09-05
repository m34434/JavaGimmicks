/**
 * 
 */
package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.AbstractTraverser;
import net.sf.javagimmicks.collections.Traverser;

class TransformingTraverser<F, T>
   extends AbstractTraverser<T>
   implements Transforming<F, T>
{
   protected final Traverser<F> _internalTraverser;
   private final Transformer<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingTraverser(Traverser<F> traverser, Transformer<F, T> transformer)
   {
      _internalTraverser = traverser;
      _transformer = transformer;
   }
   
   public Transformer<F, T> getTransformer()
   {
      return _transformer;
   }
   
   public int size()
   {
      return _internalTraverser.size();
   }

   public T get()
   {
      return getTransformer().transform(_internalTraverser.get());
   }

   public void insertAfter(T value)
   {
      throw new UnsupportedOperationException();
   }

   public void insertBefore(T value)
   {
      throw new UnsupportedOperationException();
   }

   public T next()
   {
      return getTransformer().transform(_internalTraverser.next());
   }

   public T previous()
   {
      return getTransformer().transform(_internalTraverser.previous());
   }

   public T remove()
   {
      return getTransformer().transform(_internalTraverser.remove());
   }

   public Traverser<T> traverser()
   {
      return TransformerUtils.decorate(_internalTraverser.traverser(), getTransformer());
   }

}