package net.sf.javagimmicks.collections.transformer;

public class DefaultBidiTransformer<F, T> extends AbstractBidiTransformer<F, T>
{
   protected final Transformer<F, T> _transformer;
   protected final Transformer<T, F> _backTransformer;
   
   public DefaultBidiTransformer(Transformer<F, T> transformer, Transformer<T, F> backTransformer)
   {
      _transformer = transformer;
      _backTransformer = backTransformer;
   }

   public F transformBack(T source)
   {
      return _backTransformer.transform(source);
   }
   
   public T transform(F source)
   {
      return _transformer.transform(source);
   }
   
}
