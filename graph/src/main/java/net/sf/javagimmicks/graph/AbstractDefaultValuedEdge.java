package net.sf.javagimmicks.graph;

abstract class AbstractDefaultValuedEdge<VertexType, ValueType, EdgeType extends AbstractDefaultValuedEdge<VertexType, ValueType, EdgeType>> extends
      AbstractDefaultEdge<VertexType, EdgeType> implements ValuedEdge<VertexType, ValueType, EdgeType>
{
   protected ValueType _value;

   AbstractDefaultValuedEdge(final Graph<VertexType, ? extends EdgeType> graph, final VertexType source, final VertexType target, final ValueType value)
   {
      super(graph, source, target);

      setValue(value);
   }

   @Override
   public ValueType getValue()
   {
      return _value;
   }

   public void setValue(final ValueType value)
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