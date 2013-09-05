package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ListTreeModel<E> implements TreeModel
{
	protected final List<TreeModelListener> _listeners = new ArrayList<TreeModelListener>();
	protected final boolean _noChildrenMeansLeaf;
	
	protected ListTreeNode<E> _root;
	
	public ListTreeModel(boolean noChildrenMeansLeaf)
	{
		_noChildrenMeansLeaf = noChildrenMeansLeaf;
	}
	
	public ListTreeModel()
	{
		this(false);
	}
	
	public ListTreeNode<E> createRoot(E value)
	{
		if(_root != null)
		{
			throw new IllegalStateException("The model already has a root!");
		}
		
		_root = new ListTreeNode<E>(this, false, value);
		
		return _root;
	}
	
	public ListTreeNode<E> clear()
	{
		if(_root == null)
		{
			return null;
		}
		else
		{
			ListTreeNode<E> result = _root;
			
			_root.updateModel(null);
			
			return result;
		}
	}
	
	public Object getChild(Object parent, int index)
	{
		ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);
		
		return simpleTreeNode.getChildAt(index);
	}

	public int getChildCount(Object parent)
	{
		ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);
		
		return simpleTreeNode.getChildCount();
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);
		
		return simpleTreeNode.getChildListView().indexOf(child);
	}

	public Object getRoot()
	{
		return _root;
	}

	public boolean isLeaf(Object node)
	{
		ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(node);
		
		return _noChildrenMeansLeaf ? simpleTreeNode.getChildCount() == 0 : !simpleTreeNode.getAllowsChildren();
	}
	
	public void addTreeModelListener(TreeModelListener listener)
	{
		_listeners.add(listener);
	}

	public void removeTreeModelListener(TreeModelListener listener)
	{
		_listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		Object node = path.getLastPathComponent();
		ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(node);

		simpleTreeNode.setValue((E)newValue);
	}
	
	@SuppressWarnings("unchecked")
	public List<ListTreeNode<E>> getPathToRootList(ListTreeNode<E> node)
	{
		if(node == null || node._model != this)
		{
			return Collections.EMPTY_LIST;
		}

		LinkedList<ListTreeNode<E>> result = new LinkedList<ListTreeNode<E>>();
		
		while(node != null)
		{
			result.addFirst(node);
			node = node._parent;
		}
		
		return result;
	}
	
	public TreePath getPathToRoot(ListTreeNode<E> node)
	{
		return new TreePath(getPathToRootList(node).toArray());
	}
	
	protected void fireNodeChanged(ListTreeNode<E> parent, int index)
	{
		TreeModelEvent event = createTreeModelEvent(parent, index);
		
		for(TreeModelListener listener : _listeners)
		{
			listener.treeNodesChanged(event);
		}
	}
	
	protected void fireNodesInserted(ListTreeNode<E> parent, int fromIndex, Collection<? extends ListTreeNode<E>> nodes)
	{
		TreeModelEvent event = createTreeModelEvent(parent, fromIndex, nodes);

		for(TreeModelListener listener : _listeners)
		{
			listener.treeNodesInserted(event);
		}
	}
	
	protected void fireNodesRemoved(ListTreeNode<E> parent, int index, Collection<? extends ListTreeNode<E>> nodes)
	{
		TreeModelEvent event = createTreeModelEvent(parent, index, nodes);
		
		for(TreeModelListener listener : _listeners)
		{
			listener.treeNodesRemoved(event);
		}
	}
	
	protected TreeModelEvent createTreeModelEvent(ListTreeNode<E> parent, int index)
	{
		return new TreeModelEvent(this, getPathToRoot(parent), new int[]{index}, new Object[]{parent._children.get(index)}); 
	}
	
	protected TreeModelEvent createTreeModelEvent(ListTreeNode<E> parent, int fromIndex, Collection<? extends ListTreeNode<E>> nodes)
	{
		int[] indices = new int[nodes.size()];
		for(int i = 0; i < indices.length; ++i)
		{
			indices[i] = fromIndex + i;
		}
		
		return new TreeModelEvent(this, getPathToRoot(parent), indices, nodes.toArray()); 
	}

	@SuppressWarnings("unchecked")
	private ListTreeNode<E> checkAndConvertToNode(Object o)
	{
		if(!(o instanceof ListTreeNode))
		{
			throw new IllegalArgumentException("Provided node must be of type " + ListTreeNode.class.getName());
		}
		
		ListTreeNode<E> simpleTreeNode = (ListTreeNode<E>)o;
		
		if(simpleTreeNode._model != this)
		{
			throw new IllegalArgumentException("Provided node is not contained in this model");
		}
		
		return simpleTreeNode;
	}

}
