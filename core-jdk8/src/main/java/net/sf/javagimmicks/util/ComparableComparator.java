package net.sf.javagimmicks.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A {@link Comparator} implementation the simply compares {@link Comparable}
 * objects - useful in cases where an API has the explicit need for a
 * {@link Comparator} implementation.
 * 
 * @param <T>
 *           the type of {@link Comparable}s to compare
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T>, Serializable
{
   private static final long serialVersionUID = 1790088535783496697L;

   static
   {
      INSTANCE = new ComparableComparator();
   }

   /**
    * @deprecated Use static method {@link #getInstance()} instead.
    */
   @Deprecated
   public ComparableComparator()
   {

   }

   /**
    * The static singleton instance of {@link ComparableComparator}.
    */
   public static ComparableComparator<? extends Comparable<?>> INSTANCE;

   /**
    * Returns the singleton {@link Comparable} instance.
    * 
    * @return the singleton {@link Comparable} instance
    */
   public static final <T extends Comparable<? super T>> ComparableComparator<T> getInstance()
   {
      return (ComparableComparator<T>) INSTANCE;
   }

   @Override
   public int compare(final T o1, final T o2)
   {
      return o1.compareTo(o2);
   }
}