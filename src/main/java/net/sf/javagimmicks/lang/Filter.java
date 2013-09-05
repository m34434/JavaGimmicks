package net.sf.javagimmicks.lang;

public interface Filter<E>
{
   public boolean accepts(E element);
}
