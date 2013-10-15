package net.sf.javagimmicks.swing.model;

/**
 * A combined abstract basic implementation of {@link TypedChildTreeNode} and
 * {@link TypedParentTreeNode} suitable as any <i>intermediate</i> node within a
 * tree.
 */
public abstract class AbstractTypedParentChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>, ChildValue, ChildNode extends TypedChildTreeNode<? extends ChildValue, Value, ? extends TypedParentTreeNode<?, ?, ?>>>
      extends AbstractTypedParentTreeNode<Value, ChildValue, ChildNode> implements
      TypedChildTreeNode<Value, ParentValue, ParentNode>, TypedParentTreeNode<Value, ChildValue, ChildNode>
{
   protected final ParentNode _parentNode;

   protected AbstractTypedParentChildTreeNode(final Value value, final ParentNode parentNode,
         final boolean noChildrenMeansLeaf)
   {
      super(value, noChildrenMeansLeaf);

      _parentNode = parentNode;
   }

   protected AbstractTypedParentChildTreeNode(final Value value, final ParentNode parentNode)
   {
      super(value);

      _parentNode = parentNode;
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
