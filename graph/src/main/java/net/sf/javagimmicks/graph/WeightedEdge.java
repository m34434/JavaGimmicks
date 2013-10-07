package net.sf.javagimmicks.graph;

/**
 * A special version of {@link Edge} that is weighted (it has a cost).
 */
public interface WeightedEdge<VertexType, EdgeType extends WeightedEdge<VertexType, EdgeType>> extends
      Edge<VertexType, EdgeType>
{
   /**
    * Returns the weight or cost of this {@link WeightedEdge}.
    * 
    * @return the weight or cost of this {@link WeightedEdge}
    */
   public double getCost();

   /**
    * Sets the weight or cost of this {@link WeightedEdge}.
    * 
    * @param cost
    *           the cost to set
    */
   public void setCost(double cost);
}
