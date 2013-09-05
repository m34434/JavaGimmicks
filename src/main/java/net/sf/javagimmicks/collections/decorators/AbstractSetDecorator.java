package net.sf.javagimmicks.collections.decorators;

import java.util.Set;

public abstract class AbstractSetDecorator<E> extends AbstractCollectionDecorator<E> implements Set<E>
{
   private static final long serialVersionUID = 1997599261091959009L;

   protected AbstractSetDecorator(Set<E> decorated)
   {
      super(decorated);
   }

   public Set<E> getDecorated()
   {
      return (Set<E>)super.getDecorated();
   }
}
