package net.sf.javagimmicks.collections;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

public class LinkedRingTest extends AbstractRingTest
{
	protected Ring<String> createRing()
	{
		return new LinkedRing<String>();
	}

	public static Test suite()
	{
	   return new JUnit4TestAdapter(LinkedRingTest.class);
	}
}
