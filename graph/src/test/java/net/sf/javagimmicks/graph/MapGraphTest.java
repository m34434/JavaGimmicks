package net.sf.javagimmicks.graph;


import org.junit.Assert;
import org.junit.Test;

public class MapGraphTest
{
   private static final String A = "a";
   private static final String B = "b";
   private static final String C = "c";
   private static final String D = "d";
   private static final String E = "e";
   private static final String F = "f";
   private static final String G = "g";

   @Test
   public void testBasicOperations()
   {
      final MapGraphBuilder<String, DefaultEdge<String>> builder = MapGraphBuilder.createDefaultHashInstance();
      final Graph<String, DefaultEdge<String>> graph = builder.build();

      Assert.assertTrue(graph.isEmpty());

      graph.addVertex(A);
      Assert.assertEquals(1, graph.size());
      Assert.assertTrue(graph.containsVertex(A));
      Assert.assertNotNull(graph.edgesOf(A));
      Assert.assertTrue(graph.edgesOf(A).isEmpty());

      graph.addEdge(B, C);
      Assert.assertEquals(3, graph.size());
      Assert.assertTrue(graph.containsVertex(B));
      Assert.assertNotNull(graph.edgesOf(B));
      Assert.assertEquals(1, graph.edgesOf(B).size());
      Assert.assertTrue(graph.containsVertex(C));
      Assert.assertNotNull(graph.edgesOf(C));
      Assert.assertEquals(1, graph.edgesOf(C).size());
   }
}
