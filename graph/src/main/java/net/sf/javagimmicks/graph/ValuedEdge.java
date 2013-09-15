package net.sf.javagimmicks.graph;

public interface ValuedEdge<V, O, E extends ValuedEdge<V, O, E>> extends Edge<V, E>
{
   public O getValue();
}
