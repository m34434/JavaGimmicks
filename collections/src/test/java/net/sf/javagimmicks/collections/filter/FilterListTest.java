package net.sf.javagimmicks.collections.filter;

import static net.sf.javagimmicks.testing.JUnitListAssert.assertListEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import net.sf.javagimmicks.util.Filter;

import org.junit.Test;

public class FilterListTest
{
   private static final Filter<Integer> evenFilter = new EvenFilter();
   private static final Filter<Integer> oddFilter = new OddFilter();

   @Test
   public void test()
   {
      FilterList<Integer> allList = new FilterList<Integer>(false);
      
      // Add a first bunch of number (1-4)
      for(int i = 1; i <= 4; ++i)
      {
         allList.add(i);
      }
      
      // Create filtered instances (one for odd, two for even numbers) on the FilterList
      List<Integer> evenList = allList.createFilteredList(evenFilter);
      List<Integer> evenList2 = allList.createFilteredList(evenFilter);
      List<Integer> oddList = allList.createFilteredList(oddFilter);
      
      // Test the contents of the master and child lists
      assertListEquals(allList, 1, 2, 3, 4);
      assertListEquals(oddList, 1, 3);
      assertListEquals(evenList, 2, 4);
      assertEquals(evenList, evenList2);
      
      // Add another bunch of numbers (5-8) to the master list
      for(int i = 5; i <= 8; ++i)
      {
         allList.add(i);
      }

      // Check if all lists (master and children) were updated properly
      assertListEquals(allList, 1, 2, 3, 4, 5, 6, 7, 8);
      assertListEquals(oddList, 1, 3, 5, 7);
      assertListEquals(evenList, 2, 4, 6, 8);
      assertEquals(evenList, evenList2);
      
      // Now check removal on child lists
      oddList.remove(0);
      evenList.remove(0);

      assertListEquals(allList, 3, 4, 5, 6, 7, 8);
      assertListEquals(oddList, 3, 5, 7);
      assertListEquals(evenList, 4, 6, 8);
      assertEquals(evenList, evenList2);
      
      // Now check adding on child lists
      oddList.add(9);
      evenList.add(10);

      assertListEquals(allList, 3, 4, 5, 6, 7, 8, 9, 10);
      assertListEquals(oddList, 3, 5, 7, 9);
      assertListEquals(evenList, 4, 6, 8, 10);
      assertEquals(evenList, evenList2);
      
      // And now set() on the master list (it will change two indices between odd and even)
      allList.set(0, 4);
      allList.set(1, 3);

      assertListEquals(allList, 4, 3, 5, 6, 7, 8, 9, 10);
      assertListEquals(oddList, 3, 5, 7, 9);
      assertListEquals(evenList, 4, 6, 8, 10);
      assertEquals(evenList, evenList2);
      
      // Now change set() on the child lists
      oddList.set(0, 1);
      evenList.set(0, 2);

      assertListEquals(allList, 2, 1, 5, 6, 7, 8, 9, 10);
      assertListEquals(oddList, 1, 5, 7, 9);
      assertListEquals(evenList, 2, 6, 8, 10);
      assertEquals(evenList, evenList2);
   }

   private static final class EvenFilter implements Filter<Integer>
   {
      public boolean accepts(Integer element)
      {
         return element != null && element % 2 == 0;
      }
   }

   private static final class OddFilter implements Filter<Integer>
   {
      public boolean accepts(Integer element)
      {
         return element != null && element % 2 == 1;
      }
   }
}
