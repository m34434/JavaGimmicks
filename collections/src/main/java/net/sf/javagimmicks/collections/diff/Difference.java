package net.sf.javagimmicks.collections.diff;

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
    * The pseudo-index ({@value #NONE}) if there is no delete or add range.
    * 
    * @see #getDeleteStartIndex()
    * @see #getDeleteEndIndex()
    * @see #getAddStartIndex()
    * @see #getAddEndIndex()
    */
   int NONE = -1;

   /**
    * Return the <i>delete {@link Range}</i> of this {@link Difference}. This
    * will never return {@code null} event if there is no such {@link Range} -
    * instead {@link Range#exists()} will result to {@code false}.
    * 
    * @return the delete {@link Range}
    */
   Range<T> deleteRange();

   /**
    * Return the <i>add {@link Range}</i> of this {@link Difference}. This will
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
    * @return
    */
   Difference<T> invert();

   interface Range<T> extends List<T>
   {
      /**
       * Returns the index of the first element of the range or {@link #NONE} if
       * there is none ({@link #exists()} resolves to {@code false}).
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
       * Return if the range exists in the surrounding {@link Difference}
       * 
       * @return if the range exists in the surrounding {@link Difference}
       */
      boolean exists();
   }
}