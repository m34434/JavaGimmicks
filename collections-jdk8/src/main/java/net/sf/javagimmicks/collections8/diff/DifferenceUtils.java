package net.sf.javagimmicks.collections8.diff;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections8.diff.Difference.Range;

/**
 * This class serves as central entry point into the diff API and provides some
 * additional helper methods around it.
 * <p>
 * The main methods <code>findDifferences()</code> take as arguments two (typed)
 * {@link List}s and internally compare them with the LCS (longest common
 * subsequences) algorithm. The resulting differences between the {@link List}s
 * are returned in form of a {@link DifferenceList} object, which is actually a
 * {@link List} of {@link Difference} objects (but containing some more specific
 * logic). Each one of the {@link Difference} objects carries detailed
 * information about one single difference between the two compared {@link List}
 * s, like the start and end index of a deletion and/or addition or the changed
 * elements.
 * <p>
 * A {@link Comparator} may be passed as an additional parameter, in order to
 * compare elements of the respective type. If the elements are not comparable
 * and no {@link Comparator} is passed, they must implement
 * {@link #equals(Object)} and {@link #hashCode()} in order to make the
 * comparison work.
 * <p>
 * This implementation class for the actual algorithm is strongly based upon the
 * <code>Diff</code> class from the <i>java-diff</i> project on <a
 * href="http://www.incava.org/">http://www.incava.org</a> (which seems to be
 * offline in between). That class was refactored, adapted to Java 5 and is now
 * used here.
 * </p>
 */
public class DifferenceUtils
{
   /**
    * Finds the differences between two provided {@link List}s using the
    * "longest common subsequences" algorithm.
    * 
    * @param <T>
    *           the element type for the provided {@link List}s
    * @param fromList
    *           the first {@link List} to be analyzed (called 'from' list)
    * @param toList
    *           the second {@link List} to be analyzed (called 'to' list)
    * @return the differences between the two {@link List}s encapsulated in a
    *         {@link DifferenceList} object
    */
   public static <T> DifferenceList<T> findDifferences(final List<T> fromList, final List<T> toList)
   {
      return new DifferenceAlgorithm<T>(fromList, toList, null).getDifferences();
   }

   /**
    * Finds the differences between two provided arrays using the
    * "longest common subsequences" algorithm.
    * 
    * @param <T>
    *           the element type for the provided arrays
    * @param fromArray
    *           the first arrays to be analyzed (called 'from' list)
    * @param toArray
    *           the second arrays to be analyzed (called 'to' list)
    * @return the differences between the two arrays encapsulated in a
    *         {@link DifferenceList} object
    */
   public static <T> DifferenceList<T> findDifferences(final T[] fromArray, final T[] toArray)
   {
      return findDifferences(Arrays.asList(fromArray), Arrays.asList(toArray));
   }

   /**
    * Finds the differences between two provided {@link List}s using the
    * "longest common subsequences" algorithm.
    * 
    * @param <T>
    *           the element type for the provided {@link List}s
    * @param fromList
    *           the first {@link List} to be analyzed (called 'from' list)
    * @param toList
    *           the second {@link List} to be analyzed (called 'to' list)
    * @param comparator
    *           a {@link Comparator} able to compare elements of the respective
    *           type
    * @return the differences between the two {@link List}s encapsulated in a
    *         {@link DifferenceList} object
    */
   public static <T> DifferenceList<T> findDifferences(final List<T> fromList, final List<T> toList,
         final Comparator<T> comparator)
   {
      return new DifferenceAlgorithm<T>(fromList, toList, comparator).getDifferences();
   }

   /**
    * Finds the differences between two provided arrays using the
    * "longest common subsequences" algorithm.
    * 
    * @param <T>
    *           the element type for the provided arrays
    * @param fromArray
    *           the first arrays to be analyzed (called 'from' list)
    * @param toArray
    *           the second arrays to be analyzed (called 'to' list)
    * @param comparator
    *           a {@link Comparator} able to compare elements of the respective
    *           type
    * @return the differences between the two arrays encapsulated in a
    *         {@link DifferenceList} object
    */
   public static <T> DifferenceList<T> findDifferences(final T[] fromArray, final T[] toArray,
         final Comparator<T> comparator)
   {
      return findDifferences(Arrays.asList(fromArray), Arrays.asList(toArray), comparator);
   }

