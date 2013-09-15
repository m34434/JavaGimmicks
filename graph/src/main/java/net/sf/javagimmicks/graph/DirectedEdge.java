package net.sf.javagimmicks.graph;

public interface DirectedEdge<V, E extends DirectedEdge<V, E>> extends Edge<V, E>
{
   public V getSourceVertex();
   public V getTargetVertex();
}
