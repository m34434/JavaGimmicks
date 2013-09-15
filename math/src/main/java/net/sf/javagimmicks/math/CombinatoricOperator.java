package net.sf.javagimmicks.math;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class CombinatoricOperator<T> implements
		Iterator<ArrayList<T>>, Iterable<ArrayList<T>>
{

	/**
	 * Initialise a new operator, with given elements and size of the arrays to
	 * be returned.
	 * 
	 * @param elements
	 *            The elements on which this combinatoric operator has to act.
	 * @param r
	 *            The size of the arrays to compute.
	 */
	protected CombinatoricOperator(T[] elements, int r)
	{
		this(Arrays.asList(elements), r);
	}

	/**
	 * Initialise a new operator, with given elements and size of the arrays to
	 * be returned.
	 * 
	 * @param elements
	 *            The elements on which this combinatoric operator has to act.
	 * @param r
	 *            The size of the arrays to compute.
	 * @pre r should not be smaller than 0. | 0 <= r
	 * @post The total number of iterations is set to the correct number. |
	 *       new.getTotal() == initialiseTotal();
	 * @post The number of variations left is set to the total number. |
	 *       new.getNumLeft() == new.getTotal()
	 */
	protected CombinatoricOperator(Collection<T> elements, int r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException(
					"Size of lists to create must at least 0!");
		}
		indices = new int[r];
		this.elements = new ArrayList<T>(elements);
		total = calculateTotal(elements.size(), r);
		reset();
	}

	/**
	 * The elements the operator works upon.
	 */
	protected List<T> elements;

	/**
	 * An integer array backing up the original one to keep track of the
	 * indices.
	 */
	protected int[] indices;

	/**
	 * Initialise the array of indices. By default, it is initialised with
	 * incrementing integers.
	 */
	protected void initialiseIndices()
	{
		for (int i = 0; i < indices.length; i++)
		{
			indices[i] = i;
		}
	}

	/**
	 * The variations still to go.
	 */
	private BigInteger numLeft;

	/**
	 * The total number of variations to be computed.
	 */
	private BigInteger total;

	/**
	 * Compute the total number of elements to return.
	 * 
	 * @param n
	 *            The number of elements the operator works on.
	 * @param r
	 *            The size of the arrays to return.
	 * @return The total number of elements is always bigger than 0. | result >=
	 *         0
	 */
	protected abstract BigInteger calculateTotal(int n, int r);

	/**
	 * Reset the iteration.
	 */
	public void reset()
	{
		initialiseIndices();
		numLeft = total;
	}

	/**
	 * Return number of variations not yet generated.
	 */
	public BigInteger getNumLeft()
	{
		return numLeft;
	}

	/**
	 * Return the total number of variations.
	 * 
	 * @return The factorial of the number of elements divided by the factorials
	 *         of the size of the variations and the number of elements minus
	 *         the size of the variations. That is, with the number of elements =
	 *         n and the size of the variations = r: n^r
	 */
	public BigInteger getTotal()
	{
		return total;
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. This is the
	 * case if not all n! permutations have been covered.
	 * 
	 * @return The number of permutations to go is bigger than zero. | result ==
	 *         getNumLeft().compareTo(BigInteger.ZERO) > 0;
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return numLeft.compareTo(ZERO) == 1;
	}

	/**
	 * Compute the next combination.
	 * 
	 * @see java.util.Iterator#next()
	 */
	public ArrayList<T> next()
	{
		if (!numLeft.equals(total))
		{
			computeNext();
		}
		numLeft = numLeft.subtract(ONE);
		return getResult(indices);
	}

	/**
	 * Compute the next array of indices.
	 */
	protected abstract void computeNext();

	/**
	 * Compute the result, based on the given array of indices.
	 * 
	 * @param indexes
	 *            An array of indices into the element array.
	 * @return An array consisting of the elements at positions of the given
	 *         array. | result[i] == elements[indexes[i]]
	 */
	private ArrayList<T> getResult(int[] indexes)
	{
		ArrayList<T> result = new ArrayList<T>(indexes.length);
		for (int i = 0; i < indexes.length; i++)
		{
			result.add(elements.get(indexes[i]));
		}
		return result;
	}

	/**
	 * Not supported.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * A combinatoric operator is itself an iterator.
	 * 
	 * @return Itself. | result == this
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<ArrayList<T>> iterator()
	{
		return this;
	}

	/**
	 * Compute the factorial of n.
	 */
	public static BigInteger factorial(int n)
	{
		BigInteger fact = ONE;
		for (int i = n; i > 1; i--)
		{
			fact = fact.multiply(BigInteger.valueOf(i));
		}
		return fact;
	}
}
