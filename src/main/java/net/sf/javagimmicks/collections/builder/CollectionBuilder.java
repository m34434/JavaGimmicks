package net.sf.javagimmicks.collections.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionBuilder<E, T extends Collection<E>>
{
   protected final T _internalCollection;

   public static <E, T extends Collection<E>> CollectionBuilder<E, T> create(T internalCollection)
   {
      return new CollectionBuilder<E, T>(internalCollection);
   }
   
   public static <E> CollectionBuilder<E, HashSet<E>> createHashSet()
   {
      return create(new HashSet<E>());
   }
   
   public static <E> CollectionBuilder<E, TreeSet<E>> createTreeSet()
   {
      return create(new TreeSet<E>());
   }
   
   public static <E> CollectionBuilder<E, TreeSet<E>> createTreeSet(Comparator<? super E> comparator)
   {
      return create(new TreeSet<E>(comparator));
   }
   
   public CollectionBuilder(T internalCollection)
   {
      _internalCollection = internalCollection;
   }
   
   public CollectionBuilder<E, T> add(E e)
   {
      _internalCollection.add(e);
      return this;
   }
   
   public CollectionBuilder<E, T> addAll(Collection<? extends E> c)
   {
      _internalCollection.addAll(c);
      return this;
   }
   
   public CollectionBuilder<E, T> addAll(E... elements)
   {
      return addAll(Arrays.asList(elements));
   }
   
   public CollectionBuilder<E, T> remove(Object o)
   {
      _internalCollection.remove(o);
      return this;
   }
   
   public CollectionBuilder<E, T> removeAll(Collection<?> c)
   {
      _internalCollection.removeAll(c);
      return this;
   }
   
   public CollectionBuilder<E, T> removeAll(E... elements)
   {
      return removeAll(Arrays.asList(elements));
   }
   
   public CollectionBuilder<E, T> retainAll(Collection<?> c)
   {
      _internalCollection.retainAll(c);
      return this;
   }
   
   public CollectionBuilder<E, T> retainAll(E... elements)
   {
      return retainAll(Arrays.asList(elements));
   }
   
   public CollectionBuilder<E, T> clear()
   {
      _internalCollection.clear();
      return this;
   }
   
   public T toCollection()
   {
      return _internalCollection;
   }
   
   public String toString()
   {
      return _internalCollection.toString();
   }
}
