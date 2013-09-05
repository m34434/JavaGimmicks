package net.sf.javagimmicks.collections;

public class ArrayRingTest extends AbstractRingTest
{
	protected Ring<String> createRing()
	{
		return new ArrayRing<String>();
	}
}
