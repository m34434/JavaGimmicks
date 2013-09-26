package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Mappings<L, R> extends Iterable<Mapping<L, R>>, Serializable
{
   public boolean put(L left, R right);
   public boolean putLeft(R right, Collection<? extends L> c);
   public boolean putRight(L left, Collection<? extends R> c);
   
   public boolean remove(L left, R right);
   public Set<L> removeLeft(R right);
   public Set<R> removeRight(L left);

   public void clear();

   public boolean contains(L left, R right);
   public boolean containsLeft(L left);
   public boolean containsRight(R right);
   
   public int size();
   public boolean isEmpty();
   
   public Mappings <R, L> getInverseMappings();

   public Set<Mapping<L, R>> getMappingSet();

   public Map<L, Set<R>> getLeftMap();
   public Map<R, Set<L>> getRightMap();
   
   public Set<L> getLeft(R right);
   public Set<R> getRight(L left);
}
