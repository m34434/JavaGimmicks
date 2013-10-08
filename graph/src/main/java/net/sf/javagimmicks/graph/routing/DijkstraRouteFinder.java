package net.sf.javagimmicks.graph.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.javagimmicks.graph.Edge;
import net.sf.javagimmicks.graph.Graph;
import net.sf.javagimmicks.graph.WeightedEdge;

public class DijkstraRouteFinder<V, E extends Edge<V, E>> extends AbstractRouteFinder<V, E>
{
   public static <V, E extends Edge<V, E>> DijkstraRouteFinder<V, E> createInstance(Graph<V, E> graph)
   {
      return new DijkstraRouteFinder<V, E>(graph);
   }
   
   public DijkstraRouteFinder(Graph<V, E> graph)
   {
      super(graph);
   }

   public Route<V, E> findRoute(V source, V target)
   {
      final Map<V, Route<V, E>> result = new HashMap<V, Route<V,E>>();
      findRoutes(_graph, source, result, target);
      
      return result.get(target);
   }

   public Map<V, Route<V, E>> findRoutes(V source)
   {
      final Map<V, Route<V, E>> result = new HashMap<V, Route<V,E>>();
      findRoutes(_graph, source, result, null);
      
      return result;
   }

   public static <V, E extends Edge<V, E>> void findRoutes(Graph<V, E> graph, V source, Map<V, Route<V, E>> routes, V optionalTargetVertex)
   {
      final Map<V, Double> costs = new HashMap<V, Double>();
      costs.put(source, 0.0);

      final Map<V, E> previous = new HashMap<V, E>();
      
      doFindRoutes(graph, costs, previous, optionalTargetVertex);
      
      if(optionalTargetVertex == null)
      {
         for(V target : previous.keySet())
         {
            createAddRoute(routes, costs, previous, target);
         }
      }
      else
      {
         createAddRoute(routes, costs, previous, optionalTargetVertex);
      }
   }
   
   protected static <V, E extends Edge<V, E>> void doFindRoutes(final Graph<V, E> graph, final Map<V, Double> costs, final Map<V, E> previous, V optionalTargetVertex)
   {
      final List<V> vertexList = new ArrayList<V>(graph.vertexSet());
      sortVerticesByCost(vertexList, costs);

      while(!vertexList.isEmpty())
      {
         final V currentVertex = vertexList.remove(0);
         final Double currentCost = costs.get(currentVertex);
         
         if(currentCost == null)
         {
            break;
         }
         
         for(E edge : graph.edgesOf(currentVertex))
         {
            final V targetVertex = edge.getOutgoingVertex(currentVertex);
            
            final double targetNewCost = currentCost +
               ((edge instanceof WeightedEdge<?, ?>) ? ((WeightedEdge<V, ?>)edge).getCost() : 1.0);
            final Double targetCurrentCost = costs.get(targetVertex);
            
            if(targetCurrentCost == null || targetNewCost < targetCurrentCost)
            {
               costs.put(targetVertex, targetNewCost);
               previous.put(targetVertex, edge);
               
               sortVerticesByCost(vertexList, costs);
            }
            
            if(optionalTargetVertex != null && optionalTargetVertex.equals(targetVertex))
            {
               return;
            }
         }
      }
   }
   
   protected static <V, E extends Edge<V, E>> void createAddRoute(final Map<V, Route<V, E>> routes, final Map<V, Double> costs, final Map<V, E> previous, V target)
   {
      // Skip if route is already there
      if(routes.containsKey(target))
      {
         return;
      }

      final E edge = previous.get(target);

      // Trivial case - the source vertex itself
      if(edge == null)
      {
         routes.put(target, new DefaultRoute<V, E>(target, target));
         return;
      }
      
      // Get the source vertex and (optionally build the route for it)
      final V source = edge.getOutgoingVertex(target);
      createAddRoute(routes, costs, previous, source);

      // Build a new route by adding the current edge to the route of the previous node
      final Route<V, E> sourceRoute = routes.get(source);
      final DefaultRoute<V, E> newRoute = new DefaultRoute<V, E>(sourceRoute.getSourceVertex(), target);
      newRoute.addAll(sourceRoute);
      newRoute.add(edge);

      routes.put(target, newRoute);
   }

   protected static <V> void sortVerticesByCost(final List<V> vertexList, final Map<V, Double> costs)
   {
      Collections.sort(vertexList, new DistComparator<V>(costs));
   }
   
   protected static final class DistComparator<V> implements Comparator<V>
   {
      protected final Map<V, Double> _costs;

      public DistComparator(Map<V, Double> costs)
      {
         _costs = costs;
      }

      public int compare(V v1, V v2)
      {
         Double d1 = _costs.get(v1);
         Double d2 = _costs.get(v2);
         
         if(d1 == null)
         {
            return d2 == null ? 0 : 1;
         }
         else if(d2 == null)
         {
            return -1;
         }
         else
         {
            return d1.compareTo(d2);
         }
      }
   }
}
