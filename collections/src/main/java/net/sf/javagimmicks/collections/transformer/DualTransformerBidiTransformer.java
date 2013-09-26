package net.sf.javagimmicks.collections.transformer;

class DualTransformerBidiTransformer<F, T> extends AbstractBidiTransformer<F, T>
{
   protected final Transformer<F, T> _transformer;
   protected final Transformer<T, F> _backTransformer;

   public DualTransformerBidiTransformer(final Transformer<F, T> transformer, final Transformer<T, F> backTransformer)
   {
      _transformer = transformer;
      _backTransformer = backTransformer;
   }

   @Override
   public F transformBack(final T source)
   {
      return _backTransformer.transform(source);
   }

   @Override
   public T transform(final F source)
   {
      return _transformer.transform(source);
   }

}
