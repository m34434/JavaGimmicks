package net.sf.javagimmicks.graph;

import java.util.Map;

public interface RouteFinder<V, E extends Edge<V, E>>
{
   public Map<V, Route<V, E>> findRoutes(V source);
   public Route<V, E> findRoute(V source, V target);
}
