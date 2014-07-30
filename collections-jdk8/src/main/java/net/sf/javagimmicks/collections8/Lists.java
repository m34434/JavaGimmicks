package net.sf.javagimmicks.collections8;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Lists
{
   private Lists()
   {}

   /**
    * Performs a {@link Collection#forEach(java.util.function.Consumer)}
    * operation on a given {@link List} providing the current index as an
    * additional parameter - uses a {@link BiConsumer} instead of a
    * {@link Consumer} to be able to provide the index.
    * 
    * @param list
    *           the {@link List} to iterate over
    * @param action
    *           the {@link BiConsumer} that takes the current index and element
    * @param <E>
    *           the type of elements that the {@link List} carries
    */
   public static <E> void forEachWithIndex(final List<E> list, final BiConsumer<Integer, ? super E> action)
   {
      for (final ListIterator<E> listIterator = list.listIterator(); listIterator.hasNext();)
      {
         action.accept(listIterator.nextIndex(), listIterator.next());
      }
   }

   /**
    * Performs a filtered
    * {@link Collection#forEach(java.util.function.Consumer)} operation on a
    * given {@link List} providing the current index as an additional parameter
    * - uses a {@link BiConsumer} instead of a {@link Consumer} to be able to
    * provide the index.
    * 
    * @param list
    *           the {@link List} to iterate over
    * @param filter
    *           a {@link Predicate} to filter entries on which the
    *           {@link BiConsumer} should be called
    * @param action
    *           the {@link BiConsumer} that takes the current index and element
    * @param <E>
    *           the type of elements that the {@link List} carries
    */
   public static <E> void forEachWithIndex(final List<E> list, final Predicate<E> filter,
         final BiConsumer<Integer, ? super E> action)
   {
      for (final ListIterator<E> listIterator = list.listIterator(); listIterator.hasNext();)
      {
         final int index = listIterator.nextIndex();
         final E element = listIterator.next();

         if (filter.test(element))
         {
            action.accept(index, element);
         }
      }
   }
}
