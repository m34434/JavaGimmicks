package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;

public interface Mapping<L, R> extends Serializable
{
   public L getLeft();

   public R getRight();

   public Mapping<R, L> getInverseMapping();
}