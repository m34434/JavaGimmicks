package net.sf.javagimmicks.graph.routing;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.Graph;

/**
 * An very simple abstract {@link RouteFinder} that holds a reference to the
 * {@link Graph} where to find {@link Route}s.
 */
public abstract class AbstractRouteFinder<VertexType, EdgeType extends Edge<VertexType, EdgeType>> implements RouteFinder<VertexType, EdgeType>
{
   protected final Graph<VertexType, EdgeType> _graph;

   protected AbstractRouteFinder(final Graph<VertexType, EdgeType> graph)
   {
      _graph = graph;
   }
}
