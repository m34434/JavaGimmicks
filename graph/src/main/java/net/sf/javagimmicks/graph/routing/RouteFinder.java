package net.sf.javagimmicks.graph.routing;

import java.util.Map;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.Graph;

/**
 * Represents objects that can find {@link Route}s between vertices of a
 * {@link Graph}.
 * 
 * @param <VertexType>
 *           the type of vertices of the {@link Route}s to find
 * @param <EdgeTyoe>
 *           the type of {@link Edge}s of the {@link Route}s to find
 */
public interface RouteFinder<VertexType, EdgeTyoe extends Edge<VertexType, EdgeTyoe>>
{
   /**
    * Bulk-finds all {@link Route}s to all target vertices that can be reached
    * from a given source vertex.
    * 
    * @param source
    *           the source vertex to find {@link Route}s for
    * @return a {@link Map} of all reachable target vertices and the respective
    *         {@link Route}s to them from the given source vertex
    */
   Map<VertexType, Route<VertexType, EdgeTyoe>> findRoutes(VertexType source);

   /**
    * Finds a {@link Route} between a given source and target vertex.
    * 
    * @param source
    *           the source vertex to find {@link Route} for
    * @param target
    *           the target vertex to find {@link Route} for
    * @return the resulting {@link Route} or {@code null} if the given target
    *         vertex is not reachable from the given source vertex.
    */
   Route<VertexType, EdgeTyoe> findRoute(VertexType source, VertexType target);
}
