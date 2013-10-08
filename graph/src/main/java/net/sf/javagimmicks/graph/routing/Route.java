package net.sf.javagimmicks.graph.routing;

import java.util.List;

import net.sf.javagimmicks.graph.Edge;


public interface Route<V, E extends Edge<V, E>> extends List<E>
{
   public double getCost();
   
   public V getSourceVertex();
   public V getTargetVertex();
}
