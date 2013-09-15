package net.sf.javagimmicks.collections.transformer;

public interface BidiTransformer<F, T> extends Transformer<F, T>
{
   public F transformBack(T source);
   
   public BidiTransformer<T, F> invert();
}
