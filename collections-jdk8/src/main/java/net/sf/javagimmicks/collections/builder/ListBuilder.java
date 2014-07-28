package net.sf.javagimmicks.collections.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A builder class for building {@link List}s using a fluent API with one
 * chained call.
 * 
 * @param <E>
 *           the type of elements the resulting {@link List} can carry
 * @param <T>
 *           the type of the produced {@link List}
 */
public class ListBuilder<E, T extends List<E>> extends CollectionBuilder<E, T> implements Supplier<T>
{
   /**
    * Wraps a new {@link ListBuilder} around the given {@link List}
    * 
    * @param internalList
    *           the {@link List} to wrap a new {@link ListBuilder} around
    * @return the resulting {@link ListBuilder}
    */
   public static <E, T extends List<E>> ListBuilder<E, T> create(final T internalList)
   {
      return new ListBuilder<E, T>(internalList);
   }

   /**
    * Creates a new {@link ListBuilder} for building a new {@link ArrayList}
    * 
    * @return the resulting {@link ListBuilder}
    */
   public static <E> ListBuilder<E, ArrayList<E>> createArrayList()
   {
      return create(new ArrayList<E>());
   }

   /**
    * Creates a new {@link ListBuilder} for building a new {@link ArrayList}
    * with the given initial capacity
    * 
    * @param initialCapcity
    *           the initial capacity to use for the wrapped {@link ArrayList}
    * @return the resulting {@link ListBuilder}
    */
   public static <E> ListBuilder<E, ArrayList<E>> createArrayList(final int initialCapcity)
   {
      return create(new ArrayList<E>(initialCapcity));
   }

   /**
    * Creates a new {@link ListBuilder} for building a new {@link LinkedList}
    * 
    * @return the resulting {@link ListBuilder}
    */
   public static <E> ListBuilder<E, LinkedList<E>> createLinkedList()
   {
      return create(new LinkedList<E>());
   }

   /**
    * Creates a new {@link ListBuilder} around a given internal {@link List}
    * 
    * @param internalList
    *           the {@link List} to wrap around
    */
   public ListBuilder(final T internalList)
   {
      super(internalList);
   }

   /**
    * Calls {@link List#add(int, Object)} on the underlying {@link List} and
    * returns itself
    * 
    * @param index
    *           the index where to add the new element in the {@link List}
    * @param e
    *           the element to add
    * @return the {@link ListBuilder} itself
    * @see List#add(int, Object)
    */
   public ListBuilder<E, T> add(final int index, final E e)
   {
      _internalCollection.add(index, e);
      return this;
   }

   /**
    * Calls {@link List#addAll(int, Collection)} on the underlying {@link List}
    * and returns itself
    * 
    * @param index
    *           the index where to add the new elements in the {@link List}
    * @param c
    *           the {@link Collection} whose elements should be added
    * @return the {@link ListBuilder} itself
    * @see List#addAll(int, Collection)
    */
   public ListBuilder<E, T> addAll(final int index, final Collection<? extends E> c)
   {
      _internalCollection.addAll(index, c);
      return this;
   }

   /**
    * Adds any number of elements given as a variable argument list to the
    * underlying {@link List} and returns itself
    * 
    * @param index
    *           the index where to add the new elements in the {@link List}
    * @param elements
    *           the list of elements to add
    * @return the {@link ListBuilder} itself
    * @see #addAll(int, Collection)
    */
   @SuppressWarnings("unchecked")
   public ListBuilder<E, T> addAll(final int index, final E... elements)
   {
      return addAll(Arrays.asList(elements));
   }

   /**
    * Calls {@link List#remove(int)} on the underlying {@link List} and returns
    * itself
    * 
    * @param index
    *           the index where to remove an element
    * @return the {@link ListBuilder} itself
    * @see List#remove(int)
    */
   public ListBuilder<E, T> remove(final int index)
   {
      _internalCollection.remove(index);
      return this;
   }

