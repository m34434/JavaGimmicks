package net.sf.javagimmicks.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.graph.DefaultEdgeFactory.DefaultEdge;
import net.sf.javagimmicks.lang.Factory;

public class MapGraphBuilder<V, E extends Edge<V, E>>
{
   protected boolean _directed = false;
   protected Map<V, Set<E>> _edgeMap = new HashMap<V, Set<E>>();
   protected Factory<? extends Set<E>> _edgeSetFactory = new HashSetFactory<E>();
   protected EdgeFactory<V, E> _edgeFactory = null;

   public static <V> MapGraphBuilder<V, DefaultEdge<V>> createDefaultInstance(final Map<V, Set<DefaultEdge<V>>> edges,
         final Factory<Set<DefaultEdge<V>>> edgeSetFactory)
   {
      return new MapGraphBuilder<V, DefaultEdge<V>>()
            .setEdgeMap(edges)
            .setEdgeSetFactory(edgeSetFactory)
            .setEdgeFactory(new DefaultEdgeFactory<V>());
   }

   public static <V> MapGraphBuilder<V, DefaultEdge<V>> createDefaultHashInstance()
   {
      return createDefaultInstance(new HashMap<V, Set<DefaultEdge<V>>>(), new HashSetFactory<DefaultEdge<V>>());
   }

   public MapGraph<V, E> build()
   {
      if (_edgeFactory == null)
      {
         throw new IllegalStateException("No EdgeFactory set yet!");
      }

      return new MapGraph<V, E>(_edgeMap, _edgeSetFactory, _edgeFactory, _directed);
   }

   public boolean isDirected()
   {
      return _directed;
   }

   public Map<V, Set<E>> getEdgeMap()
   {
      return _edgeMap;
   }

   public Factory<? extends Set<E>> getEdgeSetFactory()
   {
      return _edgeSetFactory;
   }

   public EdgeFactory<V, E> getEdgeFactory()
   {
      return _edgeFactory;
   }

   public MapGraphBuilder<V, E> setDirected(final boolean directed)
   {
      _directed = directed;

      return this;
   }

   public MapGraphBuilder<V, E> setEdgeMap(final Map<V, Set<E>> edgeMap)
   {
      _edgeMap = edgeMap;

      return this;
   }

   public MapGraphBuilder<V, E> setEdgeSetFactory(final Factory<? extends Set<E>> edgeSetFactory)
   {
      _edgeSetFactory = edgeSetFactory;

      return this;
   }

   public MapGraphBuilder<V, E> setEdgeFactory(final EdgeFactory<V, E> edgeFactory)
   {
      _edgeFactory = edgeFactory;

      return this;
   }

   private static class HashSetFactory<E> implements Factory<Set<E>>
   {
      @Override
      public Set<E> create()
      {
         return new HashSet<E>();
      }
   }
}
