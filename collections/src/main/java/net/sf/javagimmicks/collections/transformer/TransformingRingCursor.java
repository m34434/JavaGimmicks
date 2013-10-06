/**
 * 
 */
package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.AbstractRingCursor;
import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.lang.Transformer;
import net.sf.javagimmicks.lang.Transforming;

class TransformingRingCursor<F, T>
   extends AbstractRingCursor<T>
   implements Transforming<F, T>
{
   protected final RingCursor<F> _internalRingCursor;
   private final Transformer<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingRingCursor(RingCursor<F> ringCursor, Transformer<F, T> transformer)
   {
      _internalRingCursor = ringCursor;
      _transformer = transformer;
   }
   
   public Transformer<F, T> getTransformer()
   {
      return _transformer;
   }
   
   public int size()
   {
      return _internalRingCursor.size();
   }

   public T get()
   {
      return getTransformer().transform(_internalRingCursor.get());
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
      return getTransformer().transform(_internalRingCursor.next());
   }

   public T previous()
   {
      return getTransformer().transform(_internalRingCursor.previous());
   }

   public T remove()
   {
      return getTransformer().transform(_internalRingCursor.remove());
   }

   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRingCursor.cursor(), getTransformer());
   }

}