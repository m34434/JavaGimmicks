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

/**
 * An implementation of {@link RouteFinder} that uses <a
 * href="http://en.wikipedia.org/wiki/Dijkstra's_algorithm">Dijkstra's
 * algorithm</a> for finding {@link Route}s.
 */
public class DijkstraRouteFinder<VertexType, EdgeType extends Edge<VertexType, EdgeType>> extends
      AbstractRouteFinder<VertexType, EdgeType>
{
   /**
    * Creates a new instance for the given {@link Graph}.
    * 
    * @param graph
    *           the {@link Graph} to create the instance for
    */
   public DijkstraRouteFinder(final Graph<VertexType, EdgeType> graph)
   {
      super(graph);
   }

   @Override
   public Route<VertexType, EdgeType> findRoute(final VertexType source, final VertexType target)
   {
      final Map<VertexType, Route<VertexType, EdgeType>> result = new HashMap<VertexType, Route<VertexType, EdgeType>>();
      findRoutes(_graph, source, result, target);

      return result.get(target);
   }

   @Override
   public Map<VertexType, Route<VertexType, EdgeType>> findRoutes(final VertexType source)
   {
      final Map<VertexType, Route<VertexType, EdgeType>> result = new HashMap<VertexType, Route<VertexType, EdgeType>>();
      findRoutes(_graph, source, result, null);

      return result;
   }

   protected static <V, E extends Edge<V, E>> void findRoutes(final Graph<V, E> graph, final V source,
         final Map<V, Route<V, E>> routes, final V optionalTargetVertex)
   {
      final Map<V, Double> costs = new HashMap<V, Double>();
      costs.put(source, 0.0);

      final Map<V, E> previous = new HashMap<V, E>();

      doFindRoutes(graph, costs, previous, optionalTargetVertex);

      if (optionalTargetVertex == null)
      {
         for (final V target : previous.keySet())
         {
            createAddRoute(routes, costs, previous, target);
         }
      }
      else
      {
         createAddRoute(routes, costs, previous, optionalTargetVertex);
      }
   }

   protected static <V, E extends Edge<V, E>> void doFindRoutes(final Graph<V, E> graph, final Map<V, Double> costs,
         final Map<V, E> previous, final V optionalTargetVertex)
   {
      final List<V> vertexList = new ArrayList<V>(graph.vertexSet());
      sortVerticesByCost(vertexList, costs);

      while (!vertexList.isEmpty())
      {
         final V currentVertex = vertexList.remove(0);
         final Double currentCost = costs.get(currentVertex);

         if (currentCost == null)
         {
            break;
         }

         for (final E edge : graph.edgesOf(currentVertex))
         {
            final V targetVertex = edge.getOutgoingVertex(currentVertex);

            final double targetNewCost = currentCost +
                  ((edge instanceof WeightedEdge<?, ?>) ? ((WeightedEdge<V, ?>) edge).getCost() : 1.0);
            final Double targetCurrentCost = costs.get(targetVertex);

            if (targetCurrentCost == null || targetNewCost < targetCurrentCost)
            {
               costs.put(targetVertex, targetNewCost);
               previous.put(targetVertex, edge);

               sortVerticesByCost(vertexList, costs);
            }

            if (optionalTargetVertex != null && optionalTargetVertex.equals(targetVertex))
            {
               return;
            }
         }
      }
   }

   protected static <V, E extends Edge<V, E>> void createAddRoute(final Map<V, Route<V, E>> routes,
         final Map<V, Double> costs, final Map<V, E> previous, final V target)
   {
      // Skip if route is already there
      if (routes.containsKey(target))
      {
         return;
      }

      final E edge = previous.get(target);

      // Trivial case - the source vertex itself
      if (edge == null)
      {
         routes.put(target, new DefaultRoute<V, E>(target, target));
         return;
      }

      // Get the source vertex and (optionally build the route for it)
      final V source = edge.getOutgoingVertex(target);
      createAddRoute(routes, costs, previous, source);

      // Build a new route by adding the current edge to the route of the
      // previous node
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

      public DistComparator(final Map<V, Double> costs)
      {
         _costs = costs;
      }

      @Override
      public int compare(final V v1, final V v2)
      {
         final Double d1 = _costs.get(v1);
         final Double d2 = _costs.get(v2);

         if (d1 == null)
         {
            return d2 == null ? 0 : 1;
         }
         else if (d2 == null)
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
