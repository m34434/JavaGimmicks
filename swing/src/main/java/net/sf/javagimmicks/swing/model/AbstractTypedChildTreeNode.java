package net.sf.javagimmicks.swing.model;

/**
 * An base implementation of {@link TypedChildTreeNode} extending
 * {@link AbstractTypedTreeNode} by parent support.
 */
public abstract class AbstractTypedChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>>
      extends AbstractTypedTreeNode<Value> implements TypedChildTreeNode<Value, ParentValue, ParentNode>
{
   protected final ParentNode _parentNode;

   protected AbstractTypedChildTreeNode(final Value value, final ParentNode parentNode, final boolean allowsChildren,
         final boolean noChildrenMeansLeaf)
   {
      super(value, allowsChildren, noChildrenMeansLeaf);
      _parentNode = parentNode;
   }

   protected AbstractTypedChildTreeNode(final Value value, final ParentNode parentNode, final boolean allowsChildren)
   {
      this(value, parentNode, allowsChildren, false);
   }

   protected AbstractTypedChildTreeNode(final Value value, final ParentNode parentNode)
   {
      this(value, parentNode, false);
   }

   @Override
   public ParentNode getParent()
   {
      return _parentNode;
   }

   @Override
   public ParentValue getParentValue()
   {
      return _parentNode.getValue();
   }
}
