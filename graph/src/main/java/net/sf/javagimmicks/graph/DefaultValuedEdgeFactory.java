package net.sf.javagimmicks.graph;


public class DefaultValuedEdgeFactory<V, O> implements EdgeFactory<V, DefaultValuedEdge<V, O>>
{
   public DefaultValuedEdge<V, O> createEdge(Graph<V, DefaultValuedEdge<V, O>> graph, V source, V target, O value)
   {
      return new DefaultValuedEdge<V, O>(graph, source, target, value);
   }

   public DefaultValuedEdge<V, O> createEdge(Graph<V, DefaultValuedEdge<V, O>> graph, V source, V target)
   {
      return createEdge(graph, source, target, null);
   }
}
