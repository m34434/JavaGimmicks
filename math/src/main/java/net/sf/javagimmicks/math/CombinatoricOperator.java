package net.sf.javagimmicks.math;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class CombinatoricOperator<T> implements Iterable<ArrayList<T>>
{
   protected final List<T> _elements;
   protected final BigInteger _numTotal;
   protected final int _combinationSize;

   protected CombinatoricOperator(final Collection<T> elements, final int r)
   {
      if (r < 0)
      {
         throw new IllegalArgumentException("Size of combinations to create must at least 0!");
      }
      _combinationSize = r;

      _elements = new ArrayList<T>(elements);
      _numTotal = calculateTotal(elements.size(), r);
   }

   protected CombinatoricOperator(final T[] elements, final int r)
   {
      this(Arrays.asList(elements), r);
   }

   protected abstract BigInteger calculateTotal(int n, int r);

   protected abstract void computeNext(int[] indices);

   protected void initialiseIndices(final int[] indices)
   {
      for (int i = 0; i < indices.length; i++)
      {
         indices[i] = i;
      }
   }

   /**
    * Return the total number of combinations that this
    * {@link CombinatoricOperator} will create.
    * 
    * @return the total number of combinations
    */
   public BigInteger getTotal()
   {
      return _numTotal;
   }

   @Override
   public CombinationIterator iterator()
   {
      return new CombinationIterator();
   }

   public class CombinationIterator implements Iterator<ArrayList<T>>
   {
      private final int[] _indices;
      private BigInteger _numLeft = _numTotal;

      private CombinationIterator()
      {
         _indices = new int[_combinationSize];
         initialiseIndices(_indices);
      }

      /**
       * Returns the number of combinations not yet generated.
       * 
       * @return the number of combinations not yet generated
       */
      public BigInteger getNumLeft()
      {
         return _numLeft;
      }

      @Override
      public boolean hasNext()
      {
         return _numLeft.compareTo(ZERO) == 1;
      }

      @Override
      public ArrayList<T> next()
      {
         if (!_numLeft.equals(_numTotal))
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

   private ArrayList<T> getResult(final int[] indexes)
   {
      final ArrayList<T> result = new ArrayList<T>(indexes.length);
      for (final int indexe : indexes)
      {
         result.add(_elements.get(indexe));
      }
      return result;
   }
}
