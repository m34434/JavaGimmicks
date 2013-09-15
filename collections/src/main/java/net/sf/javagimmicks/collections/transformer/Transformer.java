package net.sf.javagimmicks.collections.transformer;

public interface Transformer<F, T>
{
   public T transform(F source);
}
