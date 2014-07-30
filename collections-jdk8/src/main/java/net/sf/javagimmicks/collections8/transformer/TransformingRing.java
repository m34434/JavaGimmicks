package net.sf.javagimmicks.collections8.transformer;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Function;

import net.sf.javagimmicks.collections8.AbstractRing;
import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;
import net.sf.javagimmicks.transform8.Transforming;

class TransformingRing<F, T>
   extends AbstractRing<T>
   implements Transforming<F, T>
{
   protected final Ring<F> _internalRing;
   private final Function<F, T> _tansformer;

   TransformingRing(Ring<F> ring, Function<F, T> tansformer)
   {
      _internalRing = ring;
      _tansformer = tansformer;
   }
   
   public Function<F, T> getTransformerFunction()
   {
      return _tansformer;
   }

   public RingCursor<T> cursor()
   {
      return TransformerUtils.decorate(_internalRing.cursor(), getTransformerFunction());
   }
   
   public Iterator<T> iterator()
   {
      return TransformerUtils.decorate(_internalRing.iterator(), getTransformerFunction());
   }
   
   @Override
   public Spliterator<T> spliterator()
   {
      return TransformerUtils.decorate(_internalRing.spliterator(), getTransformerFunction());
   }

   @Override
   public int size()
   {
      return _internalRing.size();
   }
}
