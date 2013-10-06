package net.sf.javagimmicks.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections.AbstractMap2;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.lang.Factory;
import net.sf.javagimmicks.lang.LangUtils;
import net.sf.javagimmicks.transform.Transformer;

public abstract class AbstractGraph<V, E extends Edge<V, E>> implements Graph<V, E> 
{
   protected final Factory<? extends Set<E>> _edgeSetFactory;
   
   protected AbstractGraph(Factory<? extends Set<E>> edgeSetFactory)
   {
      _edgeSetFactory = edgeSetFactory;
   }

   public Map<V, Set<E>> edgeMap()
   {
      return new EdgeMap();
   }

   public boolean addVertex(V vertex)
   {
      throw new UnsupportedOperationException();
   }

   public E addEdge(V source, V target)
   {
      throw new UnsupportedOperationException();
   }

   public Set<E> addEdges(V source, Collection<? extends V> targets)
   {
      final Set<E> result = createEdgeSet();
      
      for(V target : targets)
      {
         final E edge = addEdge(source, target);
         result.add(edge);
      }
      
      return result;
   }

   public boolean containsVertex(V vertex)
   {
      return vertexSet().contains(vertex);
   }

   public Set<V> targetsOf(V source)
   {
      return TransformerUtils.decorate(edgesOf(source), new EdgeToTargetTransformer<E, V>(source));
   }

   public Set<E> getEdges(V source, V target)
   {
      final Set<E> result = createEdgeSet();
      for(E edge : edgesOf(source))
      {
         final V currentTarget = edge.getOutgoingVertex(source);
         
         if(LangUtils.equalsNullSafe(target, currentTarget))
         {
            result.add(edge);
         }
      }
      
      return result;
   }

   public E getEdge(V source, V target)
   {
      for(E edge : edgesOf(source))
      {
         final V currentTarget = edge.getOutgoingVertex(source);
         
         if(LangUtils.equalsNullSafe(target, currentTarget))
         {
            return edge;
         }
      }
      
      return null;
   }
   
   public boolean isConnected(V source, V target)
   {
      return getEdge(source, target) != null;
   }

   public Set<E> removeEdges(V source, V target)
   {
      final Set<E> result = createEdgeSet();
      
      for(Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();
         
         final V currentTarget = edge.getOutgoingVertex(source);
         
         if(LangUtils.equalsNullSafe(target, currentTarget))
         {
            edges.remove();
            
            result.add(edge);
         }
      }
      
      return result;
   }

   public E removeEdge(V source, V target)
   {
      for(Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();
         
         final V currentTarget = edge.getOutgoingVertex(source);
         
         if(target == null)
         {
            if(currentTarget == null)
            {
               edges.remove();
               return edge;
            }
         }
         else if(target.equals(currentTarget))
         {
            edges.remove();
            return edge;
         }
      }
      
      return null;
   }

   public Set<E> removeEdges(V source, Collection<? extends V> targets)
   {
      final Set<E> result = createEdgeSet();
      
      for(Iterator<E> edges = edgesOf(source).iterator(); edges.hasNext();)
      {
         final E edge = edges.next();
         
         if(targets.contains(edge.getOutgoingVertex(source)))
         {
            edges.remove();
            result.add(edge);
         }
      }
      
      return result;
   }

   public Set<E> removeVertex(V vertex)
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
      protected Set<E> getValue(V key)
      {
         return edgesOf(key);
      }
   }

   protected static class EdgeToTargetTransformer<E extends Edge<V, E>, V> implements Transformer<E, V>
   {
      protected final V _source;

      public EdgeToTargetTransformer(V source)
      {
         _source = source;
      }

      public V transform(E edge)
      {
         return edge.getOutgoingVertex(_source);
      }
   }
}
