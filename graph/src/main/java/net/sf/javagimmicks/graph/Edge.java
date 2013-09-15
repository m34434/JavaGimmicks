package net.sf.javagimmicks.graph;

import java.util.Collection;

public interface Edge<V, E extends Edge<V, E>>
{
   public boolean connectsTo(V vertex);
   public Collection<V> getVerteces();
   public V getOutgoingVertex(V incoming);
   
   public Graph<V, ? extends E> getGraph();
   
   public String toString(V incoming); 
}
