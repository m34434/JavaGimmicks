package net.sf.javagimmicks.graph;

/**
 * A default implementation of {@link Edge}, {@link DirectedEdge} and
 * {@link WeightedEdge}.
 */
public class DefaultEdge<VertexType> extends AbstractDefaultEdge<VertexType, DefaultEdge<VertexType>>
{
   /**
    * Creates a new instance for the given {@link Graph}, source vertex and
    * target vertex.
    * 
    * @param graph
    *           the {@link Graph} to create the instance for
    * @param source
    *           the source vertex
    * @param target
    *           the target vertex
    */
   public DefaultEdge(final Graph<VertexType, DefaultEdge<VertexType>> graph, final VertexType source,
         final VertexType target)
   {
      super(graph, source, target);
   }
}