package net.sf.javagimmicks.transform;

/**
 * A bidirectional version of {@link Transformer} that is able to transform
 * object back into their original value and/or format.
 */
public interface BidiTransformer<F, T> extends Transformer<F, T>
{
   /**
    * Transforms a give (transformed object) back into the source value and/or
    * format.
    * 
    * @param source
    *           the source object to be transformed back
    * @return the back-transformed object
    */
   public F transformBack(T source);

   /**
    * Returns a new {@link BidiTransformer} that transforms in exactly the
    * different directions.
    * 
    * @return an inverted version of this {@link BidiTransformer}
    */
   public BidiTransformer<T, F> invert();
}
