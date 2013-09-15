package net.sf.javagimmicks.collections.diff;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DifferenceUtilsTest
{
   @Test
	public void testMissing()
	{
		String[] from = new String[] {"a", "b", "c", "d", "e", "f"};
		String[] to = new String[] {"b", "e"};
		DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);
		
		assertEquals(3, d.size());
		assertDifference(d.get(0), 0, 0, 0, Difference.NONE);
		assertDifference(d.get(1), 2, 3, 1, Difference.NONE);
		assertDifference(d.get(2), 5, 5, 2, Difference.NONE);
	}
	
   @Test
	public void testNew()
	{
		String[] from = new String[] {"b", "e"};
		String[] to = new String[] {"a", "b", "c", "d", "e", "f"};
		DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

		assertEquals(3, d.size());
		assertDifference(d.get(0), 0, Difference.NONE, 0, 0);
		assertDifference(d.get(1), 1, Difference.NONE, 2, 3);
		assertDifference(d.get(2), 2, Difference.NONE, 5, 5);
	}
	
   @Test
	public void testChange()
	{
		String[] from = new String[] {"a", "b", "c", "d", "e", "f"};
		String[] to = new String[] {"w", "b", "x", "y", "e", "z"};
		DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

		assertEquals(3, d.size());
		assertDifference(d.get(0), 0, 0, 0, 0);
		assertDifference(d.get(1), 2, 3, 2, 3);
		assertDifference(d.get(2), 5, 5, 5, 5);
	}
	
   @Test
	public void testApplyDifferenceList()
	{
		List<String> from = Arrays.asList(new String[] {"a", "b", "c"});
		List<String> to = Arrays.asList(new String[] {"w", "a", "x", "b", "y", "c", "z"});

		DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);
		
		assertEquals(4, d.size());
		
		// Make a copy because the difference list uses the FROM list in background
		List<String> fromCopy = new ArrayList<String>(from);
		d.applyTo(fromCopy);
		
		assertEquals(to, fromCopy);
	}
	
   @Test
	public void testApplyDifferenceListInverted()
	{
		List<String> from = Arrays.asList(new String[] {"a", "b", "c"});
		List<String> to = Arrays.asList(new String[] {"w", "a", "x", "b", "y", "c", "z"});

		DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);
		
		assertEquals(4, d.size());
		
		// Make a copy because the difference list uses the TO list in background
		List<String> toCopy = new ArrayList<String>(to);
		d.invert().applyTo(toCopy);
		
		assertEquals(from, toCopy);
	}
	
	protected static void assertDifference(Difference<?> d, int delStart, int delEnd, int addStart, int addEnd)
	{
		assertEquals(delStart, d.getDeleteStartIndex());
		assertEquals(delEnd, d.getDeleteEndIndex());
		assertEquals(addStart, d.getAddStartIndex());
		assertEquals(addEnd, d.getAddEndIndex());
	}
}
