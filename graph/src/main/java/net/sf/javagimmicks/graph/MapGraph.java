package net.sf.javagimmicks.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.javagimmicks.util.Factory;

/**
 * An implementation of {@link Graph} that internally maintains a {@link Map} of
 * vertices and their associated {@link Edge}s.
 * <p>
 * Note that instances cannot be created directly - instead a
 * {@link MapGraphBuilder} needs to be used.
 * <p>
 * As a {@link Map} is the internal base - to have the {@link MapGraph} work
 * properly - vertices should provide a valid implementation of
 * {@link Object#equals(Object)} and {@link Object#hashCode()} if a
 * {@link HashMap} is provided internally and should implement
 * {@link Comparable} if a {@link TreeMap} without {@link Comparator} is used
 * internally.
 * <p>
 * {@link MapGraph} redirects {@link Edge} creation (resulting from a call to
 * {@link #addEdge(Object, Object)} or
 * {@link #addEdges(Object, java.util.Collection)}) to an internal
 * {@link EdgeFactory}.
 * <p>
 * A {@link MapGraph} also can be created in directed or non-directed mode. See
 * {@link #isDirected()} for more details about this modes.
 * 
 * @see MapGraphBuilder
 */
public class MapGraph<V, E extends Edge<V, E>> extends AbstractGraph<V, E>
{
   protected final Map<V, Set<E>> _edges;
   protected final EdgeFactory<V, E> _edgeFactory;

   protected final boolean _directed;

   MapGraph(final Map<V, Set<E>> edges, final Factory<? extends Set<E>> edgeSetFactory,
         final EdgeFactory<V, E> edgeFactory, final boolean directed)
   {
      super(edgeSetFactory);

      _edges = edges;
      _edgeFactory = edgeFactory;

      _directed = directed;
   }

   /**
    * Returns if this instance is created in directed or non-directed mode.
    * <p>
    * If the instance is created in <b>directed</b> mode, only one {@link Edge}
    * will be created for each call to {@link #addEdge(Object, Object)} or
    * vertex combination given within
    * {@link #addEdges(Object, java.util.Collection)}.
    * <p>
    * If the instance is created in <b>non-directed</b> mode, two {@link Edge}s
    * will be created for each call to {@link #addEdge(Object, Object)} or
    * vertex combination given within
    * {@link #addEdges(Object, java.util.Collection)} - one for each direction.
    * <p>
    * <b>Attention:</b> this behavior is completely isolated from the usage or
    * non-usage of {@link Edge}s of type {@link DirectedEdge} for this
    * {@link MapGraph} - it only influences the number of created {@link Edge}s
    * per add-operation and the vertex parameter order of each respective
    * internal call to {@link EdgeFactory#createEdge(Graph, Object, Object)}.
    * 
    * @return if this instance is directed
    */
   public boolean isDirected()
   {
      return _directed;
   }

   @Override
   public int size()
   {
      return vertexSet().size();
   }

   @Override
   public boolean isEmpty()
   {
      return size() == 0;
   }

   @Override
   public Set<E> edgesOf(final V source)
   {
      return _edges.get(source);
   }

   @Override
   public Set<V> vertexSet()
   {
      return _edges.keySet();
   }

   @Override
   public E addEdge(final V source, final V target)
   {
      addVertex(source);
      addVertex(target);

      final E edge = _edgeFactory.createEdge(this, source, target);
      _edges.get(source).add(edge);

      if (!_directed)
      {
         _edges.get(target).add(edge);
      }

      return edge;
   }

   @Override
   public boolean addVertex(final V vertex)
   {
      if (_edges.containsKey(vertex))
      {
         return false;
      }

      _edges.put(vertex, createEdgeSet());
      return true;
   }
}
