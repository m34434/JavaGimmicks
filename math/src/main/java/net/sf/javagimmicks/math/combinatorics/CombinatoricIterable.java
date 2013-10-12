package net.sf.javagimmicks.math.combinatorics;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} that performs a combinatoric algorithm on a given set of
 * elements and allows to iterate over the resulting tuples.
 * 
 * @param <T>
 *           the type of elements that this instance operates on
 */
public abstract class CombinatoricIterable<T> implements Iterable<List<T>>
{
   protected final List<T> _elements;
   protected final BigInteger _size;
   protected final int _tupleSize;

   protected CombinatoricIterable(final Collection<T> elements, final int tupleSize)
   {
      if (tupleSize < 0)
      {
         throw new IllegalArgumentException("Size of combinations to create must at least 0!");
      }
      _tupleSize = tupleSize;

      _elements = new ArrayList<T>(elements);
      _size = calculateTotal(elements.size(), tupleSize);
   }

   protected CombinatoricIterable(final T[] elements, final int r)
   {
      this(Arrays.asList(elements), r);
   }

   /**
    * Returns the total number of tuples that this instance will create.
    * 
    * @return the total number of tuples that this instance will create
    */
   public BigInteger size()
   {
      return _size;
   }

   /**
    * Returns the size of the tuples that this instance will create.
    * 
    * @return the size of the tuples that this instance will create
    */
   public int getTupleSize()
   {
      return _tupleSize;
   }

   /**
    * Returns the base elements used for creating tuples.
    * 
    * @return the base elements used for creating tuples
    */
   public List<T> getElements()
   {
      return Collections.unmodifiableList(_elements);
   }

   @Override
   public Iterator<List<T>> iterator()
   {
      return new CombinationIterator();
   }

   abstract protected BigInteger calculateTotal(int elementCount, int tupleSize);

   abstract protected void computeNext(int[] indices);

   protected void initialiseIndices(final int[] indices)
   {
      for (int i = 0; i < indices.length; i++)
      {
         indices[i] = i;
      }
   }

   private class CombinationIterator implements Iterator<List<T>>
   {
      private final int[] _indices;
      private BigInteger _numLeft = _size;

      private CombinationIterator()
      {
         _indices = new int[_tupleSize];
         initialiseIndices(_indices);
      }

      @Override
      public boolean hasNext()
      {
         return _numLeft.compareTo(ZERO) == 1;
      }

      @Override
      public List<T> next()
      {
         if (!_numLeft.equals(_size))
         {
            computeNext(_indices);
         }
         _numLeft = _numLeft.subtract(ONE);
         return getResult(_indices);
      }

      @Override
      public void remove()
      {
         throw new UnsupportedOperationException();
      }
   }

   private List<T> getResult(final int[] indices)
   {
      final ArrayList<T> result = new ArrayList<T>(indices.length);
      for (final int index : indices)
      {
         result.add(_elements.get(index));
      }
      return Collections.unmodifiableList(result);
   }
}
