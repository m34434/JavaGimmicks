package net.sf.javagimmicks.graph;

public class DefaultValuedEdge<V, O> extends AbstractDefaultValuedEdge<V, O, DefaultValuedEdge<V, O>>
{
   public DefaultValuedEdge(final Graph<V, DefaultValuedEdge<V, O>> graph, final V source,
         final V target, final O value)
   {
      super(graph, source, target, value);
   }
}