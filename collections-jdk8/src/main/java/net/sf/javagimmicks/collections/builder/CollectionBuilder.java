package net.sf.javagimmicks.collections.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * A builder class for building {@link Collection}s and {@link Set}s using a
 * fluent API with one chained call.
 * 
 * @param <E>
 *           the type of elements the resulting {@link Collection} can carry
 * @param <T>
 *           the type of the produced {@link Collection}
 */
public class CollectionBuilder<E, T extends Collection<E>> implements Supplier<T>
{
   protected final T _internalCollection;

   /**
    * Wraps a new {@link CollectionBuilder} around the given {@link Collection}
    * 
    * @param internalCollection
    *           the {@link Collection} to wrap a new {@link CollectionBuilder}
    *           around
    * @return the resulting {@link CollectionBuilder}
    */
   public static <E, T extends Collection<E>> CollectionBuilder<E, T> create(final T internalCollection)
   {
      return new CollectionBuilder<E, T>(internalCollection);
   }

   /**
    * Creates a new {@link CollectionBuilder} for building a new {@link HashSet}
    * 
    * @return the resulting {@link CollectionBuilder}
    */
   public static <E> CollectionBuilder<E, HashSet<E>> createHashSet()
   {
      return create(new HashSet<E>());
   }

   /**
    * Creates a new {@link CollectionBuilder} for building a new {@link TreeSet}
    * 
    * @return the resulting {@link CollectionBuilder}
    */
   public static <E> CollectionBuilder<E, TreeSet<E>> createTreeSet()
   {
      return create(new TreeSet<E>());
   }

   /**
    * Creates a new {@link CollectionBuilder} for building a new {@link TreeSet}
    * based on a given {@link Comparator}
    * 
    * @param comparator
    *           the {@link Comparator} to use in the underlying {@link TreeSet}
    * @return the resulting {@link CollectionBuilder}
    */
   public static <E> CollectionBuilder<E, TreeSet<E>> createTreeSet(final Comparator<? super E> comparator)
   {
      return create(new TreeSet<E>(comparator));
   }

   /**
    * Creates a new {@link CollectionBuilder} around a given internal
    * {@link Collection}
    * 
    * @param internalCollection
    *           the {@link Collection} to wrap around
    */
   public CollectionBuilder(final T internalCollection)
   {
      _internalCollection = internalCollection;
   }

   /**
    * Calls {@link Collection#add(Object)} on the underlying {@link Collection}
    * and returns itself
    * 
    * @param e
    *           the element to add
    * @return the {@link CollectionBuilder} itself
    * @see Collection#add(Object)
    */
   public CollectionBuilder<E, T> add(final E e)
   {
      _internalCollection.add(e);
      return this;
   }

   /**
    * Calls {@link Collection#addAll(Collection)} on the underlying
    * {@link Collection} and returns itself
    * 
    * @param c
    *           the {@link Collection} whose elements should be added
    * @return the {@link CollectionBuilder} itself
    * @see Collection#addAll(Collection)
    */
   public CollectionBuilder<E, T> addAll(final Collection<? extends E> c)
   {
      _internalCollection.addAll(c);
      return this;
   }

   /**
    * Adds any number of elements given as a variable argument list to the
    * underlying {@link Collection} and returns itself
    * 
    * @param elements
    *           the list of elements to add
    * @return the {@link CollectionBuilder} itself
    * @see #addAll(Collection)
    */
   @SuppressWarnings("unchecked")
   public CollectionBuilder<E, T> addAll(final E... elements)
   {
      return addAll(Arrays.asList(elements));
   }

   /**
    * Calls {@link Collection#remove(Object)} on the underlying
    * {@link Collection} and returns itself
    * 
    * @param o
    *           the element to remove from the underlying {@link Collection}
    * @return the {@link CollectionBuilder} itself
    * @see Collection#remove(Object)
    */
   public CollectionBuilder<E, T> remove(final Object o)
   {
      _internalCollection.remove(o);
      return this;
   }

   /**
    * Calls {@link Collection#removeAll(Collection)} on the underlying
    * {@link Collection} and returns itself
    * 
    * @param c
    *           the {@link Collection} of elements to remove from the underlying
    *           {@link Collection}
    * @return the {@link CollectionBuilder} itself
    * @see Collection#removeAll(Collection)
    */
   public CollectionBuilder<E, T> removeAll(final Collection<?> c)
   {
      _internalCollection.removeAll(c);
      return this;
   }

   /**
    * Removes any number of elements given as a variable argument list from the
    * underlying {@link Collection} and returns itself
    * 
    * @param elements
    *           the list of elements to remove
    * @return the {@link CollectionBuilder} itself
    * @see #removeAll(Collection)
    */
   @SuppressWarnings("unchecked")
   public CollectionBuilder<E, T> removeAll(final E... elements)
   {
      return removeAll(Arrays.asList(elements));
   }

   /**
    * Calls {@link Collection#retainAll(Collection)} on the underlying
    * {@link Collection} and returns itself
    * 
    * @param c
    *           the {@link Collection} of elements to retain in the underlying
    *           {@link Collection}
    * @return the {@link CollectionBuilder} itself
    * @see Collection#retainAll(Collection)
    */
   public CollectionBuilder<E, T> retainAll(final Collection<?> c)
   {
      _internalCollection.retainAll(c);
      return this;
   }

   /**
    * Retains any number of elements given as a variable argument list in the
    * underlying {@link Collection} and returns itself
    * 
    * @param elements
    *           the list of elements to retain
    * @return the {@link CollectionBuilder} itself
    * @see #retainAll(Collection)
    */
   @SuppressWarnings("unchecked")
   public CollectionBuilder<E, T> retainAll(final E... elements)
   {
      return retainAll(Arrays.asList(elements));
   }

   /**
    * Calls {@link Collection#clear()} on the underlying {@link Collection} and
    * returns itself
    * 
    * @return the {@link CollectionBuilder} itself
    * @see Collection#clear()
    */
   public CollectionBuilder<E, T> clear()
   {
      _internalCollection.clear();
      return this;
   }

   /**
    * Returns the underlying {@link Collection}
    * 
    * @return the underlying {@link Collection}
    */
   public T get()
   {
      return _internalCollection;
   }

   @Override
   public String toString()
   {
      return _internalCollection.toString();
   }
}
