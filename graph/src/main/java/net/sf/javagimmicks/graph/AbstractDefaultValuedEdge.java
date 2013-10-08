package net.sf.javagimmicks.graph;

abstract class AbstractDefaultValuedEdge<VertexType, O, EdgeType extends AbstractDefaultValuedEdge<VertexType, O, EdgeType>> extends
      AbstractDefaultEdge<VertexType, EdgeType> implements ValuedEdge<VertexType, O, EdgeType>
{
   protected O _value;

   AbstractDefaultValuedEdge(final Graph<VertexType, ? extends EdgeType> graph, final VertexType source, final VertexType target, final O value)
   {
      super(graph, source, target);

      setValue(value);
   }

   @Override
   public O getValue()
   {
      return _value;
   }

   public void setValue(final O value)
   {
      _value = value;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      if (_value != null)
      {
         result.append(": ").append(_value);
      }

      return result.toString();
   }
}