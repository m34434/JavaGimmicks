package net.sf.javagimmicks.transform8;

import java.util.function.Function;

/**
 * A bidirectional version of {@link Function} that is able to transform object
 * back into their original value and/or format.
 * 
 * @param <F>
 *           the "from" or source type
 * @param <T>
 *           the "to" or target type
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
    * Returns a new {@link BidiFunction} that transforms in exactly the
    * different directions.
    * 
    * @return an inverted version of this {@link BidiFunction}
    */
   default BidiFunction<T, F> invert()
   {
      return new BidiFunction<T, F>()
      {
         @Override
         public T applyReverse(final F source)
         {
            return BidiFunction.this.apply(source);
         }

         @Override
         public F apply(final T source)
         {
            return BidiFunction.this.applyReverse(source);
         }

         @Override
         public BidiFunction<F, T> invert()
         {
            return BidiFunction.this;
         }
      };
   }

   /**
    * Creates a new {@link BidiFunction} from two given {@link Function}s (which
    * should perform contrary operations).
    * 
    * @param forwardFunction
    *           the "forward" {@link Function}
    * @param backwardFunction
    *           the "backward" {@link Function} which should perform the
    *           contrary operation to the "forward" one
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return a resulting {@link BidiFunction}
    */
   static <F, T> BidiFunction<F, T> create(final Function<F, T> forwardFunction, final Function<T, F> backwardFunction)
   {
      return new BidiFunction<F, T>()
      {
         @Override
         public T apply(final F source)
         {
            return forwardFunction.apply(source);
         }

         @Override
         public F applyReverse(final T source)
         {
            return backwardFunction.apply(source);
         }
      };
   }
}
