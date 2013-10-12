package net.sf.javagimmicks.math.combinatorics;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

/**
 * A class that sequentially returns all variations with repetition of a given
 * length out of an array of given elements.
 * <p>
 * <b>Example:</b> a call to
 * <code>new VariatorWithRepetition(2, "a", "b", "c")</code> would return the
 * following combinations:
 * <ul>
 * <li><code>[a, a]</code></li>
 * <li><code>[a, b]</code></li>
 * <li><code>[a, c]</code></li>
 * <li><code>[b, a]</code></li>
 * <li><code>[b, b]</code></li>
 * <li><code>[b, c]</code></li>
 * <li><code>[c, a]</code></li>
 * <li><code>[c, b]</code></li>
 * <li><code>[c, c]</code></li>
 * </ul>
 * 
 * @param <T>
 *           The type of the elements of which variations are to be returned.
 */
public class VariatorWithRepetition<T> extends CombinatoricIterable<T>
{

   /**
    * Initialize a instance with given elements and size of the arrays to be
    * returned.
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
    * Initialize a instance with given elements and size of the arrays to be
    * returned.
    * 
    * @param elements
    *           The elements of which variations have to be computed.
    * @param r
    *           The size of the variations to compute.
    */
   public VariatorWithRepetition(final int r, final T... elements)
   {
      super(elements, r);
   }

   /**
    * Initialize a new instance with given elements and size of the arrays to be
    * returned.
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
   protected void initialiseIndices(final int[] indices)
   {
      Arrays.fill(indices, 0);
   }

   @Override
   protected BigInteger calculateTotal(final int n, final int r)
   {
      return BigInteger.valueOf(n).pow(r);
   }

   @Override
   protected void computeNext(final int[] indices)
   {
      int i = indices.length - 1;
      final int n = _elements.size();
      while (++indices[i] == n && i > 0)
      {
         indices[i--] = 0;
      }
   }

}
