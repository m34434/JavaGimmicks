package net.sf.javagimmicks.graph.routing;

import java.util.Map.Entry;

import net.sf.javagimmicks.graph.DefaultEdgeFactory.DefaultEdge;
import net.sf.javagimmicks.graph.Graph;
import net.sf.javagimmicks.graph.MapGraphBuilder;
import net.sf.javagimmicks.graph.Route;

import org.junit.Test;

public class DijkstraRouteFinderTest
{
   @Test
   public void testDijkstra() throws Exception
   {
      final MapGraphBuilder<String, DefaultEdge<String>> builder = MapGraphBuilder.createDefaultHashInstance();
      final Graph<String, DefaultEdge<String>> graph = builder.build();

      addEdge(graph, "1", "2", 1);
      addEdge(graph, "1", "3", 4);
      addEdge(graph, "1", "5", 7);
      addEdge(graph, "2", "3", 2);
      addEdge(graph, "3", "4", 1);
      addEdge(graph, "4", "6", 1);
      addEdge(graph, "5", "3", 2);
      addEdge(graph, "5", "6", 3);

      final DijkstraRouteFinder<String, DefaultEdge<String>> routingAlgorithm = DijkstraRouteFinder
            .createInstance(graph);
      for (final Entry<String, Route<String, DefaultEdge<String>>> entry : routingAlgorithm.findRoutes("1").entrySet())
      {
         System.out.println("Target: " + entry.getKey());
         System.out.println(entry.getValue());
         System.out.println();
      }
   }

   private static <V> void addEdge(final Graph<V, DefaultEdge<V>> graph, final V source, final V target,
         final double cost)
   {
      graph.addEdge(source, target).setCost(cost);
   }
}
