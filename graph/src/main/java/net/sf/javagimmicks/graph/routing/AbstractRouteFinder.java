package net.sf.javagimmicks.graph.routing;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.Graph;

public abstract class AbstractRouteFinder<V, E extends Edge<V, E>> implements RouteFinder<V, E>
{
   protected final Graph<V, E> _graph;

   protected AbstractRouteFinder(Graph<V, E> graph)
   {
      _graph = graph;
   }
}
