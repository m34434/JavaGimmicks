package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ValueMappings<L, R, E> extends Iterable<ValueMappings.Mapping<L, R, E>>, Serializable
{
   public E put(L left, R right, E value);
   public void putLeft(R right, Map<? extends L, ? extends E> c);
   public void putRight(L left, Map<? extends R, ? extends E> c);

   public E get(L left, R right);
   
   public E remove(L left, R right);
   public Map<L, E> removeLeft(R right);
   public Map<R, E> removeRight(L left);

   public void clear();

   public boolean containsMapping(L left, R right);
   public boolean containsLeft(L left);
   public boolean containsRight(R right);
   
   public int size();
   public boolean isEmpty();
   
   public ValueMappings <R, L, E> getInverseMappings();

   public Set<Mapping<L, R, E>> getMappingSet();
   public Collection<E> getValues();

   public Map<L, Map<R, E>> getLeftOuterMap();
   public Map<R, Map<L, E>> getRightOuterMap();
   
   public Map<L, E> getLeftInnerMap(R right);
   public Map<R, E> getRightInnerMap(L left);
   
   public static interface Mapping<L, R, E> extends Serializable
   {
      public L getLeft();
      public R getRight();
      public E getValue();
      
      public Mapping<R, L, E> getInverseMapping();
   }
}
