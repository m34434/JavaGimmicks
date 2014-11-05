package net.sf.javagimmicks.transform;

/**
 * A basic class for writing {@link BidiFunction}s - provides a default
 * implementation of {@link #invert()}, so that the developer only needs to
 * provide {@link #apply(Object)} and {@link #applyReverse(Object)}.
 */
public abstract class AbstractBidiFunction<F, T> implements BidiFunction<F, T>
{
   protected AbstractBidiFunction()
   {}

   @Override
   public BidiFunction<T, F> invert()
   {
      return Functions.invert(this);
   }
}
