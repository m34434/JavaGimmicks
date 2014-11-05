package net.sf.javagimmicks.transform;

import net.sf.javagimmicks.util.Function;



class DualFunctionBidiFunction<F, T> extends AbstractBidiFunction<F, T>
{
   protected final Function<F, T> _transformer;
   protected final Function<T, F> _backTransformer;

   public DualFunctionBidiFunction(final Function<F, T> transformer, final Function<T, F> backTransformer)
   {
      _transformer = transformer;
      _backTransformer = backTransformer;
   }

   @Override
   public F applyReverse(final T source)
   {
      return _backTransformer.apply(source);
   }

   @Override
   public T apply(final F source)
   {
      return _transformer.apply(source);
   }

}
