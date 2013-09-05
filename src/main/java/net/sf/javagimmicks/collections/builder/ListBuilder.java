package net.sf.javagimmicks.collections.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ListBuilder<E, T extends List<E>> extends CollectionBuilder<E, T>
{
   public static <E, T extends List<E>> ListBuilder<E, T> create(T internalList)
   {
      return new ListBuilder<E, T>(internalList);
   }
   
   public static <E> ListBuilder<E, ArrayList<E>> createArrayList()
   {
      return create(new ArrayList<E>());
   }
   
   public static <E> ListBuilder<E, ArrayList<E>> createArrayList(int initialCapcity)
   {
      return create(new ArrayList<E>(initialCapcity));
   }
   
   public static <E> ListBuilder<E, LinkedList<E>> createLinkedList()
   {
      return create(new LinkedList<E>());
   }
   
   public ListBuilder(T internalList)
   {
      super(internalList);
   }
   
   public ListBuilder<E, T> add(int index, E e)
   {
      _internalCollection.add(index, e);
      return this;
   }
   
   public ListBuilder<E, T> addAll(int index, Collection<? extends E> c)
   {
      _internalCollection.addAll(index, c);
      return this;
   }
   
   public ListBuilder<E, T> addAll(int index, E... elements)
   {
      return addAll(Arrays.asList(elements));
   }
   
   public ListBuilder<E, T> remove(int index)
   {
      _internalCollection.remove(index);
      return this;
   }
   
   public ListBuilder<E, T> set(int index, E e)
   {
      _internalCollection.set(index, e);
      return this;
   }
   
   @Override
   public ListBuilder<E, T> add(E e)
   {
      return (ListBuilder<E, T>) super.add(e);
   }

   @Override
   public ListBuilder<E, T> addAll(Collection<? extends E> c)
   {
      return (ListBuilder<E, T>) super.addAll(c);
   }
   
   @Override
   public ListBuilder<E, T> addAll(E... elements)
   {
      return (ListBuilder<E, T>) super.addAll(elements);
   }

   @Override
   public ListBuilder<E, T> clear()
   {
      return (ListBuilder<E, T>) super.clear();
   }

   @Override
   public ListBuilder<E, T> remove(Object o)
   {
      return (ListBuilder<E, T>) super.remove(o);
   }

   @Override
   public ListBuilder<E, T> removeAll(Collection<?> c)
   {
      return (ListBuilder<E, T>) super.removeAll(c);
   }
   
   @Override
   public ListBuilder<E, T> removeAll(E... elements)
   {
      return (ListBuilder<E, T>) super.removeAll(elements);
   }

   @Override
   public ListBuilder<E, T> retainAll(Collection<?> c)
   {
      return (ListBuilder<E, T>) super.retainAll(c);
   }
   
   @Override
   public ListBuilder<E, T> retainAll(E... elements)
   {
      return (ListBuilder<E, T>) super.retainAll(elements);
   }

   public T toList()
   {
      return toCollection();
   }
}
