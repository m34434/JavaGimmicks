package net.sf.javagimmicks.graph;

import java.util.Collection;

/**
 * Represents an edge of a {@link Graph} that connects two vertices together.
 * 
 * @param <VertexType>
 *           the type of vertices of the surrounding {@link Graph}
 * @param <EdgeType>
 *           the concrete type of {@link Edge}s
 */
public interface Edge<VertexType, EdgeType extends Edge<VertexType, EdgeType>>
{
   /**
    * Checks if this {@link Edge} connects to the given vertex.
    * 
    * @param vertex
    *           the vertex to check for connection
    * @return if this {@link Edge} connects to the given vertex
    */
   boolean connectsTo(VertexType vertex);

   /**
    * Returns the two vertices that this {@link Edge} connects to as a
    * {@link Collection}.
    * 
    * @return the two vertices that this {@link Edge} connects to as a
    *         {@link Collection}
    */
   Collection<VertexType> getVertices();

   /**
    * Returns the partner vertex of a given incoming vertex.
    * 
    * @param incoming
    *           the vertex the return the partner vertex for
    * @return the respective partner vertex or {@code null} if the vertex is not
    *         connected to this {@link Edge} (see {@link #connectsTo(Object)})
    */
   VertexType getOutgoingVertex(VertexType incoming);

   /**
    * Returns the enclosing {@link Graph}.
    * 
    * @return the enclosing {@link Graph}
    */
   Graph<VertexType, ? extends EdgeType> getGraph();

   /**
    * Returns a {@link String} representation of this {@link Edge} using the
    * given vertex as the incoming one and the other connected vertex as the
    * outgoing one.
    * 
    * @param incoming
    *           the vertex to use as the incoming one for {@link String}
    *           representation
    * @return the resulting {@link String} representation
    * @throws IllegalArgumentException
    *            if the given incoming vertex is not connected to this
    *            {@link Edge} (see {@link #connectsTo(Object)})
    */
   String toString(VertexType incoming) throws IllegalArgumentException;
}
