package net.sf.javagimmicks.collections8.composite;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class CompositeList<E> extends AbstractList<E>
{
   protected final List<? extends List<E>> _lists;

   CompositeList(List<? extends List<E>> lists)
   {
      _lists = lists;
   }
   
   @Override
   public E get(int index)
   {
      for(List<E> list : _lists)
      {
         final int listSize = list.size();
         
         if(index < listSize)
         {
            return list.get(index);
         }
         
         index -= listSize;
      }
      
      throw new IndexOutOfBoundsException();
   }

   @Override
   public E set(int index, E element)
   {
      for(List<E> list : _lists)
      {
         final int listSize = list.size();
         
         if(index < listSize)
         {
            return list.set(index, element);
         }
         
         index -= listSize;
      }
      
      throw new IndexOutOfBoundsException();
   }
   
   @Override
   public E remove(int index)
   {
      for(List<E> list : _lists)
      {
         final int listSize = list.size();
         
         if(index < listSize)
         {
            return list.remove(index);
         }
         
         index -= listSize;
      }
      
      throw new IndexOutOfBoundsException();
   }

   @Override
   public int size()
   {
      int result = 0;
      for(List<E> list : _lists)
      {
         result += list.size();
      }
      
      return result;
   }

   @Override
   public ListIterator<E> listIterator(int index)
   {
      if(index < 0 || index > size())
      {
         throw new IndexOutOfBoundsException();
      }
      
      return new CompositeListIterator<E>(_lists, index);
   }

   @Override
   public Iterator<E> iterator()
   {
      return CompositeIterator.fromCollectionList(_lists);
   }
}