   /**
    * Calls {@link List#set(int, Object)} on the underlying {@link List} and
    * returns itself
    * 
    * @param index
    *           the index where to set the new element
    * @param e
    *           the element to set in the underlying {@link List}
    * @return the {@link ListBuilder} itself
    * @see List#set(int, Object)
    */
   public ListBuilder<E, T> set(final int index, final E e)
   {
      _internalCollection.set(index, e);
      return this;
   }

   /**
    * Calls {@link List#add(Object)} on the underlying {@link List} and returns
    * itself
    * 
    * @param e
    *           the element to add
    * @return the {@link ListBuilder} itself
    * @see List#add(Object)
    */
   @Override
   public ListBuilder<E, T> add(final E e)
   {
      return (ListBuilder<E, T>) super.add(e);
   }

   /**
    * Calls {@link List#addAll(Collection)} on the underlying {@link List} and
    * returns itself
    * 
    * @param c
    *           the {@link Collection} whose elements should be added
    * @return the {@link ListBuilder} itself
    * @see List#addAll(Collection)
    */
   @Override
   public ListBuilder<E, T> addAll(final Collection<? extends E> c)
   {
      return (ListBuilder<E, T>) super.addAll(c);
   }

   /**
    * Adds any number of elements given as a variable argument list to the
    * underlying {@link List} and returns itself
    * 
    * @param elements
    *           the list of elements to add
    * @return the {@link ListBuilder} itself
    * @see #addAll(Collection)
    */
   @Override
   @SuppressWarnings("unchecked")
   public ListBuilder<E, T> addAll(final E... elements)
   {
      return (ListBuilder<E, T>) super.addAll(elements);
   }

   /**
    * Calls {@link List#clear()} on the underlying {@link List} and returns
    * itself
    * 
    * @return the {@link ListBuilder} itself
    * @see List#clear()
    */
   @Override
   public ListBuilder<E, T> clear()
   {
      return (ListBuilder<E, T>) super.clear();
   }

   /**
    * Calls {@link List#remove(Object)} on the underlying {@link List} and
    * returns itself
    * 
    * @param o
    *           the element to remove from the underlying {@link List}
    * @return the {@link ListBuilder} itself
    * @see List#remove(Object)
    */
   @Override
   public ListBuilder<E, T> remove(final Object o)
   {
      return (ListBuilder<E, T>) super.remove(o);
   }

   /**
    * Calls {@link List#removeAll(Collection)} on the underlying {@link List}
    * and returns itself
    * 
    * @param c
    *           the {@link Collection} of elements to remove from the underlying
    *           {@link List}
    * @return the {@link ListBuilder} itself
    * @see List#removeAll(Collection)
    */
   @Override
   public ListBuilder<E, T> removeAll(final Collection<?> c)
   {
      return (ListBuilder<E, T>) super.removeAll(c);
   }

   /**
    * Removes any number of elements given as a variable argument list from the
    * underlying {@link List} and returns itself
    * 
    * @param elements
    *           the list of elements to remove
    * @return the {@link ListBuilder} itself
    * @see #removeAll(Collection)
    */
   @Override
   @SuppressWarnings("unchecked")
   public ListBuilder<E, T> removeAll(final E... elements)
   {
      return (ListBuilder<E, T>) super.removeAll(elements);
   }

   /**
    * Calls {@link List#retainAll(Collection)} on the underlying {@link List}
    * and returns itself
    * 
    * @param c
    *           the {@link Collection} of elements to retain in the underlying
    *           {@link List}
    * @return the {@link ListBuilder} itself
    * @see List#retainAll(Collection)
    */
   @Override
   public ListBuilder<E, T> retainAll(final Collection<?> c)
   {
      return (ListBuilder<E, T>) super.retainAll(c);
   }

   /**
    * Retains any number of elements given as a variable argument list in the
    * underlying {@link List} and returns itself
    * 
    * @param elements
    *           the list of elements to retain
    * @return the {@link ListBuilder} itself
    * @see #retainAll(Collection)
    */
   @Override
   @SuppressWarnings("unchecked")
   public ListBuilder<E, T> retainAll(final E... elements)
   {
      return (ListBuilder<E, T>) super.retainAll(elements);
   }
}
