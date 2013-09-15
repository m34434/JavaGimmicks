package net.sf.javagimmicks.collections.diff;

import java.util.List;

public interface DifferenceList<T> extends List<Difference<T>>
{
	public DifferenceList<T> invert();
	
	public void applyTo(List<T> list);
}
