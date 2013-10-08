package net.sf.javagimmicks.graph;

/**
 * A {@link EdgeFactory} implementation that creates {@link DefaultEdge}
 * instances.
 */
public class DefaultEdgeFactory<VertexType> implements EdgeFactory<VertexType, DefaultEdge<VertexType>>
{
   /**
    * Creates a new instance.
    */
   public DefaultEdgeFactory()
   {}

   @Override
   public DefaultEdge<VertexType> createEdge(final Graph<VertexType, DefaultEdge<VertexType>> graph,
         final VertexType source, final VertexType target)
   {
      return new DefaultEdge<VertexType>(graph, source, target);
   }
}
