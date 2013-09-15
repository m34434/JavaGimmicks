package net.sf.javagimmicks.graph;

import java.util.List;


public interface Route<V, E extends Edge<V, E>> extends List<E>
{
   public double getCost();
   
   public V getSourceVertex();
   public V getTargetVertex();
}
