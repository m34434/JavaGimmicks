package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractCollectionDecorator<E> implements Collection<E>, Serializable
{
   private static final long serialVersionUID = -5203666410345126066L;

   protected final Collection<E> _decorated;
   
   protected AbstractCollectionDecorator(Collection<E> decorated)
   {
      _decorated = decorated;
   }
   
   public Collection<E> getDecorated()
   {
      return _decorated;
   }

   public boolean add(E e)
   {
      return getDecorated().add(e);
   }

   public boolean addAll(Collection<? extends E> c)
   {
      return getDecorated().addAll(c);
   }

   public void clear()
   {
      getDecorated().clear();
   }

   public boolean contains(Object o)
   {
      return getDecorated().contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return getDecorated().containsAll(c);
   }

   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   public Iterator<E> iterator()
   {
      return getDecorated().iterator();
   }

   public boolean remove(Object o)
   {
      return getDecorated().remove(o);
   }

   public boolean removeAll(Collection<?> c)
   {
      return getDecorated().removeAll(c);
   }

   public boolean retainAll(Collection<?> c)
   {
      return getDecorated().retainAll(c);
   }

   public int size()
   {
      return getDecorated().size();
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
