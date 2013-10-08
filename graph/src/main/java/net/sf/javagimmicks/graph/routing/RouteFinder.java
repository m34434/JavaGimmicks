package net.sf.javagimmicks.graph.routing;

import java.util.Map;

import net.sf.javagimmicks.graph.Edge;

public interface RouteFinder<V, E extends Edge<V, E>>
{
   public Map<V, Route<V, E>> findRoutes(V source);
   public Route<V, E> findRoute(V source, V target);
}
