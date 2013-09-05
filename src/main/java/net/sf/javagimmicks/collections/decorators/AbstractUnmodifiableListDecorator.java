package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractUnmodifiableListDecorator<E> extends AbstractList<E> implements Serializable
{
   private static final long serialVersionUID = 8953157735186723748L;

   protected final List<E> _decorated;
   
   protected AbstractUnmodifiableListDecorator(List<E> decorated)
   {
      _decorated = decorated;
   }
   
   public List<E> getDecorated()
   {
      return _decorated;
   }

   @Override
   public E get(int index)
   {
      return getDecorated().get(index);
   }

   @Override
   public int size()
   {
      return getDecorated().size();
   }

   public boolean contains(Object o)
   {
      return getDecorated().contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return getDecorated().containsAll(c);
   }

   public int indexOf(Object o)
   {
      return getDecorated().indexOf(o);
   }

   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   public int lastIndexOf(Object o)
   {
      return getDecorated().lastIndexOf(o);
   }

   public Object[] toArray()
   {
      return getDecorated().toArray();
   }

   public <T> T[] toArray(T[] a)
   {
      return getDecorated().toArray(a);
   }
   
   public String toString()
   {
      return getDecorated().toString();
   }
}
