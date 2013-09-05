package net.sf.javagimmicks.collections.transformer;

public interface BidiTransforming<F, T> extends Transforming<F, T>
{
	public BidiTransformer<F, T> getBidiTransformer();
}
