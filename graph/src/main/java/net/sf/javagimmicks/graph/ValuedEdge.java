package net.sf.javagimmicks.graph;

/**
 * A special version of {@link Edge} that can carry additional information
 * (simply called "value").
 * 
 * @param <ValueType>
 *           the type of values that this {@link ValuedEdge} can carry
 */
public interface ValuedEdge<VertexType, ValueType, EdgeType extends ValuedEdge<VertexType, ValueType, EdgeType>> extends Edge<VertexType, EdgeType>
{
   /**
    * Returns the value of this {@link ValuedEdge}.
    * 
    * @return the value of this {@link ValuedEdge}
    */
   public ValueType getValue();
}
