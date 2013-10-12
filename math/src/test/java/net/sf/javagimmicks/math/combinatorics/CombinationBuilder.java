package net.sf.javagimmicks.math.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;
import net.sf.javagimmicks.collections.CollectionUtils;

import org.junit.Assert;

public class CombinationBuilder<T>
{
   private final List<List<T>> _combinations = new ArrayList<List<T>>();

   public CombinationBuilder<T> add(final T... combination)
   {
      _combinations.add(Arrays.asList(combination));

      return this;
   }

   public void assertEquals(final Iterable<? extends List<T>> actual)
   {
      final ArrayList<List<T>> actualList = new ArrayList<List<T>>();
      CollectionUtils.addAll(actualList, actual.iterator());

      Assert.assertNotNull("Actual list is null!", actual);
      Assert.assertEquals("Number of combinations differs!", _combinations.size(), actualList.size());

      if (!actualList.containsAll(_combinations))
      {
         throw newAssertionFailed(actualList);
      }
   }

   private AssertionError newAssertionFailed(final ArrayList<List<T>> actual)
   {
      final String message = String.format(
            "Actual list did not contain all expected combinations! Expected: %1$s / actual: %2$s", _combinations,
            actual);

      return new AssertionFailedError(message);
   }
}
