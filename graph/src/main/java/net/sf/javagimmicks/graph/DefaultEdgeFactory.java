package net.sf.javagimmicks.graph;

import java.util.Arrays;
import java.util.Collection;

import net.sf.javagimmicks.lang.LangUtils;

public class DefaultEdgeFactory<V> implements EdgeFactory<V, DefaultEdgeFactory.DefaultEdge<V>>
{
   public DefaultEdge<V> createEdge(Graph<V, DefaultEdge<V>> graph, V source, V target)
   {
      return new DefaultEdge<V>(graph, source, target);
   }

   public static abstract class AbstractDefaultEdge<V, E extends AbstractDefaultEdge<V, E>> implements Edge<V, E>, DirectedEdge<V, E>, WeightedEdge<V, E>
   {
      protected final Graph<V, ? extends E> _graph;
      protected final V _source;
      protected final V _target;

      protected double _cost = 1.0;

      public AbstractDefaultEdge(Graph<V, ? extends E> graph, V source, V target)
      {
         _graph = graph;
         _source = source;
         _target = target;
      }

      public double getCost()
      {
         return _cost;
      }
      
      public void setCost(double cost)
      {
         _cost = cost;
      }
      
      public Graph<V, ? extends E> getGraph()
      {
         return _graph;
      }

      public V getSourceVertex()
      {
         return _source;
      }

      public V getTargetVertex()
      {
         return _target;
      }
      
      public V getOutgoingVertex(V incoming)
      {
         if(LangUtils.equalsNullSafe(incoming, _source))
         {
            return _target;
         }
         else if(LangUtils.equalsNullSafe(incoming, _target))
         {
            return _source;
         }
         else
         {
            return null;
         }
      }
      
      public boolean connectsTo(V vertex)
      {
         return
            LangUtils.equalsNullSafe(vertex, _source) ||
            LangUtils.equalsNullSafe(vertex, _target);
      }

      @SuppressWarnings("unchecked")
      public Collection<V> getVerteces()
      {
         return Arrays.asList(_source, _target);
      }

      public String toString(V incoming)
      {
         return new StringBuilder()
            .append(incoming)
            .append("->")
            .append(getOutgoingVertex(incoming))
            .toString();
      }

      @Override
      public String toString()
      {
         return toString(_source);
      }
   }
   
   public static class DefaultEdge<V> extends AbstractDefaultEdge<V, DefaultEdge<V>>
   {
      public DefaultEdge(Graph<V, DefaultEdge<V>> graph, V source, V target)
      {
         super(graph, source, target);
      }
   }
}
