package net.sf.javagimmicks.swing.model;

/**
 * An special {@link TypedTreeNode} that represents a child node within a tree -
 * it has a parameterized parent of type {@link TypedParentTreeNode}.
 * 
 * @param <PV>
 *           the type of values that the parent {@link TypedParentTreeNode}
 *           carries
 * @param <P>
 *           the type of the parent {@link TypedParentTreeNode}
 */
public interface TypedChildTreeNode<V, PV, P extends TypedParentTreeNode<PV, ? super V, ? extends TypedChildTreeNode<?, ?, ?>>>
      extends TypedTreeNode<V>
{
   @Override
   public P getParent();

   /**
    * Returns the value of the parent {@link TypedParentTreeNode}.
    * 
    * @return the value of the parent {@link TypedParentTreeNode}
    */
   public PV getParentValue();
}
