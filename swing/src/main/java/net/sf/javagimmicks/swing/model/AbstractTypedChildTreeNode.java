package net.sf.javagimmicks.swing.model;

/**
 * An base implementation of {@link TypedChildTreeNode} extending
 * {@link AbstractTypedTreeNode} by parent support.
 */
public abstract class AbstractTypedChildTreeNode<V, PV, P extends TypedParentTreeNode<PV, ? super V, ? extends TypedChildTreeNode<?, ?, ?>>>
      extends AbstractTypedTreeNode<V> implements TypedChildTreeNode<V, PV, P>
{
   protected final P _parentNode;

   protected AbstractTypedChildTreeNode(final V value, final P parentNode, final boolean allowsChildren,
         final boolean noChildrenMeansLeaf)
   {
      super(value, allowsChildren, noChildrenMeansLeaf);
      _parentNode = parentNode;
   }

   protected AbstractTypedChildTreeNode(final V value, final P parentNode, final boolean allowsChildren)
   {
      this(value, parentNode, allowsChildren, false);
   }

   protected AbstractTypedChildTreeNode(final V value, final P parentNode)
   {
      this(value, parentNode, false);
   }

   @Override
   public P getParent()
   {
      return _parentNode;
   }

   @Override
   public PV getParentValue()
   {
      return _parentNode.getValue();
   }
}
