package net.sf.javagimmicks.graph;

/**
 * A {@link EdgeFactory} implementation that creates {@link DefaultValuedEdge}
 * instances.
 * <p>
 * Note that the interface method {@link #createEdge(Graph, Object, Object)} by
 * default leaves the value empty in the created {@link DefaultValuedEdge}
 * (because it cannot guess a value from the given input parameters), so for
 * filling {@link Graph} instances that operate on {@link ValuedEdge}s you
 * always need to set the value <b>after</b> the {@link ValuedEdge}s was created
 * (see {@link Graph#addEdge(Object, Object)} and
 * {@link Graph#addEdges(Object, java.util.Collection)}).
 * 
 * @param <ValueType>
 *           the type of values that this instance can create
 * @see Graph#addEdge(Object, Object)
 * @see Graph#addEdges(Object, java.util.Collection)
 */
public class DefaultValuedEdgeFactory<VertexType, ValueType> implements
      EdgeFactory<VertexType, DefaultValuedEdge<VertexType, ValueType>>
{
   /**
    * Creates a new instance.
    */
   public DefaultValuedEdgeFactory()
   {}

   /**
    * Creates a new {@link DefaultValuedEdge} for the given {@link Graph},
    * vertices and value.
    * 
    * @param graph
    *           the {@link Graph} to create a {@link DefaultValuedEdge} for
    * @param source
    *           the first vertex to create the {@link DefaultValuedEdge} for
    * @param target
    *           the second vertex to create the {@link DefaultValuedEdge} for
    * @param value
    *           the value to set in the created {@link DefaultValuedEdge}
    * @return the resulting {@link DefaultValuedEdge}
    */
   public DefaultValuedEdge<VertexType, ValueType> createEdge(
         final Graph<VertexType, DefaultValuedEdge<VertexType, ValueType>> graph, final VertexType source,
         final VertexType target, final ValueType value)
   {
      return new DefaultValuedEdge<VertexType, ValueType>(graph, source, target, value);
   }

   @Override
   public DefaultValuedEdge<VertexType, ValueType> createEdge(
         final Graph<VertexType, DefaultValuedEdge<VertexType, ValueType>> graph, final VertexType source,
         final VertexType target)
   {
      return createEdge(graph, source, target, null);
   }
}
