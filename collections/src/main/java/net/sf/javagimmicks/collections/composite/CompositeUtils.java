package net.sf.javagimmicks.collections.composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The central entry point into the composite API.
 * Provides numerous creation methods for different collection types.
 * <p>
 * Each method takes a number of collections of one type and returns
 * a new one of the same type that serves as a composite for the
 * given ones.
 */
@SuppressWarnings("unchecked")
public class CompositeUtils
{
   /**
    * Creates a composite {@link Enumeration} around any number of other ones
    * (provided as {@link Collection} of {@link Enumeration}s)
    * @param enumerations the {@link Enumeration}s to wrap a composite one around
    * @return a composite {@link Enumeration} wrapped around the given ones
    */
   public static <E> Enumeration<E> enumeration(Collection<Enumeration<E>> enumerations)
   {
      return new CompositeEnumeration<E>(new ArrayList<Enumeration<E>>(enumerations));
   }

   /**
    * Creates a composite {@link Enumeration} around any number of other ones
    * (provided as variable argument list of {@link Enumeration}s)
    * @param enumerations the {@link Enumeration}s to wrap a composite one around
    * @return a composite {@link Enumeration} wrapped around the given ones
    */
   public static <E> Enumeration<E> enumeration(Enumeration<E>... enumerations)
   {
      return enumeration(Arrays.asList(enumerations));
   }
   
   /**
    * Creates a composite {@link Enumeration} around two given ones
    * @param e1 the first {@link Enumeration} to build a composite one from
    * @param e2 the first {@link Enumeration} to build a composite one from
    * @return a composite {@link Enumeration} wrapped around the given ones
    */
   public static <E> Enumeration<E> enumeration(Enumeration<E> e1, Enumeration<E> e2)
   {
      return new CompositeEnumeration<E>(Arrays.asList(e1, e2));
   }

   /**
    * Creates a composite {@link Iterator} around any number of other ones
    * (provided as {@link Collection} of {@link Iterator}s)
    * @param iterators the {@link Iterator}s to wrap a composite one around
    * @return a composite {@link Iterator} wrapped around the given ones
    */
   public static <E, C extends Iterator<E>> Iterator<E> iterator(Collection<C> iterators)
   {
      return new CompositeIterator<E>(new ArrayList<C>(iterators));
   }

   /**
    * Creates a composite {@link Iterator} around any number of other ones
    * (provided as variable argument list of {@link Iterator}s)
    * @param iterators the {@link Iterator}s to wrap a composite one around
    * @return a composite {@link Iterator} wrapped around the given ones
    */
   public static <E> Iterator<E> iterator(Iterator<E>... iterators)
   {
      return iterator(Arrays.asList(iterators));
   }
   
   /**
    * Creates a composite {@link Iterator} around two given ones
    * @param i1 the first {@link Iterator} to build a composite one from
    * @param i2 the first {@link Iterator} to build a composite one from
    * @return a composite {@link Iterator} wrapped around the given ones
    */
   public static <E> Iterator<E> iterator(Iterator<E> i1, Iterator<E> i2)
   {
      return new CompositeIterator<E>(Arrays.asList(i1, i2));
   }

   /**
    * Creates a composite {@link Collection} around any number of other ones
    * (provided as {@link Collection} of {@link Collection}s)
    * @param collections the {@link Collection}s to wrap a composite one around
    * @return a composite {@link Collection} wrapped around the given ones
    */
   public static <E, C extends Collection<E>> Collection<E> collection(Collection<C> collections)
   {
      return new CompositeCollection<E>(new ArrayList<C>(collections));
   }

   /**
    * Creates a composite {@link Collection} around any number of other ones
    * (provided as variable argument list of {@link Collection}s)
    * @param collections the {@link Collection}s to wrap a composite one around
    * @return a composite {@link Collection} wrapped around the given ones
    */
   public static <E> Collection<E> collection(Collection<E>... collections)
   {
      return collection(Arrays.asList(collections));
   }
   
   /**
    * Creates a composite {@link Collection} around two given ones
    * @param c1 the first {@link Collection} to build a composite one from
    * @param c2 the first {@link Collection} to build a composite one from
    * @return a composite {@link Collection} wrapped around the given ones
    */
   public static <E> Collection<E> collection(Collection<E> c1, Collection<E> c2)
   {
      return new CompositeCollection<E>(Arrays.asList(c1, c2));
   }
   
   /**
    * Creates a composite {@link ListIterator} around any number of other ones
    * (provided as {@link List} of {@link ListIterator}s)
    * @param listIterators the {@link ListIterator}s to wrap a composite one around
    * @return a composite {@link ListIterator} wrapped around the given ones
    */
   public static <E, C extends ListIterator<E>> ListIterator<E> listIterator(List<C> listIterators)
   {
      return new CompositeListIterator<E>(new ArrayList<C>(listIterators));
   }
   
   /**
    * Creates a composite {@link ListIterator} around any number of other ones
    * (provided as variable argument list of {@link ListIterator}s)
    * @param listIterators the {@link ListIterator}s to wrap a composite one around
    * @return a composite {@link ListIterator} wrapped around the given ones
    */
   public static <E> ListIterator<E> listIterator(ListIterator<E>... listIterators)
   {
      return listIterator(Arrays.asList(listIterators));
   }
   
   /**
    * Creates a composite {@link ListIterator} around two given ones
    * @param it1 the first {@link ListIterator} to build a composite one from
    * @param it2 the first {@link ListIterator} to build a composite one from
    * @return a composite {@link ListIterator} wrapped around the given ones
    */
   public static <E> ListIterator<E> listIterator(ListIterator<E> it1, ListIterator<E> it2)
   {
      return new CompositeListIterator<E>(Arrays.asList(it1, it2));
   }
   
   /**
    * Creates a composite {@link List} around any number of other ones
    * (provided as {@link List} of {@link List}s)
    * @param lists the {@link List}s to wrap a composite one around
    * @return a composite {@link List} wrapped around the given ones
    */
   public static <E, C extends List<E>> List<E> list(List<C> lists)
   {
      return new CompositeList<E>(new ArrayList<C>(lists));
   }
   
   /**
    * Creates a composite {@link List} around any number of other ones
    * (provided as variable argument list of {@link List}s)
    * @param lists the {@link List}s to wrap a composite one around
    * @return a composite {@link List} wrapped around the given ones
    */
   public static <E> List<E> list(List<E>... lists)
   {
      return list(Arrays.asList(lists));
   }
   
   /**
    * Creates a composite {@link List} around two given ones
    * @param l1 the first {@link List} to build a composite one from
    * @param l2 the first {@link List} to build a composite one from
    * @return a composite {@link List} wrapped around the given ones
    */
   public static <E> List<E> list(List<E> l1, List<E> l2)
   {
      return new CompositeList<E>(Arrays.asList(l1, l2));
   }
}
