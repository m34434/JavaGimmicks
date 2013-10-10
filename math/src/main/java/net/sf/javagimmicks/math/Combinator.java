package net.sf.javagimmicks.math;

import static net.sf.javagimmicks.math.MathExt.factorial;

import java.math.BigInteger;
import java.util.Collection;

/**
 * A class that sequentially returns all combinations of a given length out of
 * an array of given elements.
 * <p>
 * Mind that combinations don't have an order, so the order of elements in the
 * result is not necessarily deterministic!
 * <p>
 * <b>Example:</b> a call to <code>new Combinator(2, "a", "b", "c")</code> would
 * return the following combinations:
 * <ul>
 * <li><code>[a, b]</code></li>
 * <li><code>[a, c]</code></li>
 * <li><code>[b, c]</code></li>
 * </ul>
 * 
 * @param <T>
 *           The type of the elements of which combinations are to be returned.
 */
public class Combinator<T> extends CombinatoricOperator<T>
{

   /**
    * Initialize a new instance, with given elements and size of the arrays to
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
    * Initialize a new instance, with given elements and size of the arrays to
    * be returned.
    * 
    * @param elements
    *           The elements of which combinations have to be computed.
    * @param r
    *           The size of the combinations to compute.
    */
   public Combinator(final int r, final T... elements)
   {
      super(elements, r);
      assert r <= elements.length;
   }

   /**
    * Initialize a new instance, with given elements and size of the arrays to
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
   protected void computeNext(final int[] indices)
   {
      final int r = indices.length;
      int i = r - 1;
      final int n = _elements.size();
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
