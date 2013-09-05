package net.sf.javagimmicks.collections;


public abstract class AbstractCursor<E> implements Cursor<E>
{
   public void insertAfter(Iterable<? extends E> collection)
   {
      int count = 0;
      for (E value : collection)
      {
         insertAfter(value);
         next();

         ++count;
      }

      previous(count);
   }

   public void insertBefore(Iterable<? extends E> collection)
   {
      for (E value : collection)
      {
         insertBefore(value);
      }
   }

   public E next(int count)
   {
      if (count < 0)
      {
         return previous(-count);
      }

      if (count == 0)
      {
         return get();
      }

      E result = null;
      for (int i = 0; i < count; ++i)
      {
         result = next();
      }

      return result;
   }

   public E previous(int count)
   {
      if (count < 0)
      {
         return next(-count);
      }

      if (count == 0)
      {
         return get();
      }

      E result = null;
      for (int i = 0; i < count; ++i)
      {
         result = previous();
      }

      return result;
   }
}