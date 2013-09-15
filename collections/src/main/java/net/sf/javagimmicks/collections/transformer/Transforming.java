package net.sf.javagimmicks.collections.transformer;

public interface Transforming<F, T>
{
	public Transformer<F, T> getTransformer();
}
