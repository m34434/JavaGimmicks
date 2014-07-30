package net.sf.javagimmicks.collections8.composite;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class CompositeEnumeration<E> implements Enumeration<E>
{
   protected final Iterator<Enumeration<E>> _enumerations;
   
   protected Enumeration<E> _currentEnumeration;
   protected Enumeration<E> _nextEnumeration;
   
   CompositeEnumeration(List<Enumeration<E>> enumerations)
   {
      _enumerations = enumerations.iterator();
      
      findNextEnumeration();
   }
   
   public boolean hasMoreElements()
   {
      if(_currentEnumeration != null && _currentEnumeration.hasMoreElements())
      {
         return true;
      }
      
      findNextEnumeration();
      return _nextEnumeration != null && _nextEnumeration.hasMoreElements();
   }

   public E nextElement()
   {
      if(!hasMoreElements())
      {
         throw new NoSuchElementException();
      }
      
      return moveNext();
   }

   protected void findNextEnumeration()
   {
      if(_nextEnumeration != null)
      {
         return;
      }
      
      while(_enumerations.hasNext())
      {
          _nextEnumeration = _enumerations.next();
          
          if(_nextEnumeration.hasMoreElements())
          {
             break;
          }
      }
   }

   protected E moveNext()
   {
      if(_nextEnumeration != null)
      {
         _currentEnumeration = _nextEnumeration;
         _nextEnumeration = null;
      }
      
      return _currentEnumeration.nextElement();
   }
}