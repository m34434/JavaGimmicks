package net.sf.javagimmicks.collections8.diff;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.javagimmicks.collections8.diff.Difference;
import net.sf.javagimmicks.collections8.diff.DifferenceList;
import net.sf.javagimmicks.collections8.diff.DifferenceUtils;
import net.sf.javagimmicks.collections8.diff.Difference.Range;

import org.junit.Test;

public class DifferenceUtilsTest
{
   @Test
   public void testMissing()
   {
      final String[] from = new String[] { "a", "b", "c", "d", "e", "f" };
      final String[] to = new String[] { "b", "e" };
      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(3, d.size());
      assertDifference(d.get(0), 0, 0, 0, Difference.NONE);
      assertDifference(d.get(1), 2, 3, 1, Difference.NONE);
      assertDifference(d.get(2), 5, 5, 2, Difference.NONE);
   }

   @Test
   public void testNew()
   {
      final String[] from = new String[] { "b", "e" };
      final String[] to = new String[] { "a", "b", "c", "d", "e", "f" };
      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(3, d.size());
      assertDifference(d.get(0), 0, Difference.NONE, 0, 0);
      assertDifference(d.get(1), 1, Difference.NONE, 2, 3);
      assertDifference(d.get(2), 2, Difference.NONE, 5, 5);
   }

   @Test
   public void testChange()
   {
      final String[] from = new String[] { "a", "b", "c", "d", "e", "f" };
      final String[] to = new String[] { "w", "b", "x", "y", "e", "z" };
      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(3, d.size());
      assertDifference(d.get(0), 0, 0, 0, 0);
      assertDifference(d.get(1), 2, 3, 2, 3);
      assertDifference(d.get(2), 5, 5, 5, 5);
   }

   @Test
   public void testChange2()
   {
      final String[] from = new String[] { "1", "2", "3", "4" };
      final String[] to = new String[] { "1", "x", "y", "z", "4" };
      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(1, d.size());
      assertDifference(d.get(0), 1, 2, 1, 3);
   }

   @Test
   public void testApplyDifferenceList()
   {
      final List<String> from = Arrays.asList(new String[] { "a", "b", "c" });
      final List<String> to = Arrays.asList(new String[] { "w", "a", "x", "b", "y", "c", "z" });

      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(4, d.size());

      // Make a copy because the difference list uses the FROM list in
      // background
      final List<String> fromCopy = new ArrayList<String>(from);
      d.applyTo(fromCopy);

      assertEquals(to, fromCopy);
   }

   @Test
   public void testApplyDifferenceListInverted()
   {
      final List<String> from = Arrays.asList(new String[] { "a", "b", "c" });
      final List<String> to = Arrays.asList(new String[] { "w", "a", "x", "b", "y", "c", "z" });

      final DifferenceList<String> d = DifferenceUtils.findDifferences(from, to);

      assertEquals(4, d.size());

      // Make a copy because the difference list uses the TO list in background
      final List<String> toCopy = new ArrayList<String>(to);
      d.invert().applyTo(toCopy);

      assertEquals(from, toCopy);
   }

   protected static void assertDifference(final Difference<?> d, final int delStart, final int delEnd,
         final int addStart, final int addEnd)
   {
      final Range<?> deleteRange = d.deleteRange();
      final Range<?> addRange = d.addRange();

      assertEquals("Start index of delete range does not match", delStart, deleteRange.getStartIndex());
      assertEquals("End index of delete range does not match", delEnd, deleteRange.getEndIndex());
      assertEquals("Start index of add range does not match", addStart, addRange.getStartIndex());
      assertEquals("End index of add range does not match", addEnd, addRange.getEndIndex());
   }
}
