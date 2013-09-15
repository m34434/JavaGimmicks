package net.sf.javagimmicks.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Graph<V, E extends Edge<V, E>>
{
   public Set<V> vertexSet();
   public Map<V, Set<E>> edgeMap();
   
   public boolean containsVertex(V vertex);
   public boolean addVertex(V vertex);
   public Set<E> removeVertex(V vertex);
   
   public Set<E> edgesOf(V source);
   public Set<V> targetsOf(V source);
   
   public boolean isConnected(V source, V target);
   public E getEdge(V source, V target);
   public Set<E> getEdges(V source, V target);
   
   public E addEdge(V source, V target);
   public Set<E> addEdges(V source, Collection<? extends V> targets);
   
   public E removeEdge(V source, V target);
   public Set<E> removeEdges(V source, V target);
   public Set<E> removeEdges(V source, Collection<? extends V> targets);
}
