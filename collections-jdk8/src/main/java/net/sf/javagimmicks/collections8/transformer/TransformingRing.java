package net.sf.javagimmicks.collections8.transformer;

import java.util.function.Function;

import net.sf.javagimmicks.collections8.AbstractRing;
import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;
import net.sf.javagimmicks.transform.Transforming;

class TransformingRing<F, T>
   extends AbstractRing<T>
   implements Transforming<F, T>
{
   protected final Ring<F> _internalRing;
   private final Function<F, T> _tansformer;

   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingRing(Ring<F> ring, Function<F, T> tansformer)
   {
      _internalRing = ring;
      _tansformer = tansformer;
   }
   
   public Function<F, T> getTransformer()
   {
      return _tansformer;
   }

   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRing.cursor(), getTransformer());
   }

   @Override
   public int size()
   {
      return _internalRing.size();
   }
}
