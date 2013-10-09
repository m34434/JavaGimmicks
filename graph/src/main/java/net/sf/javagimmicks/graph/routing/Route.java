package net.sf.javagimmicks.graph.routing;

import java.util.List;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.Graph;
import net.sf.javagimmicks.graph.WeightedEdge;

/**
 * Represents a route between two vertices with a {@link Graph}.
 * <p>
 * A route is a {@link List} of {@link Edge}s that knows its source and target
 * vertex as well as an optional cost.
 * 
 * @param <VertexType>
 *           the type of vertices of this {@link Route}
 * @param <EdgeType>
 *           the type of {@link Edge}s of this {@link Route}
 */
public interface Route<VertexType, EdgeType extends Edge<VertexType, EdgeType>> extends List<EdgeType>
{
   /**
    * Returns the cost of this {@link Route} - which is the sum of it's
    * {@link Edge}s (if they are {@link WeightedEdge}s).
    * 
    * @return the cost of this {@link Route}
    */
   double getCost();

   /**
    * Returns the source vertex of this {@link Route}.
    * 
    * @return the source vertex of this {@link Route}
    */
   VertexType getSourceVertex();

   /**
    * Returns the target vertex of this {@link Route}.
    * 
    * @return the target vertex of this {@link Route}
    */
   VertexType getTargetVertex();
}
