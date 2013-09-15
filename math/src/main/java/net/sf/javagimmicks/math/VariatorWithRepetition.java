package net.sf.javagimmicks.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

/**
 * A class that sequentially returns all variations with repetition of a certain
 * number out of an array of given elements.
 * 
 * @author <a href="mailto:hendrik.maryns@uni-tuebingen.de">Hendrik Maryns</a>
 * @param <T>
 *           The type of the elements of which variations are to be returned.
 */
public class VariatorWithRepetition<T> extends CombinatoricOperator<T>
{

   /**
    * Initialise a new variator, with given elements and size of the arrays to
    * be returned.
    * 
    * @param elements
    *           The elements of which variations have to be computed.
    * @param r
    *           The size of the variations to compute.
    */
   public VariatorWithRepetition(final T[] elements, final int r)
   {
      super(elements, r);
   }

   /**
    * Initialise a new variator, with given elements and size of the arrays to
    * be returned.
    * 
    * @param elements
    *           The elements of which variations have to be computed.
    * @param r
    *           The size of the variations to compute.
    */
   public VariatorWithRepetition(final Collection<T> elements, final int r)
   {
      super(elements, r);
   }

   /**
    * Initialise the array of indices. For variations with repetition, it needs
    * to be initialised with all 0s
    */
   @Override
   protected void initialiseIndices()
   {
      Arrays.fill(indices, 0);
   }

   @Override
   protected BigInteger calculateTotal(final int n, final int r)
   {
      return BigInteger.valueOf(n).pow(r);
   }

   @Override
   protected void computeNext()
   {
      int i = indices.length - 1;
      final int n = elements.size();
      while (++indices[i] == n && i > 0)
      {
         indices[i--] = 0;
      }
   }

}