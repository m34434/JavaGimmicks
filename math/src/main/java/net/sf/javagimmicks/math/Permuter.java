package net.sf.javagimmicks.math;

import static net.sf.javagimmicks.math.MathExt.factorial;

import java.math.BigInteger;
import java.util.Collection;

/**
 * A class that permutes a given array of elements. It is an iterator that
 * returns all permutations, successively.
 * <p>
 * <b>Example:</b> a call to <code>new Permuter("a", "b", "c")</code> would
 * return the following combinations:
 * <ul>
 * <li><code>[a, b, c]</code></li>
 * <li><code>[a, c, b]</code></li>
 * <li><code>[b, a, c]</code></li>
 * <li><code>[b, c, a]</code></li>
 * <li><code>[c, a, b]</code></li>
 * <li><code>[c, b, a]</code></li>
 * </ul>
 * 
 * @param <T>
 *           The type of the elements to be permuted.
 */
public class Permuter<T> extends CombinatoricOperator<T>
{

   /**
    * Initialize a new instance, with the given elements to permute.
    * 
    * @param elements
    *           The elements to permute.
    */
   public Permuter(final T... elements)
   {
      super(elements, elements.length);
   }

   /**
    * Initialize a new instance, with the given elements to permute.
    * 
    * @param elements
    *           The elements to permute.
    */
   public Permuter(final Collection<T> elements)
   {
      super(elements, elements.size());
   }

   @Override
   protected BigInteger calculateTotal(final int n, final int r)
   {
      return factorial(n);
   }

   @Override
   protected void computeNext(final int[] indices)
   {
      // find the rightmost element that is smaller than the element at its
      // right
      int i = indices.length - 1;
      while (indices[i - 1] >= indices[i])
         i = i - 1;
      // find the rightmost element that is bigger then the other one
      int j = indices.length;
      while (indices[j - 1] <= indices[i - 1])
         j = j - 1;
      // swap them (always is i < j)
      swap(indices, i - 1, j - 1);
      // now the elements at the right of i
      // are in descending order, so reverse them all
      i++;
      j = indices.length;
      while (i < j)
      {
         swap(indices, i - 1, j - 1);
         i++;
         j--;
      }
      // TODO: try other algorithms,
      // see
      // http://www.cut-the-knot.org/Curriculum/Combinatorics/JohnsonTrotter.shtml
   }

   /**
    * Swap the elements at positions a and b, both from the index array and from
    * the element array.
    * 
    * @param a
    *           , b The indices of the elements to be swapped.
    */
   private void swap(final int[] indices, final int a, final int b)
   {
      final int temp = indices[a];
      indices[a] = indices[b];
      indices[b] = temp;
   }

}
