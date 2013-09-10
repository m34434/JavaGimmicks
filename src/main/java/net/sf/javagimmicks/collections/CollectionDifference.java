package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class - if provided with two {@link Collection}s - will find out the
 * differences between them.
 * <p>
 * In concrete, it will find out and provide which elements are common to both
 * provided {@link Collection}s, which ones are only contained in the first and
 * which ones are only contained in the second one.
 * 
 * @param <E>
 *           the type of elements the to be compared {@link Collection}s must
 *           have
 */
public class CollectionDifference<E>
{
   protected final List<E> _onlyA = new ArrayList<E>();
   protected final List<E> _onlyB = new ArrayList<E>();
   protected final List<E> _both = new ArrayList<E>();

   /**
    * Create a new {@link CollectionDifference} instance for the two given
    * {@link Collection}s.
    * 
    * @param a
    *           the first {@link Collection} to find differences
    * @param b
    *           the second {@link Collection} to find differences
    * @return the {@link CollectionDifference} containing the differences
    *         between the two given {@link Collection}s
    */
   public static <E> CollectionDifference<E> create(final Collection<? extends E> a, final Collection<? extends E> b)
   {
      return new CollectionDifference<E>(a, b);
   }

   /**
    * @deprecated Use {@link #create(Collection, Collection)} instead
    */
   @Deprecated
   public CollectionDifference(final Collection<? extends E> a, final Collection<? extends E> b)
   {
      for (final E element : a)
      {
         if (b.contains(element))
         {
            _both.add(element);
         }
         else
         {
            _onlyA.add(element);
         }
      }

      for (final E element : b)
      {
         if (!_both.contains(element))
         {
            _onlyB.add(element);
         }
      }
   }

   public List<E> getOnlyA()
   {
      return _onlyA;
   }

   public List<E> getOnlyB()
   {
      return _onlyB;
   }

   public List<E> getBoth()
   {
      return _both;
   }
}
