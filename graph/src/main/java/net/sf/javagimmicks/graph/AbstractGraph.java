package net.sf.javagimmicks.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections.AbstractMap2;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.lang.LangUtils;
import net.sf.javagimmicks.transform.Transformer;
import net.sf.javagimmicks.util.Factory;

/**
 * An abstract {@link Graph} implementation taking care about many basic
 * operations.
 */
public abstract class AbstractGraph<V, E extends Edge<V, E>> implements Graph<V, E>
{
   protected final Factory<? extends Set<E>> _edgeSetFactory;

   protected AbstractGraph(final Factory<? extends Set<E>> edgeSetFactory)
   {
      _edgeSetFactory = edgeSetFactory;
   }

   @Override
   public Map<V, Set<E>> edgeMap()
   {
      return new EdgeMap();
   }

   @Override
   public boolean addVertex(final V vertex)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public E addEdge(final V source, final V target)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public Set<E> addEdges(final V source, final Collection<? extends V> targets)
   {
      final Set<E> result = createEdgeSet();

      for (final V target : targets)
      {
         final E edge = addEdge(source, target);
         result.add(edge);
      }

      return result;
   }

   @Override
   public boolean containsVertex(final V vertex)
   {
      return vertexSet().contains(vertex);
   }

   @Override
   public Set<V> targetsOf(final V source)
   {
      return TransformerUtils.decorate(edgesOf(source), new EdgeToTargetTransformer<E, V>(source));
   }

   @Override
   public Set<E> getEdges(final V source, final V target)
   {
      final Set<E> result = createEdgeSet();
      for (final E edge : edgesOf(source))
      {
         final V currentTarget = edge.getOutgoingVertex(source);

         if (LangUtils.equalsNullSafe(target, currentTarget))
         {
            result.add(edge);
         }
      }

      return result;
   }

   @Override
   public E getEdge(final V source, final V target)
   {
      for (final E edge : edgesOf(source))
      {
         final V currentTarget = edge.getOutgoingVertex(source);

         if (LangUtils.equalsNullSafe(target, currentTarget))
         {
            return edge;
         }
      }

      return null;
   }

   @Override
   public boolean isConnected(final V source, final V target)
   {
      return getEdge(source, target) != null;
   }

   @Override
   public Set<E> removeEdges(final V source, final V target)
   {
      final Set<E> result = createEdgeSet();

      for (final Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();

         final V currentTarget = edge.getOutgoingVertex(source);

         if (LangUtils.equalsNullSafe(target, currentTarget))
         {
            edges.remove();

            result.add(edge);
         }
      }

      return result;
   }

   @Override
   public E removeEdge(final V source, final V target)
   {
      for (final Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();

         final V currentTarget = edge.getOutgoingVertex(source);

         if (target == null)
         {
            if (currentTarget == null)
            {
               edges.remove();
               return edge;
            }
         }
         else if (target.equals(currentTarget))
         {
            edges.remove();
            return edge;
         }
      }

      return null;
   }

   @Override
   public Set<E> removeEdges(final V source, final Collection<? extends V> targets)
   {
      final Set<E> result = createEdgeSet();

      for (final Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();

         if (targets.contains(edge.getOutgoingVertex(source)))
         {
            edges.remove();
            result.add(edge);
         }
      }

      return result;
   }

   @Override
   public Set<E> removeVertex(final V vertex)
   {
      return edgeMap().remove(vertex);
   }

   protected Set<E> createEdgeSet()
   {
      return _edgeSetFactory.create();
   }

   protected class EdgeMap extends AbstractMap2<V, Set<E>>
   {
      @Override
      public Set<V> keySet()
      {
         return vertexSet();
      }

      @Override
      protected Set<E> getValue(final V key)
      {
         return edgesOf(key);
      }
   }

   protected static class EdgeToTargetTransformer<E extends Edge<V, E>, V> implements Transformer<E, V>
   {
      protected final V _source;

      public EdgeToTargetTransformer(final V source)
      {
         _source = source;
      }

      @Override
      public V transform(final E edge)
      {
         return edge.getOutgoingVertex(_source);
      }
   }
}
