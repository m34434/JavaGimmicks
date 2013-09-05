package net.sf.javagimmicks.swing.model;

import java.util.Enumeration;
import java.util.List;

public interface TypedParentTreeNode<Value, ChildValue, ChildNode extends TypedChildTreeNode<? extends ChildValue, Value, ? extends TypedParentTreeNode<?, ?, ?>>> extends TypedTreeNode<Value> 
{
    public Enumeration<? extends ChildNode> children();
    public ChildNode getChildAt(int iChildIndex);
    
    public List<ChildValue> getChildValues();
}
