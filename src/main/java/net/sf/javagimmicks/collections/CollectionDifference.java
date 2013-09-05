package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionDifference<E>
{
   protected final List<E> _onlyA = new ArrayList<E>();
   protected final List<E> _onlyB = new ArrayList<E>();
   protected final List<E> _both = new ArrayList<E>();
   
   public CollectionDifference(Collection<? extends E> a, Collection<? extends E> b)
   {
      for(E element : a)
      {
         if(b.contains(element))
         {
            _both.add(element);
         }
         else
         {
            _onlyA.add(element);
         }
      }
      
      for(E element : b)
      {
         if(!_both.contains(element))
         {
            _onlyB.add(element);
         }
      }
   }

   public List<E> getOnlyA()
   {
      return _onlyA;
   }

   public List<E> getOnlyB()
   {
      return _onlyB;
   }

   public List<E> getBoth()
   {
      return _both;
   }
}
