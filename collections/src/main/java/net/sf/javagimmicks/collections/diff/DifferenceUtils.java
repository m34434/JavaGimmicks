package net.sf.javagimmicks.collections.diff;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class serves as central entry point into the diff API and provides some additional
 * helper methods around it. The main methods <code>findDifferences()</code> take two 
 * (typed) {@link List}s as arguments and internally compare them with the
 * LCS (longest common subsequences) algorithm.
 * The resulting differences between the {@link List}s are returned in form of a
 * {@link DifferenceList} object, which is actually a {@link List} of {@link Difference}
 * objects (but containing some more specific logic).
 * Each one of the {@link Difference} objects carries detailed information about one single
 * difference between the two compared {@link List}s, like the start and end index of a
 * deletion and/or addition or the changed elements.
 * A {@link Comparator} may be passed as an additional parameter, in order to compare
 * elements of the respective type. If the elements are not comparable and no
 * {@link Comparator} is passed, they must implement {@link #equals(Object)} and {@link #hashCode()}
 * in order to make the comparison work.
 * <p>
 * This implementation class for the actual algorithm is strongly based upon the
 * <code>Diff</code> class from the <i>java-diff</i> project on
 * <a href="http://www.incava.org/">http://www.incava.org</a>.
 * That class was refactored, adapted to Java 5 and is now used here.
 * </p>
 */
public class DifferenceUtils
{
	/**
	 * Finds the differences between two provided {@link List}s using the "longest common subsequences" algorithm.
	 * @param <T> the element type for the provided {@link List}s
	 * @param fromList the first {@link List} to be analyzed (called 'from' list)
	 * @param toList the second {@link List} to be analyzed (called 'to' list)
	 * @return the differences between the two {@link List}s encapsulated in a {@link DifferenceList} object
	 */
	public static <T> DifferenceList<T> findDifferences(List<T> fromList, List<T> toList)
	{
		return new DifferenceAlgorithm<T>(fromList, toList, null).getDifferences();
	}
	
	/**
	 * Finds the differences between two provided arrays using the "longest common subsequences" algorithm.
	 * @param <T> the element type for the provided arrays
	 * @param fromArray the first arrays to be analyzed (called 'from' list)
	 * @param toArray the second arrays to be analyzed (called 'to' list)
	 * @return the differences between the two arrays encapsulated in a {@link DifferenceList} object
	 */
	public static <T> DifferenceList<T> findDifferences(T[] fromArray, T[] toArray)
	{
		return findDifferences(Arrays.asList(fromArray), Arrays.asList(toArray));
	}
	
	/**
	 * Finds the differences between two provided {@link List}s using the "longest common subsequences" algorithm.
	 * @param <T> the element type for the provided {@link List}s
	 * @param fromList the first {@link List} to be analyzed (called 'from' list)
	 * @param toList the second {@link List} to be analyzed (called 'to' list)
	 * @param comparator a {@link Comparator} able to compare elements of the respective type
	 * @return the differences between the two {@link List}s encapsulated in a {@link DifferenceList} object
	 */
	public static <T> DifferenceList<T> findDifferences(List<T> fromList, List<T> toList, Comparator<T> comparator)
	{
		return new DifferenceAlgorithm<T>(fromList, toList, comparator).getDifferences();
	}
	
	/**
	 * Finds the differences between two provided arrays using the "longest common subsequences" algorithm.
	 * @param <T> the element type for the provided arrays
	 * @param fromArray the first arrays to be analyzed (called 'from' list)
	 * @param toArray the second arrays to be analyzed (called 'to' list)
	 * @param comparator a {@link Comparator} able to compare elements of the respective type
	 * @return the differences between the two arrays encapsulated in a {@link DifferenceList} object
	 */
	public static <T> DifferenceList<T> findDifferences(T[] fromArray, T[] toArray, Comparator<T> comparator)
	{
		return findDifferences(Arrays.asList(fromArray), Arrays.asList(toArray), comparator);
	}
	
	/**
	 * Returns an inverted {@link Difference} object for a given one.
	 * Inverted means that delete and add information are exchanged.
	 * @param <T> the element type of the {@link Difference} object
	 * @param difference the {@link Difference} object to invert
	 * @return an inverted version of the provided {@link Difference} object using the original
	 * one in background
	 */
	public static <T> Difference<T> getInvertedDifference(Difference<T> difference)
	{
		return new InvertedDifference<T>(difference);
	}
	
	/**
	 * Returns an inverted {@link DifferenceList} object for a given one.
	 * Inverted means that delete and add information are exchanged.
	 * @param <T> the element type of the {@link DifferenceList} object
	 * @param differenceList the {@link DifferenceList} object to invert
	 * @return an inverted version of the provided {@link DifferenceList} object using the original
	 * one in background
	 */
	public static <T> DifferenceList<T> getInvertedDifferenceList(DifferenceList<T> differenceList)
	{
		return new InvertedDifferenceList<T>(differenceList);
	}

	/**
	 * Applies the difference information contained in a given
	 * {@link Difference} object to a target {@link List}.
	 * The means: remove there all elements denoted by the "delete" information of the
	 * {@link Difference} object at the right position
	 * and add all elements from the "add" {@link List} at the same position. 
	 * @param <T> the element type of the {@link Difference} object and target {@link List}
	 * @param d the {@link Difference} object to apply
	 * @param targetList the {@link List} where to apply the changes
	 */
	public static <T> void applyDifference(Difference<T> d, List<T> targetList)
	{
		// We need this index for deleting AND adding (to know where to add)
		int deleteStartIndex = d.getDeleteStartIndex();

		if(d.isDelete())
		{
			// Get a ListIterator at the delete position
			ListIterator<T> iterator = targetList.listIterator(deleteStartIndex);
			
			// Delete as many elements as are contained in the delete list
			for(int i = 0; i < d.getDeleteList().size(); ++i)
			{
				iterator.next();
				iterator.remove();
			}
		}
		
		if(d.isAdd())
		{
			// Adding is easy as we have addAll(); just use the right index and add the complete AddList
			targetList.addAll(deleteStartIndex, d.getAddList());
		}
	}

	/**
	 * Applies the difference information contained in a given
	 * {@link DifferenceList} object to a target {@link List}.
	 * The means: apply step by step (in reverse order) all {@link Difference} object inside
	 * of the {@link DifferenceList}.
	 * If a {@link List} with the same elements like the original from {@link List} is provided here,
	 * it will have after execution the same elements like the original to {@link List}.
	 * @param <T> the element type of the {@link DifferenceList} object and target {@link List}
	 * @param diffList the {@link DifferenceList} object to apply
	 * @param targetList the {@link List} where to apply the changes
	 */
	public static <T> void applyDifferenceList(DifferenceList<T> diffList, List<T> targetList)
	{
		// ATTENTION: In any case, apply in reverse order; otherwise would cause data corruption
		ListIterator<Difference<T>> iterator = diffList.listIterator(diffList.size());
		while(iterator.hasPrevious())
		{
			applyDifference(iterator.previous(), targetList);
		}
	}
	
	public static String toString(Difference<?> d)
	{
		return new StringBuilder()
		.append("del(")
		.append(d.getDeleteStartIndex())
		.append(", ")
		.append(d.getDeleteEndIndex())
		.append(")")
		.append("|")
		.append("add(")
		.append(d.getAddStartIndex())
		.append(", ")
		.append(d.getAddEndIndex())
		.append(")")
		.toString();
	}
	
	protected static class InvertedDifference<T> implements Difference<T>
	{
		protected final Difference<T> _original;
		
		protected InvertedDifference(Difference<T> original)
		{
			_original = original;
		}

		public int getAddStartIndex()
		{
			return _original.getDeleteStartIndex();
		}

		public int getAddEndIndex()
		{
			return _original.getDeleteEndIndex();
		}

		public List<T> getAddList()
		{
			return _original.getDeleteList();
		}
		
		public boolean isAdd()
		{
			return _original.isDelete();
		}

		public int getDeleteStartIndex()
		{
			return _original.getAddStartIndex();
		}

		public int getDeleteEndIndex()
		{
			return _original.getAddEndIndex();
		}

		public List<T> getDeleteList()
		{
			return _original.getAddList();
		}

		public boolean isDelete()
		{
			return _original.isAdd();
		}

		public Difference<T> invert()
		{
			return _original;
		}

		public String toString()
		{
			return DifferenceUtils.toString(this);
		}
	}
	
	protected static class InvertedDifferenceList<T> extends AbstractList<Difference<T>> implements DifferenceList<T>
	{
		protected final DifferenceList<T> _original;
		
		protected InvertedDifferenceList(DifferenceList<T> original)
		{
			_original = original;
		}

		@Override
		public void add(int index, Difference<T> element)
		{
			_original.add(index, element.invert());
		}

		@Override
		public Difference<T> get(int index)
		{
			return _original.get(index).invert();
		}

		@Override
		public Difference<T> remove(int index)
		{
			return _original.remove(index).invert();
		}

		@Override
		public Difference<T> set(int index, Difference<T> element)
		{
			return _original.set(index, element.invert()).invert();
		}

		public void applyTo(List<T> list)
		{
			DifferenceUtils.applyDifferenceList(this, list);
		}

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
