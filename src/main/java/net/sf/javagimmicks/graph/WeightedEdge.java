package net.sf.javagimmicks.graph;

public interface WeightedEdge<V, E extends WeightedEdge<V, E>> extends Edge<V, E>
{
   public double getCost();
   public void setCost(double cost);
}
