package net.sf.javagimmicks.graph;

public abstract class AbstractRouteFinder<V, E extends Edge<V, E>> implements RouteFinder<V, E>
{
   protected final Graph<V, E> _graph;

   protected AbstractRouteFinder(Graph<V, E> graph)
   {
      _graph = graph;
   }
}
