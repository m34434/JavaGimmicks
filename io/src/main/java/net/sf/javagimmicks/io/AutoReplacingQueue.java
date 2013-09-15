package net.sf.javagimmicks.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class AutoReplacingQueue<E> implements Queue<E>
{
   protected final Queue<E> _internalQueue;

   protected final List<E> _pattern;
   protected final List<E> _replacement;
   
   public AutoReplacingQueue(Queue<E> internalQueue, List<E> pattern, List<E> replacement)
   {
      _internalQueue = internalQueue;
      
      _pattern = new ArrayList<E>(pattern);
      _replacement = new ArrayList<E>(replacement);
   }
   
   public boolean add(E e)
   {
      return _internalQueue.add(e);
   }

   public boolean addAll(Collection<? extends E> c)
   {
      return _internalQueue.addAll(c);
   }

   public void clear()
   {
      _internalQueue.clear();
   }

   public boolean contains(Object o)
   {
      return _internalQueue.contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return _internalQueue.containsAll(c);
   }

   public E element()
   {
      return _internalQueue.element();
   }

   public boolean equals(Object o)
   {
      return _internalQueue.equals(o);
   }

   public int hashCode()
   {
      return _internalQueue.hashCode();
   }

   public boolean isEmpty()
   {
      return _internalQueue.isEmpty();
   }

   public Iterator<E> iterator()
   {
      return _internalQueue.iterator();
   }

   public boolean offer(E e)
   {
      return _internalQueue.offer(e);
   }

   public E peek()
   {
      return _internalQueue.peek();
   }

   public E poll()
   {
      return _internalQueue.poll();
   }

   public E remove()
   {
      return _internalQueue.remove();
   }

   public boolean remove(Object o)
   {
      return _internalQueue.remove(o);
   }

   public boolean removeAll(Collection<?> c)
   {
      return _internalQueue.removeAll(c);
   }

   public boolean retainAll(Collection<?> c)
   {
      return _internalQueue.retainAll(c);
   }

   public int size()
   {
      return _internalQueue.size();
   }

   public Object[] toArray()
   {
      return _internalQueue.toArray();
   }

   public <T> T[] toArray(T[] a)
   {
      return _internalQueue.toArray(a);
   }

   
}
