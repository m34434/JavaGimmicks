package net.sf.javagimmicks.collections.composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public class CompositeUtils
{
   public static <E> Enumeration<E> enumeration(Collection<Enumeration<E>> enumerations)
   {
      return new CompositeEnumeration<E>(new ArrayList<Enumeration<E>>(enumerations));
   }

   public static <E> Enumeration<E> enumeration(Enumeration<E>... enumerations)
   {
      return enumeration(Arrays.asList(enumerations));
   }
   
   public static <E> Enumeration<E> enumeration(Enumeration<E> e1, Enumeration<E> e2)
   {
      return new CompositeEnumeration<E>(Arrays.asList(e1, e2));
   }

   public static <E, C extends Iterator<E>> Iterator<E> iterator(Collection<C> iterators)
   {
      return new CompositeIterator<E>(new ArrayList<C>(iterators));
   }

   public static <E> Iterator<E> iterator(Iterator<E>... iterators)
   {
      return iterator(Arrays.asList(iterators));
   }
   
   public static <E> Iterator<E> iterator(Iterator<E> i1, Iterator<E> i2)
   {
      return new CompositeIterator<E>(Arrays.asList(i1, i2));
   }

   public static <E, C extends Collection<E>> Collection<E> collection(Collection<C> collections)
   {
      return new CompositeCollection<E>(new ArrayList<C>(collections));
   }

   public static <E> Collection<E> collection(Collection<E>... collections)
   {
      return collection(Arrays.asList(collections));
   }
   
   public static <E> Collection<E> collection(Collection<E> c1, Collection<E> c2)
   {
      return new CompositeCollection<E>(Arrays.asList(c1, c2));
   }
   
   public static <E, C extends ListIterator<E>> ListIterator<E> listIterator(List<C> listIterators)
   {
      return new CompositeListIterator<E>(new ArrayList<C>(listIterators));
   }
   
   public static <E> ListIterator<E> listIterator(ListIterator<E>... listIterators)
   {
      return listIterator(Arrays.asList(listIterators));
   }
   
   public static <E> ListIterator<E> listIterator(ListIterator<E> it1, ListIterator<E> it2)
   {
      return new CompositeListIterator<E>(Arrays.asList(it1, it2));
   }
   
   public static <E, C extends List<E>> List<E> list(List<C> lists)
   {
      return new CompositeList<E>(new ArrayList<C>(lists));
   }
   
   public static <E> List<E> list(List<E>... lists)
   {
      return list(Arrays.asList(lists));
   }
   
   public static <E> List<E> list(List<E> l1, List<E> l2)
   {
      return new CompositeList<E>(Arrays.asList(l1, l2));
   }
}
