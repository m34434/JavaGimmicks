package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.Iterator;

/**
 * Contains some helper methods for dealing with {@link Collection}s
 */
public class CollectionUtils
{
   /**
    * Joins the {@link String} representations of the elements of a given
    * {@link Collection} using a given separator.
    * 
    * @param c
    *           the {@link Collection} whose elements should be joined
    * @param separator
    *           the separator to use for joining
    * @return the join result as a {@link StringBuilder}
    */
   public static StringBuilder concatElements(final Collection<?> c, final String separator)
   {
      final StringBuilder builder = new StringBuilder();

      if (c.isEmpty())
      {
         return builder;
      }

      final Iterator<?> oIterator = c.iterator();
      builder.append(oIterator.next());

      while (oIterator.hasNext())
      {
         builder.append(separator).append(oIterator.next());
      }

      return builder;
   }

   /**
    * Adds all elements that a given {@link Iterator} provides to a given
    * {@link Collection} of appropriate type
    * 
    * @param targetCollection
    *           the {@link Collection} where to add the elements
    * @param sourceIterator
    *           the {@link Iterator} where to get the elements from
    * @param <T>
    *           the type of elements to transfer
    */
   public static <T> void addAll(final Collection<? super T> targetCollection,
         final Iterator<? extends T> sourceIterator)
   {
      while (sourceIterator.hasNext())
      {
         targetCollection.add(sourceIterator.next());
      }
   }
}
