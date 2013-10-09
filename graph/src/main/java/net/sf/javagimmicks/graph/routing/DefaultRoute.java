package net.sf.javagimmicks.graph.routing;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.WeightedEdge;

/**
 * A simple default {@link Route} implementation based on an {@link ArrayList}
 * that can automatically calculate the {@link #getCost() cost}.
 */
public class DefaultRoute<VertexType, EdgeType extends Edge<VertexType, EdgeType>> extends ArrayList<EdgeType>
      implements Route<VertexType, EdgeType>
{
   private static final long serialVersionUID = 8309167375893037566L;

   protected final VertexType _source;
   protected final VertexType _target;

   public DefaultRoute(final VertexType source, final VertexType target)
   {
      _source = source;
      _target = target;
   }

   @Override
   public double getCost()
   {
      double cost = 0.0;

      for (final EdgeType edge : this)
      {
         cost += (edge instanceof WeightedEdge<?, ?>) ?
               ((WeightedEdge<VertexType, ?>) edge).getCost() : 1.0;
      }

      return cost;
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
   public String toString()
   {
      final StringBuilder result = new StringBuilder()
            .append(_source)
            .append("->")
            .append(_target);

      if (!isEmpty())
      {
         result.append(" (");

         final Iterator<EdgeType> edgeIterator = iterator();
         VertexType source = _source;
         EdgeType edge = edgeIterator.next();

         result.append(edge.toString(_source));

         while (edgeIterator.hasNext())
         {
            source = edge.getOutgoingVertex(source);
            edge = edgeIterator.next();
            result.append(", ").append(edge.toString(source));
         }

         result.append(")");
      }

      return result
            .append(" / Overall cost: ")
            .append(getCost())
            .toString();
   }
}
