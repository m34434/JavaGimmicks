package net.sf.javagimmicks.collections.transformer;

import net.sf.javagimmicks.collections.AbstractRing;
import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.RingCursor;
import net.sf.javagimmicks.transform.Transformer;
import net.sf.javagimmicks.transform.Transforming;

class TransformingRing<F, T>
   extends AbstractRing<T>
   implements Transforming<F, T>
{
   protected final Ring<F> _internalRing;
   private final Transformer<F, T> _tansformer;

   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingRing(Ring<F> ring, Transformer<F, T> tansformer)
   {
      _internalRing = ring;
      _tansformer = tansformer;
   }
   
   public Transformer<F, T> getTransformer()
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
