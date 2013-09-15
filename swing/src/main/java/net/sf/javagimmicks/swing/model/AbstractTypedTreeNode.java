package net.sf.javagimmicks.swing.model;

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public abstract class AbstractTypedTreeNode<Value> implements TypedTreeNode<Value>
{
    protected final Value _value;

    protected final boolean _allowsChildren;
    protected final boolean _noChildrenMeansLeaf; 
    
    protected AbstractTypedTreeNode(Value value, boolean allowsChildren, boolean noChildrenMeansLeaf)
    {
        _value = value;

        _allowsChildren = allowsChildren;
        _noChildrenMeansLeaf = noChildrenMeansLeaf;
    }
    
    protected AbstractTypedTreeNode(Value value, boolean allowsChildren)
    {
        this(value, allowsChildren, false);
    }
    
    protected AbstractTypedTreeNode(Value value)
    {
        this(value, false);
    }
    
    public TreeNode getParent()
    {
        return null;
    }

    public Value getValue()
    {
        return _value;
    }
    
    public boolean getAllowsChildren()
    {
        return _allowsChildren;
    }

    public boolean isLeaf()
    {
        return _noChildrenMeansLeaf ? !children().hasMoreElements() : !_allowsChildren;
    }
    
    public Enumeration<?> children()
    {
        return Collections.enumeration(Collections.emptySet());
    }
    
    public TreeNode getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public int getIndex(TreeNode node)
    {
        return -1;
    }
    
    public String toString()
    {
        return _value.toString();
    }
}
