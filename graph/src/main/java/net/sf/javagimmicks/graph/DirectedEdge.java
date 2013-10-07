package net.sf.javagimmicks.graph;

/**
 * A directed version of {@link Edge} that has a well-defined
 * {@link #getSourceVertex() source} side and {@link #getTargetVertex() target}
 * side.
 */
public interface DirectedEdge<VertexType, EdgeType extends DirectedEdge<VertexType, EdgeType>> extends Edge<VertexType, EdgeType>
{
   /**
    * Returns the source vertex of this {@link DirectedEdge}.
    * 
    * @return the source vertex of this {@link DirectedEdge}
    */
   public VertexType getSourceVertex();

   /**
    * Returns the target vertex of this {@link DirectedEdge}.
    * 
    * @return the target vertex of this {@link DirectedEdge}
    */
   public VertexType getTargetVertex();
}
