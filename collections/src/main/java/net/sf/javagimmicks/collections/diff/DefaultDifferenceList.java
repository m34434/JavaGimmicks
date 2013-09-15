package net.sf.javagimmicks.collections.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultDifferenceList<T> extends ArrayList<Difference<T>> implements DifferenceList<T>
{
	private static final long serialVersionUID = -8782622138787742405L;

	public DefaultDifferenceList()
	{
		super();
	}

	public DefaultDifferenceList(Collection<? extends Difference<T>> c)
	{
		super(c);
	}

	public DefaultDifferenceList(int initialCapacity)
	{
		super(initialCapacity);
	}

	public void applyTo(List<T> list)
	{
		DifferenceUtils.applyDifferenceList(this, list);
	}

	public DifferenceList<T> invert()
	{
		return DifferenceUtils.getInvertedDifferenceList(this);
	}
}
