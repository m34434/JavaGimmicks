package net.sf.javagimmicks.swing.model;

public interface TypedChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>> extends TypedTreeNode<Value>
{
    public ParentNode getParent();
    
    public ParentValue getParentValue();
}
