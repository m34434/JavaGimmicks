package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.SortedMap;

public abstract class AbstractUnmodifiableSortedMapDecorator<K, V> extends AbstractUnmodifiableMapDecorator<K, V> implements SortedMap<K, V>
{
   private static final long serialVersionUID = 6155129365977806691L;

   protected AbstractUnmodifiableSortedMapDecorator(SortedMap<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public SortedMap<K, V> getDecorated()
   {
      return (SortedMap<K, V>) super.getDecorated();
   }

   public Comparator<? super K> comparator()
   {
      return getDecorated().comparator();
   }

   public K firstKey()
   {
      return getDecorated().firstKey();
   }

   public K lastKey()
   {
      return getDecorated().lastKey();
   }
}
