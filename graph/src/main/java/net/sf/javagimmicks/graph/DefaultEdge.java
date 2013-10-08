package net.sf.javagimmicks.graph;

public class DefaultEdge<VertexType> extends AbstractDefaultEdge<VertexType, DefaultEdge<VertexType>>
{
   public DefaultEdge(final Graph<VertexType, DefaultEdge<VertexType>> graph, final VertexType source, final VertexType target)
   {
      super(graph, source, target);
   }
}