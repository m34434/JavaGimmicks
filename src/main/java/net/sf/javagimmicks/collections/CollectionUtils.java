package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils
{
   public static StringBuilder concatElements(Collection<?> c, String separator)
   {
      StringBuilder builder = new StringBuilder();

      if(c.isEmpty())
      {
         return builder;
      }
      
      Iterator<?> oIterator = c.iterator();
      builder.append(oIterator.next());
      
      while(oIterator.hasNext())
      {
         builder.append(separator).append(oIterator.next());
      }
      
      return builder;
   }
   
   public static <T> void addAll(Collection<T> targetCollection, Iterator<? extends T> sourceIterator)
   {
      while(sourceIterator.hasNext())
      {
         targetCollection.add(sourceIterator.next());
      }
   }
}
