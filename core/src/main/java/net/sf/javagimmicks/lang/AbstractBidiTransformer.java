package net.sf.javagimmicks.lang;

/**
 * A basic class for writing {@link BidiTransformer}s - provides a default
 * implementation of {@link #invert()}, so that the developer only needs to
 * provide {@link #transform(Object)} and {@link #transformBack(Object)}.
 */
public abstract class AbstractBidiTransformer<F, T> implements BidiTransformer<F, T>
{
   protected AbstractBidiTransformer()
   {}

   @Override
   public BidiTransformer<T, F> invert()
   {
      return Transformers.invert(this);
   }
}
