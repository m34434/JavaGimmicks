package net.sf.javagimmicks.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.util.Factory;

/**
 * A builder for creating {@link MapGraph} instances using a fluent API.
 * <p>
 * A {@link MapGraph} needs three things for internal operation:
 * <ol>
 * <li>The core {@link Map} of vertices to a {@link Set} of {@link Edge}s - can
 * be provided via {@link #setEdgeMap(Map)}</li>
 * <li>A {@link Factory} creating new {@link Set} instances for {@link Edge}s -
 * can be provided via {@link #setEdgeSetFactory(Factory)}</li>
 * <li>An {@link EdgeFactory} for creating new {@link Edge} instances - can be
 * provided via {@link #setEdgeFactory(EdgeFactory)}</li>
 * </ol>
 * If nothing else is provided by the client, {@link MapGraphBuilder} uses by
 * default a {@link HashMap} for {@code 1.} and a {@link Factory} creating
 * {@link HashSet}s for {@code 2.} - for {@code 3.} the client <b>must</b>
 * provide an instance.
 * <p>
 * Additionally, it can be configured, if the generated {@link MapGraph} runs in
 * directed or non-directed mode (see {@link MapGraph#isDirected()}) using
 * {@link #setDirected(boolean)}.
 * <p>
 * <b>Attention:</b> this class is not thread-safe
 * 
 * @param <VertexType>
 *           the type of vertices that the built {@link Graph}s can carry
 * @param <EdgeType>
 *           the type of {@link Edge}s that the built {@link Graph}s can carry
 */
public class MapGraphBuilder<VertexType, EdgeType extends Edge<VertexType, EdgeType>>
{
   protected boolean _directed = false;
   protected Map<VertexType, Set<EdgeType>> _edgeMap = new HashMap<VertexType, Set<EdgeType>>();
   protected Factory<? extends Set<EdgeType>> _edgeSetFactory = new HashSetFactory<EdgeType>();
   protected EdgeFactory<VertexType, EdgeType> _edgeFactory;

   /**
    * Creates a new {@link MapGraphBuilder} using the {@link #MapGraphBuilder()
    * default constructor} and applies a {@link DefaultEdgeFactory} for
    * {@link #setEdgeFactory(EdgeFactory)}.
    * 
    * @return the new {@link MapGraphBuilder}
    */
   public static <VertexType> MapGraphBuilder<VertexType, DefaultEdge<VertexType>> withDefaultEdgeFactory()
   {
      return new MapGraphBuilder<VertexType, DefaultEdge<VertexType>>()
            .setEdgeFactory(new DefaultEdgeFactory<VertexType>());
   }

   /**
    * Creates a new {@link MapGraphBuilder} using the {@link #MapGraphBuilder()
    * default constructor} and applies a {@link DefaultValuedEdgeFactory} for
    * {@link #setEdgeFactory(EdgeFactory)}.
    * 
    * @return the new {@link MapGraphBuilder}
    */
   public static <VertexType, ValueType> MapGraphBuilder<VertexType, DefaultValuedEdge<VertexType, ValueType>> withDefaultValuedEdgeFactory()
   {
      return new MapGraphBuilder<VertexType, DefaultValuedEdge<VertexType, ValueType>>()
            .setEdgeFactory(new DefaultValuedEdgeFactory<VertexType, ValueType>());
   }

   /**
    * Creates a new instance with a internal {@link HashMap}, a {@link HashSet}
    * creating {@link Factory} and for non-directed mode.
    */
   public MapGraphBuilder()
   {}

