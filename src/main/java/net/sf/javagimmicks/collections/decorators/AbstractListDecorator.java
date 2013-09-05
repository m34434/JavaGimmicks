package net.sf.javagimmicks.collections.decorators;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractListDecorator<E> extends AbstractCollectionDecorator<E> implements List<E>
{
   private static final long serialVersionUID = -1438615027180189129L;

   protected AbstractListDecorator(List<E> decorated)
   {
      super(decorated);
   }

   @Override
   public List<E> getDecorated()
   {
      return (List<E>)super.getDecorated();
   }

   public void add(int index, E element)
   {
      getDecorated().add(index, element);
   }

   public boolean addAll(int index, Collection<? extends E> c)
   {
      return getDecorated().addAll(index, c);
   }

   public E get(int index)
   {
      return getDecorated().get(index);
   }

   public int indexOf(Object o)
   {
      return getDecorated().indexOf(o);
   }

   public int lastIndexOf(Object o)
   {
      return getDecorated().lastIndexOf(o);
   }

   public ListIterator<E> listIterator()
   {
      return getDecorated().listIterator();
   }

   public ListIterator<E> listIterator(int index)
   {
      return getDecorated().listIterator(index);
   }

   public E remove(int index)
   {
      return getDecorated().remove(index);
   }

   public E set(int index, E element)
   {
      return getDecorated().set(index, element);
   }

   public List<E> subList(int fromIndex, int toIndex)
   {
      return getDecorated().subList(fromIndex, toIndex);
   }
}
