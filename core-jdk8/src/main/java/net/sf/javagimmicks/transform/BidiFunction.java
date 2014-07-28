package net.sf.javagimmicks.transform;

import java.util.function.Function;

import javax.xml.transform.Transformer;

/**
 * A bidirectional version of {@link Transformer} that is able to transform
 * object back into their original value and/or format.
 */
public interface BidiFunction<F, T> extends Function<F, T>
{
   /**
    * Transforms a give (transformed object) back into the source value and/or
    * format.
    * 
    * @param source
    *           the source object to be transformed back
    * @return the back-transformed object
    */
   public F applyReverse(T source);

   /**
    * Returns a new {@link BidiTransformer} that transforms in exactly the
    * different directions.
    * 
    * @return an inverted version of this {@link BidiTransformer}
    */
   default BidiFunction<T, F> invert()
   {
      return Transformers.invert(this);
   }
}
