package net.sf.javagimmicks.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a graph data structure.
 * <p>
 * Graphs consist of two types of elements: vertices and {@link Edge edges}.
 * <p>
 * The first ones - <b>vertices</b> can by of an Java type and can have any
 * value. Nevertheless it is strongly recommended that they have a well-defined
 * {@link Object#equals(Object)} method. Concrete {@link Graph} might also have
 * additional requirements (like a well-defined {@link Object#hashCode()}
 * method).
 * <p>
 * Edges must implement the {@link Edge} interface (or one of it's
 * sub-interfaces) - please refer to the respective interface {@link Edge
 * documentation} for more details
 * 
 * @param <VertexType>
 *           the type of vertices of this {@link Graph}
 * @param <EdgeType>
 *           the type of {@link Edge}s of this {@link Graph}
 */
public interface Graph<VertexType, EdgeType extends Edge<VertexType, EdgeType>>
{
   Set<VertexType> vertexSet();

   Map<VertexType, Set<EdgeType>> edgeMap();

   boolean containsVertex(VertexType vertex);

   boolean addVertex(VertexType vertex);

   Set<EdgeType> removeVertex(VertexType vertex);

   Set<EdgeType> edgesOf(VertexType source);

   Set<VertexType> targetsOf(VertexType source);

   boolean isConnected(VertexType source, VertexType target);

   EdgeType getEdge(VertexType source, VertexType target);

   Set<EdgeType> getEdges(VertexType source, VertexType target);

   EdgeType addEdge(VertexType source, VertexType target);

   Set<EdgeType> addEdges(VertexType source, Collection<? extends VertexType> targets);

   EdgeType removeEdge(VertexType source, VertexType target);

   Set<EdgeType> removeEdges(VertexType source, VertexType target);

   Set<EdgeType> removeEdges(VertexType source, Collection<? extends VertexType> targets);
}
