package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Collection;

import net.sf.javagimmicks.collections.AbstractRing;
import net.sf.javagimmicks.collections.Ring;

public abstract class AbstractUnmodifiableRingDecorator<E> extends AbstractRing<E> implements Serializable
{
   private static final long serialVersionUID = -5807259095947621928L;

   protected final Ring<E> _decorated;
   
   protected AbstractUnmodifiableRingDecorator(Ring<E> decorated)
   {
      _decorated = decorated;
   }

   public Ring<E> getDecorated()
   {
      return _decorated;
   }

   @Override
   public boolean contains(Object o)
   {
      return getDecorated().contains(o);
   }

   @Override
   public boolean containsAll(Collection<?> c)
   {
      return getDecorated().containsAll(c);
   }

   @Override
   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   @Override
   public int size()
   {
      return getDecorated().size();
   }

   @Override
   public Object[] toArray()
   {
      return getDecorated().toArray();
   }

   @Override
   public <T> T[] toArray(T[] a)
   {
      return getDecorated().toArray(a);
   }
   
   @Override
   public String toString()
   {
      return getDecorated().toString();
   }
}
