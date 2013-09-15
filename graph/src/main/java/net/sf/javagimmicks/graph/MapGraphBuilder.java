package net.sf.javagimmicks.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.javagimmicks.graph.DefaultEdgeFactory.DefaultEdge;
import net.sf.javagimmicks.lang.Factory;

public class MapGraphBuilder<V, E extends Edge<V, E>>
{
   protected boolean _directed = false;
   protected Map<V, Set<E>> _edgeMap = new HashMap<V, Set<E>>();
   protected Factory<? extends Set<E>> _setFactory = new HashSetFactory<E>();
   protected EdgeFactory<V, E> _edgeFactory = null;

   public static <V> MapGraphBuilder<V, DefaultEdge<V>> createDefaultInstance(Map<V, Set<DefaultEdge<V>>> edges, Factory<Set<DefaultEdge<V>>> setFactory)
   {
      return new MapGraphBuilder<V, DefaultEdge<V>>()
         .setEdgeMap(edges)
         .setSetFactory(setFactory)
         .setEdgeFactory(new DefaultEdgeFactory<V>());
   }
   
   public static <V> MapGraphBuilder<V, DefaultEdge<V>> createDefaultHashInstance()
   {
      return createDefaultInstance(new HashMap<V, Set<DefaultEdge<V>>>(), new HashSetFactory<DefaultEdge<V>>());
   }
   
   public MapGraph<V, E> build()
   {
      if(_edgeFactory == null)
      {
         throw new IllegalStateException("No EdgeFactory set yet!");
      }
      
      return new MapGraph<V, E>(_edgeMap, _setFactory, _edgeFactory, _directed);
   }
   
   public boolean isDirected()
   {
      return _directed;
   }

   public Map<V, Set<E>> getEdgeMap()
   {
      return _edgeMap;
   }

   public Factory<? extends Set<E>> getSetFactory()
   {
      return _setFactory;
   }

   public EdgeFactory<V, E> getEdgeFactory()
   {
      return _edgeFactory;
   }

   public MapGraphBuilder<V, E> setDirected(boolean directed)
   {
      _directed = directed;
      
      return this;
   }

   public MapGraphBuilder<V, E> setEdgeMap(Map<V, Set<E>> edgeMap)
   {
      _edgeMap = edgeMap;
      
      return this;
   }

   public MapGraphBuilder<V, E> setSetFactory(Factory<? extends Set<E>> setFactory)
   {
      _setFactory = setFactory;
      
      return this;
   }

   public MapGraphBuilder<V, E> setEdgeFactory(EdgeFactory<V, E> edgeFactory)
   {
      _edgeFactory = edgeFactory;
      
      return this;
   }

   public static class HashSetFactory<E> implements Factory<Set<E>>
   {
      public Set<E> create()
      {
         return new HashSet<E>();
      }
   }

   public static class TreeSetFactory<E> implements Factory<Set<E>>
   {
      public Set<E> create()
      {
         return new TreeSet<E>();
      }
   }
}
