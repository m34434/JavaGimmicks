package net.sf.javagimmicks.graph;

import net.sf.javagimmicks.graph.DefaultEdgeFactory.AbstractDefaultEdge;

public class DefaultValuedEdgeFactory<V, O> implements EdgeFactory<V, DefaultValuedEdgeFactory.DefaultValuedEdge<V, O>>
{
   public DefaultValuedEdge<V, O> createEdge(Graph<V, DefaultValuedEdge<V, O>> graph, V source, V target, O value)
   {
      return new DefaultValuedEdge<V, O>(graph, source, target, value);
   }

   public DefaultValuedEdge<V, O> createEdge(Graph<V, DefaultValuedEdge<V, O>> graph, V source, V target)
   {
      return createEdge(graph, source, target, null);
   }

   public static abstract class AbstractDefaultValuedEdge<V, O, E extends AbstractDefaultValuedEdge<V, O, E>> extends AbstractDefaultEdge<V, E> implements ValuedEdge<V, O, E>
   {
      protected O _value;

      public AbstractDefaultValuedEdge(Graph<V, ? extends E> graph, V source, V target, O value)
      {
         super(graph, source, target);

         setValue(value);
      }

      public O getValue()
      {
         return _value;
      }
      
      public void setValue(O value)
      {
         _value = value;
      }

      @Override
      public String toString()
      {
         final StringBuilder result = new StringBuilder(super.toString());
         if(_value != null)
         {
            result.append(": ").append(_value);
         }
         
         return result.toString();
      }
   }
   
   public static class DefaultValuedEdge<V, O> extends AbstractDefaultValuedEdge<V, O, DefaultValuedEdge<V, O>>
   {
      public DefaultValuedEdge(Graph<V, DefaultValuedEdge<V, O>> graph, V source,
            V target, O value)
      {
         super(graph, source, target, value);
      }
   }
}
