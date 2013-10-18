package net.sf.javagimmicks.swing.model;

/**
 * A combined abstract basic implementation of {@link TypedChildTreeNode} and
 * {@link TypedParentTreeNode} suitable as any <i>intermediate</i> node within a
 * tree.
 */
public abstract class AbstractTypedParentChildTreeNode<V, PV, P extends TypedParentTreeNode<PV, ? super V, ? extends TypedChildTreeNode<?, ?, ?>>, CV, C extends TypedChildTreeNode<? extends CV, V, ? extends TypedParentTreeNode<?, ?, ?>>>
      extends AbstractTypedParentTreeNode<V, CV, C> implements
      TypedChildTreeNode<V, PV, P>, TypedParentTreeNode<V, CV, C>
{
   protected final P _parentNode;

   protected AbstractTypedParentChildTreeNode(final V value, final P parentNode,
         final boolean noChildrenMeansLeaf)
   {
      super(value, noChildrenMeansLeaf);

      _parentNode = parentNode;
   }

   protected AbstractTypedParentChildTreeNode(final V value, final P parentNode)
   {
      super(value);

      _parentNode = parentNode;
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
