package net.sf.javagimmicks.collections.diff;

import java.util.List;

import net.sf.javagimmicks.collections.diff.Difference.Range;

/**
 * Represents all single {@link Difference}s between two {@link List} - serves
 * as comparison result with the difference API.
 * <p>
 * <b>Note:</b> a {@link DifferenceList} is defined to be read-only, so
 * implementors should ensure this.
 * 
 * @param <T>
 *           the type of elements the compared {@link List}s carry
 */
public interface DifferenceList<T> extends List<Difference<T>>
{
   /**
    * Returns an inverted version of this {@link DifferenceList} which means
    * that the <i>delete</i> and <i>add</i> {@link Range}s within the contained
    * {@link Difference}s are exchanged.
    * 
    * @return an inverted version of this {@link DifferenceList}
    */
   default DifferenceList<T> invert()
   {
      return DifferenceUtils.getInvertedDifferenceList(this);
   }

   /**
    * Applies the {@link Difference}s contained in this {@link DifferenceList}
    * to a given "source" {@link List}.
    * <p>
    * This means - more formally - that if the source {@link List} is equal to
    * the "left side" of the original comparison it will after appliance be
    * equals to the "right side" of the original comparison.
    * 
    * @param list
    *           the {@link List} to apply the {@link Difference}s to
    */
   default void applyTo(List<T> list)
   {
      DifferenceUtils.applyDifferenceList(this, list);
   }
}
