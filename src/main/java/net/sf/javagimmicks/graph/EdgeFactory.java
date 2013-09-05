package net.sf.javagimmicks.graph;

public interface EdgeFactory<V, E extends Edge<V, E>>
{
   public E createEdge(Graph<V, E> graph, V source, V target);
}
