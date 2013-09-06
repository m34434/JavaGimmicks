package net.sf.javagimmicks.math;

import java.math.BigInteger;
import java.util.Collection;

/**
 * A class that sequentially returns all combinations of a certain number out of
 * an array of given elements. Thanks to Michael Gillegand for the base
 * implementation: <a href="http://www.merriampark.com/comb.htm"/>
 * 
 * @author Hendrik Maryns
 * @param <T>
 *           The type of the elements of which combinations are to be returned.
 */
public class Combinator<T> extends CombinatoricOperator<T>
{

   /**
    * Initialise a new Combinator, with given elements and size of the arrays to
    * be returned.
    * 
    * @param elements
    *           The elements of which combinations have to be computed.
    * @param r
    *           The size of the combinations to compute.
    */
   public Combinator(final T[] elements, final int r)
   {
      super(elements, r);
      assert r <= elements.length;
   }

   /**
    * Initialise a new Combinator, with given elements and size of the arrays to
    * be returned.
    * 
    * @param elements
    *           The elements of which combinations have to be computed.
    * @param r
    *           The size of the combinations to compute.
    */
   public Combinator(final Collection<T> elements, final int r)
   {
      super(elements, r);

      if (r > elements.size())
      {
         throw new IllegalArgumentException(
               "Size of lists to create mustn't be greater than number of provided elements!");
      }
   }

   @Override
   protected BigInteger calculateTotal(final int n, final int r)
   {
      /*
       * The factorial of the number of elements divided by the factorials of
       * the size of the combinations and the number of elements minus the size
       * of the combinations. That is, with the number of elements = n and the
       * size of the combinations = r: n n! ( ) = --------- r (n-r)!r!
       */
      final BigInteger nFact = factorial(n);
      final BigInteger rFact = factorial(r);
      final BigInteger nminusrFact = factorial(n - r);
      return nFact.divide(rFact.multiply(nminusrFact));
   }

   @Override
   protected void computeNext()
   {
      final int r = indices.length;
      int i = r - 1;
      final int n = elements.size();
      while (indices[i] == n - r + i)
      {
         i--;
      }
      indices[i] = indices[i] + 1;
      for (int j = i + 1; j < r; j++)
      {
         indices[j] = indices[i] + j - i;
      }
      // TODO: understand this.
   }

}
