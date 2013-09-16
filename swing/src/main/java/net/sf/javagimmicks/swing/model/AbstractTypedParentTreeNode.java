package net.sf.javagimmicks.swing.model;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.sf.javagimmicks.collections.transformer.Transformer;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;

public abstract class AbstractTypedParentTreeNode<Value, ChildValue, ChildNode extends TypedChildTreeNode<? extends ChildValue, Value, ? extends TypedParentTreeNode<?, ?, ?>>> extends AbstractTypedTreeNode<Value> implements TypedParentTreeNode<Value, ChildValue, ChildNode>
{
    protected AbstractTypedParentTreeNode(Value value, boolean noChildrenMeansLeaf)
    {
        super(value, true, noChildrenMeansLeaf);
    }

    protected AbstractTypedParentTreeNode(Value value)
    {
        super(value, true);
    }

    public Enumeration<? extends ChildNode> children()
    {
        final List<? extends ChildNode> childNodesList = getChildNodes();
        return Collections.enumeration(childNodesList);
    }

    public ChildNode getChildAt(int childIndex)
    {
        return buildChildNode(getChildValues().get(childIndex));
    }

    public int getChildCount()
    {
        return getChildValues().size();
    }

    public int getIndex(TreeNode node)
    {
        if(!(node instanceof TypedTreeNode<?>))
        {
            return -1;
        }
        
        final Object childValue = ((TypedTreeNode<?>)node).getValue();
        
        return getChildValues().indexOf(childValue);
    }

    protected List<ChildNode> getChildNodes()
    {
        final List<ChildValue> childValues = getChildValues();
        
        if(childValues == null || childValues.isEmpty())
        {
            return Collections.emptyList();
        }
        
        return TransformerUtils.decorate(childValues, new Transformer<ChildValue, ChildNode>()
        {
            public ChildNode transform(ChildValue childValue)
            {
                return buildChildNode(childValue);
            }
        });
    }

    abstract protected ChildNode buildChildNode(ChildValue childValue);
}
