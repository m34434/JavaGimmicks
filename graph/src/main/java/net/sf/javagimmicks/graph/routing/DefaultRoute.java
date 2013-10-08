package net.sf.javagimmicks.graph.routing;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.WeightedEdge;

public class DefaultRoute<V, E extends Edge<V, E>> extends ArrayList<E> implements Route<V, E>
{
   private static final long serialVersionUID = 8309167375893037566L;

   protected final V _source;
   protected final V _target;
   
   public DefaultRoute(V source, V target)
   {
      _source = source;
      _target = target;
   }

   public double getCost()
   {
      double cost = 0.0;
      
      for(E edge : this)
      {
         cost += (edge instanceof WeightedEdge<?, ?>) ?
            ((WeightedEdge<V, ?>)edge).getCost() : 1.0;
      }
      
      return cost;
   }
   
   public V getSourceVertex()
   {
      return _source;
   }

   public V getTargetVertex()
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
         
      if(!isEmpty())
      {
         result.append(" (");
         
         final Iterator<E> edgeIterator = iterator();
         V source = _source;
         E edge = edgeIterator.next();
         
         result.append(edge.toString(_source));
         
         while(edgeIterator.hasNext())
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
