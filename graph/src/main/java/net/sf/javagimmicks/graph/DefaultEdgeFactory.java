package net.sf.javagimmicks.graph;

import java.util.Arrays;
import java.util.Collection;

import net.sf.javagimmicks.lang.LangUtils;

public class DefaultEdgeFactory<V> implements EdgeFactory<V, DefaultEdgeFactory.DefaultEdge<V>>
{
   @Override
   public DefaultEdge<V> createEdge(final Graph<V, DefaultEdge<V>> graph, final V source, final V target)
   {
      return new DefaultEdge<V>(graph, source, target);
   }

   public static abstract class AbstractDefaultEdge<V, E extends AbstractDefaultEdge<V, E>> implements Edge<V, E>,
         DirectedEdge<V, E>, WeightedEdge<V, E>
   {
      protected final Graph<V, ? extends E> _graph;
      protected final V _source;
      protected final V _target;

      protected double _cost = 1.0;

      public AbstractDefaultEdge(final Graph<V, ? extends E> graph, final V source, final V target)
      {
         _graph = graph;
         _source = source;
         _target = target;
      }

      @Override
      public double getCost()
      {
         return _cost;
      }

      @Override
      public void setCost(final double cost)
      {
         _cost = cost;
      }

      @Override
      public Graph<V, ? extends E> getGraph()
      {
         return _graph;
      }

      @Override
      public V getSourceVertex()
      {
         return _source;
      }

      @Override
      public V getTargetVertex()
      {
         return _target;
      }

      @Override
      public V getOutgoingVertex(final V incoming)
      {
         if (LangUtils.equalsNullSafe(incoming, _source))
         {
            return _target;
         }
         else if (LangUtils.equalsNullSafe(incoming, _target))
         {
            return _source;
         }
         else
         {
            return null;
         }
      }

      @Override
      public boolean connectsTo(final V vertex)
      {
         return LangUtils.equalsNullSafe(vertex, _source) ||
               LangUtils.equalsNullSafe(vertex, _target);
      }

      @Override
      @SuppressWarnings("unchecked")
      public Collection<V> getVertices()
      {
         return Arrays.asList(_source, _target);
      }

      @Override
      public String toString(final V incoming)
      {
         if (!connectsTo(incoming))
         {
            throw new IllegalArgumentException(String.format("'%1$s' is not connected to this Edge!"));
         }

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
      public DefaultEdge(final Graph<V, DefaultEdge<V>> graph, final V source, final V target)
      {
         super(graph, source, target);
      }
   }
}
