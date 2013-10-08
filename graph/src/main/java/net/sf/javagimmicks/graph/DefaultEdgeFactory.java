package net.sf.javagimmicks.graph;



public class DefaultEdgeFactory<V> implements EdgeFactory<V, DefaultEdge<V>>
{
   @Override
   public DefaultEdge<V> createEdge(final Graph<V, DefaultEdge<V>> graph, final V source, final V target)
   {
      return new DefaultEdge<V>(graph, source, target);
   }
}