   /**
    * Builds the final {@link MapGraph} using the internal {@link Map},
    * {@link Set} for {@link Edge}s and {@link EdgeFactory}.
    * <p>
    * <b>Attention:</b> this method can be used multiple times on the same
    * instance for creating many {@link MapGraph}s, but between calls, a new
    * internal {@link Map} must be provided via {@link #setEdgeMap(Map)}.
    * 
    * @return the resulting {@link MapGraph}
    * @throws IllegalStateException
    *            if one of the necessary internal objects is (still)
    *            {@code null}
    */
   public MapGraph<VertexType, EdgeType> build()
   {
      if (_edgeFactory == null)
      {
         throw new IllegalStateException("No EdgeFactory set!");
      }

      if (_edgeMap == null)
      {
         throw new IllegalStateException("No Edge-Map set!");
      }

      if (_edgeSetFactory == null)
      {
         throw new IllegalStateException("No Edge-Set-Factory set!");
      }

      final Map<VertexType, Set<EdgeType>> edgeMap = _edgeMap;
      _edgeMap = null;

      return new MapGraph<VertexType, EdgeType>(edgeMap, _edgeSetFactory, _edgeFactory, _directed);
   }

   /**
    * Returns if generated {@link MapGraph}s will run in directed or
    * non-directed mode.
    * 
    * @return if generated {@link MapGraph}s will run in directed or
    *         non-directed mode
    * @see MapGraph#isDirected()
    */
   public boolean isDirected()
   {
      return _directed;
   }

   /**
    * Returns the {@link Map} that the generated {@link MapGraph}s will
    * internally use.
    * 
    * @return the {@link Map} that the generated {@link MapGraph}s will
    *         internally use
    */
   public Map<VertexType, Set<EdgeType>> getEdgeMap()
   {
      return _edgeMap;
   }

   /**
    * Returns the {@link Factory} for {@link Edge}-{@link Set}s that the
    * generated {@link MapGraph}s will internally use.
    * 
    * @return the {@link Factory} for {@link Edge}-{@link Set}s that the
    *         generated {@link MapGraph}s will internally use
    */
   public Factory<? extends Set<EdgeType>> getEdgeSetFactory()
   {
      return _edgeSetFactory;
   }

   /**
    * Returns the {@link EdgeFactory} that the generated {@link MapGraph}s will
    * internally use.
    * 
    * @return the {@link EdgeFactory} that the generated {@link MapGraph}s will
    *         internally use
    */
   public EdgeFactory<VertexType, EdgeType> getEdgeFactory()
   {
      return _edgeFactory;
   }

   /**
    * Sets if generated {@link MapGraph}s will run in directed or non-directed
    * mode.
    * 
    * @param directed
    *           if generated {@link MapGraph}s will run in directed or
    *           non-directed mode
    * @return the {@link MapGraphBuilder} itself
    * @see MapGraph#isDirected()
    */
   public MapGraphBuilder<VertexType, EdgeType> setDirected(final boolean directed)
   {
      _directed = directed;

      return this;
   }

   /**
    * Sets the {@link Map} that the next generated {@link MapGraph} will
    * internally use.
    * 
    * @param edgeMap
    *           the {@link Map} that the next generated {@link MapGraph} will
    *           internally use
    * @return the {@link MapGraphBuilder} itself
    */
   public MapGraphBuilder<VertexType, EdgeType> setEdgeMap(final Map<VertexType, Set<EdgeType>> edgeMap)
   {
      _edgeMap = edgeMap;

      return this;
   }

   /**
    * Sets the {@link Factory} for {@link Edge}-{@link Set}s that generated
    * {@link MapGraph}s will internally use.
    * 
    * @param edgeSetFactory
    *           the {@link Factory} for {@link Edge}-{@link Set}s that generated
    *           {@link MapGraph}s will internally use
    * @return the {@link MapGraphBuilder} itself
    */
   public MapGraphBuilder<VertexType, EdgeType> setEdgeSetFactory(final Factory<? extends Set<EdgeType>> edgeSetFactory)
   {
      _edgeSetFactory = edgeSetFactory;

      return this;
   }

   /**
    * Sets the {@link EdgeFactory} that generated {@link MapGraph}s will
    * internally use.
    * 
    * @param edgeFactory
    *           the {@link EdgeFactory} that generated {@link MapGraph}s will
    *           internally use
    * @return the {@link MapGraphBuilder} itself
    */
   public MapGraphBuilder<VertexType, EdgeType> setEdgeFactory(final EdgeFactory<VertexType, EdgeType> edgeFactory)
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
