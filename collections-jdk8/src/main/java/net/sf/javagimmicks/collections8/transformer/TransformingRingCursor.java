/**
 * 
 */
package net.sf.javagimmicks.collections8.transformer;

import java.util.function.Function;

import net.sf.javagimmicks.collections8.RingCursor;
import net.sf.javagimmicks.transform.Transforming;

class TransformingRingCursor<F, T>
   implements Transforming<F, T>, RingCursor<T>
{
   protected final RingCursor<F> _internalRingCursor;
   private final Function<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingRingCursor(RingCursor<F> ringCursor, Function<F, T> transformer)
   {
      _internalRingCursor = ringCursor;
      _transformer = transformer;
   }
   
   public Function<F, T> getTransformer()
   {
      return _transformer;
   }
   
   public int size()
   {
      return _internalRingCursor.size();
   }

   public T get()
   {
      return getTransformer().apply(_internalRingCursor.get());
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
      return getTransformer().apply(_internalRingCursor.next());
   }

   public T previous()
   {
      return getTransformer().apply(_internalRingCursor.previous());
   }

   public T remove()
   {
      return getTransformer().apply(_internalRingCursor.remove());
   }

   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRingCursor.cursor(), getTransformer());
   }

}