package net.sf.javagimmicks.collections8.diff;

import java.util.List;

/**
 * Represents one single difference (unit) between two {@link List}s.
 * <p>
 * Note that the comparison of two {@link List}s will usually result in
 * <b>multiple</b> differences, which this API encapsulates with a
 * {@link DifferenceList}.
 * <p>
 * One single difference always consists of a <i>delete range</i> of elements
 * <b>and/or</b> an <i>add range</i> of elements. These ranges are represented
 * as {@link Range} instances.
 * <p>
 * <b>Examples</b>
 * <ul>
 * <li>When comparing [1, 2, 3, 4] to [1, X, Y, Z, 4] there is a <i>delete
 * {@link Range}</i> [2, 3] and an <i>add {@link Range}</i> [X, Y, Z]</li>
 * <li>When comparing [1, 2, 3, 4] to [1, 4] there is a <i>delete {@link Range}
 * </i> [2, 3] but <b>no</b> <i>add {@link Range}</i></li>
 * <li>When comparing [1, 4] to [1, 2, 3, 4] there is <b>no</b> <i>delete
 * {@link Range}</i> but an <i>add {@link Range}</i> [2, 3]</li>
 * </ul>
 * 
 * @param <T>
 *           the type of elements the two compared {@link List}s have
 * @see DifferenceList
 */
public interface Difference<T>
{
   /**
    * The pseudo end index ({@value #NONE}) if there is no delete or add range.
    */
   int NONE = -1;

   /**
    * Returns the <i>delete {@link Range}</i> of this {@link Difference}. This
    * will never return {@code null} event if there is no such {@link Range} -
    * instead {@link Range#exists()} will result to {@code false}.
    * 
    * @return the delete {@link Range}
    */
   Range<T> deleteRange();

   /**
    * Returns the <i>add {@link Range}</i> of this {@link Difference}. This will
    * never return {@code null} event if there is no such {@link Range} -
    * instead {@link Range#exists()} will result to {@code false}.
    * 
    * @return the add {@link Range}
    */
   Range<T> addRange();

   /**
    * Produces an inverted version of this {@link Difference} which means that
    * <i>delete range</i> and <i>add range</i> are exchanged.
    * 
    * @return an inverted version of this {@link Difference}
    */
   default Difference<T> invert()
   {
      return DifferenceUtils.getInvertedDifference(this);
   }

   /**
    * Applies the difference information contained in a given {@link Difference}
    * object to a target {@link List}. The means: remove there all elements
    * denoted by the "delete" information of the {@link Difference} object at
    * the right position and add all elements from the "add" {@link List} at the
    * same position.
    * 
    * @param list
    *           the {@link List} where to apply the changes
    */
   default void applyTo(final List<T> list)
   {
      DifferenceUtils.applyDifference(this, list);
   }

   /**
    * Encapsulates a range (of type <i>delete</i> or <i>add</i>) that a
    * {@link Difference} will contain.
    * <p>
    * <b>Note:</b> a {@link Range} is defined to be read-only, so implementors
    * should ensure this.
    * 
    * @param <T>
    *           the type of elements the surrounding {@link Difference} operates
    *           on.
    */
   interface Range<T> extends List<T>
   {
      /**
       * Returns the index of the first element of the range or 0 if there is
       * none ({@link #exists()} resolves to {@code false}).
       * 
       * @return the first index of the range
       * @see #exists()
       */
      int getStartIndex();

      /**
       * Returns the index of the last element of the range or {@link #NONE} if
       * there is none ({@link #exists()} resolves to {@code false}).
       * 
       * @return the last index of the range
       * @see #exists()
       */
      int getEndIndex();

      /**
       * Returns if the range exists in the surrounding {@link Difference}
       * 
       * @return if the range exists in the surrounding {@link Difference}
       */
      boolean exists();
   }
}