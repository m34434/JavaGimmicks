/**
 * 
 */
package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.AbstractRingCursor;
import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.Transforming;
import net.sf.javagimmicks.util.Function;

class TransformingRingCursor<F, T>
   extends AbstractRingCursor<T>
   implements Transforming<F, T>
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
   
   public Function<F, T> getTransformerFunction()
   {
      return _transformer;
   }
   
   public int size()
   {
      return _internalRingCursor.size();
   }

   public T get()
   {
      return getTransformerFunction().apply(_internalRingCursor.get());
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
      return getTransformerFunction().apply(_internalRingCursor.next());
   }

   public T previous()
   {
      return getTransformerFunction().apply(_internalRingCursor.previous());
   }

   public T remove()
   {
      return getTransformerFunction().apply(_internalRingCursor.remove());
   }

   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRingCursor.cursor(), getTransformerFunction());
   }

}