   /**
    * Returns an inverted {@link Difference} object for a given one. Inverted
    * means that delete and add information are exchanged.
    * 
    * @param <T>
    *           the element type of the {@link Difference} object
    * @param difference
    *           the {@link Difference} object to invert
    * @return an inverted version of the provided {@link Difference} object
    *         using the original one in background
    */
   static <T> Difference<T> getInvertedDifference(final Difference<T> difference)
   {
      return new InvertedDifference<T>(difference);
   }

   /**
    * Returns an inverted {@link DifferenceList} object for a given one.
    * Inverted means that delete and add information are exchanged.
    * 
    * @param <T>
    *           the element type of the {@link DifferenceList} object
    * @param differenceList
    *           the {@link DifferenceList} object to invert
    * @return an inverted version of the provided {@link DifferenceList} object
    *         using the original one in background
    */
   static <T> DifferenceList<T> getInvertedDifferenceList(final DifferenceList<T> differenceList)
   {
      return new InvertedDifferenceList<T>(differenceList);
   }

   static <T> void applyDifference(final Difference<T> d, final List<T> targetList)
   {
      final Range<T> deleteRange = d.deleteRange();
      final Range<T> addRange = d.addRange();

      if (deleteRange.exists())
      {
         // Get a ListIterator at the delete position
         final ListIterator<T> iterator = targetList.listIterator(deleteRange.getStartIndex());

         // Delete as many elements as are contained in the delete list
         for (int i = 0; i < deleteRange.size(); ++i)
         {
            iterator.next();
            iterator.remove();
         }
      }

      if (addRange.exists())
      {
         // Adding is easy as we have addAll(); just use the right index and add
         // the complete AddList
         targetList.addAll(deleteRange.getStartIndex(), addRange);
      }
   }

   /**
    * Applies the difference information contained in a given
    * {@link DifferenceList} object to a target {@link List}. The means: apply
    * step by step (in reverse order) all {@link Difference} object inside of
    * the {@link DifferenceList}. If a {@link List} with the same elements like
    * the original from {@link List} is provided here, it will have after
    * execution the same elements like the original to {@link List}.
    * 
    * @param <T>
    *           the element type of the {@link DifferenceList} object and target
    *           {@link List}
    * @param diffList
    *           the {@link DifferenceList} object to apply
    * @param targetList
    *           the {@link List} where to apply the changes
    */
   static <T> void applyDifferenceList(final DifferenceList<T> diffList, final List<T> targetList)
   {
      // ATTENTION: In any case, apply in reverse order; otherwise would cause
      // data corruption
      final ListIterator<Difference<T>> iterator = diffList.listIterator(diffList.size());
      while (iterator.hasPrevious())
      {
         applyDifference(iterator.previous(), targetList);
      }
   }

   static String toString(final Difference<?> d)
   {
      final Range<?> deleteRange = d.deleteRange();
      final Range<?> addRange = d.addRange();

      return new StringBuilder()
            .append("del(")
            .append(deleteRange.getStartIndex())
            .append(", ")
            .append(deleteRange.getEndIndex())
            .append(")")
            .append("|")
            .append("add(")
            .append(addRange.getStartIndex())
            .append(", ")
            .append(addRange.getEndIndex())
            .append(")")
            .toString();
   }

   private static class InvertedDifference<T> implements Difference<T>
   {
      protected final Difference<T> _original;

      public InvertedDifference(final Difference<T> original)
      {
         _original = original;
      }

      @Override
      public Range<T> deleteRange()
      {
         return _original.addRange();
      }

      @Override
      public Range<T> addRange()
      {
         return _original.deleteRange();
      }

      @Override
      public Difference<T> invert()
      {
         return _original;
      }

      @Override
      public String toString()
      {
         return DifferenceUtils.toString(this);
      }
   }

   private static class InvertedDifferenceList<T> extends AbstractList<Difference<T>> implements DifferenceList<T>
   {
      protected final DifferenceList<T> _original;

      public InvertedDifferenceList(final DifferenceList<T> original)
      {
         _original = original;
      }

      @Override
      public void add(final int index, final Difference<T> element)
      {
         _original.add(index, element.invert());
      }

      @Override
      public Difference<T> get(final int index)
      {
         return _original.get(index).invert();
      }

      @Override
      public Difference<T> remove(final int index)
      {
         return _original.remove(index).invert();
      }

      @Override
      public Difference<T> set(final int index, final Difference<T> element)
      {
         return _original.set(index, element.invert()).invert();
      }

      @Override
      public void applyTo(final List<T> list)
      {
         DifferenceUtils.applyDifferenceList(this, list);
      }

      @Override
      public DifferenceList<T> invert()
      {
         return _original;
      }

      @Override
      public int size()
      {
         return _original.size();
      }
   }
}
