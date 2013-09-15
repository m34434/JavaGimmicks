package net.sf.javagimmicks.collections.transformer;

public abstract class AbstractBidiTransformer<F, T> implements BidiTransformer<F, T>
{
   public BidiTransformer<T, F> invert()
   {
      return TransformerUtils.invert(this);
   }
}
