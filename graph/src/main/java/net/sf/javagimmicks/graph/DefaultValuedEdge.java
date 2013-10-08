package net.sf.javagimmicks.graph;

/**
 * A default implementation of {@link Edge}, {@link DirectedEdge},
 * {@link WeightedEdge} and {@link ValuedEdge}.
 */
public class DefaultValuedEdge<VertexType, ValueType> extends
      AbstractDefaultValuedEdge<VertexType, ValueType, DefaultValuedEdge<VertexType, ValueType>>
{
   /**
    * Creates a new instance for the given {@link Graph}, source vertex, target
    * vertex and value.
    * 
    * @param graph
    *           the {@link Graph} to create the instance for
    * @param source
    *           the source vertex
    * @param target
    *           the target vertex
    * @param value
    *           the value that this instance should carry
    */
   public DefaultValuedEdge(final Graph<VertexType, DefaultValuedEdge<VertexType, ValueType>> graph,
         final VertexType source,
         final VertexType target, final ValueType value)
   {
      super(graph, source, target, value);
   }
}