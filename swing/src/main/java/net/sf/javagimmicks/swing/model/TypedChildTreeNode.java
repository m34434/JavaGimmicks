package net.sf.javagimmicks.swing.model;

/**
 * An special {@link TypedTreeNode} that represents a child node within a tree -
 * it has a parameterized parent of type {@link TypedParentTreeNode}.
 * 
 * @param <ParentValue>
 *           the type of values that the parent {@link TypedParentTreeNode}
 *           carries
 * @param <ParentNode>
 *           the type of the parent {@link TypedParentTreeNode}
 */
public interface TypedChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>>
      extends TypedTreeNode<Value>
{
   @Override
   public ParentNode getParent();

   /**
    * Returns the value of the parent {@link TypedParentTreeNode}.
    * 
    * @return the value of the parent {@link TypedParentTreeNode}
    */
   public ParentValue getParentValue();
}
