package net.sf.javagimmicks.collections8;

import net.sf.javagimmicks.collections8.ArrayRing;
import net.sf.javagimmicks.collections8.Ring;

public class ArrayRingTest extends AbstractRingTest
{
	protected Ring<String> createRing()
	{
		return new ArrayRing<String>();
	}
}
