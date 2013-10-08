package net.sf.javagimmicks.graph;

import java.util.Arrays;
import java.util.Collection;

import net.sf.javagimmicks.lang.LangUtils;

abstract class AbstractDefaultEdge<VertexType, EdgeType extends AbstractDefaultEdge<VertexType, EdgeType>> implements
      Edge<VertexType, EdgeType>,
      DirectedEdge<VertexType, EdgeType>, WeightedEdge<VertexType, EdgeType>
{
   protected final Graph<VertexType, ? extends EdgeType> _graph;
   protected final VertexType _source;
   protected final VertexType _target;

   protected double _cost = 1.0;

   AbstractDefaultEdge(final Graph<VertexType, ? extends EdgeType> graph, final VertexType source,
         final VertexType target)
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
   public Graph<VertexType, ? extends EdgeType> getGraph()
   {
      return _graph;
   }

   @Override
   public VertexType getSourceVertex()
   {
      return _source;
   }

   @Override
   public VertexType getTargetVertex()
   {
      return _target;
   }

   @Override
   public VertexType getOutgoingVertex(final VertexType incoming)
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
   public boolean connectsTo(final VertexType vertex)
   {
      return LangUtils.equalsNullSafe(vertex, _source) ||
            LangUtils.equalsNullSafe(vertex, _target);
   }

   @Override
   @SuppressWarnings("unchecked")
   public Collection<VertexType> getVertices()
   {
      return Arrays.asList(_source, _target);
   }

   @Override
   public String toString(final VertexType incoming)
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