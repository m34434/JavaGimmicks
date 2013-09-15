package net.sf.javagimmicks.graph;

import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.lang.Factory;

public class MapGraph<V, E extends Edge<V, E>> extends AbstractGraph<V, E>
{
   protected final Map<V, Set<E>> _edges;
   protected final EdgeFactory<V, E> _edgeFactory;
   
   protected final boolean _directed;
   
   MapGraph(Map<V, Set<E>> edges, Factory<? extends Set<E>> setFactory, EdgeFactory<V, E> edgeFactory, boolean directed)
   {
      super(setFactory);
      
      _edges = edges;
      _edgeFactory = edgeFactory;
      
      _directed = directed;
   }

   public Set<E> edgesOf(V source)
   {
      return _edges.get(source);
   }

   public Set<V> vertexSet()
   {
      return _edges.keySet();
   }

   @Override
   public E addEdge(V source, V target)
   {
      addVertex(source);
      addVertex(target);
      
      final E edge = _edgeFactory.createEdge(this, source, target);
      _edges.get(source).add(edge);
      
      if(!_directed)
      {
         _edges.get(target).add(edge);
      }
      
      return edge;
   }

   @Override
   public boolean addVertex(V vertex)
   {
      if(_edges.containsKey(vertex))
      {
         return false;
      }
      
      _edges.put(vertex, createEdgeSet());
      return true;
   }
}
