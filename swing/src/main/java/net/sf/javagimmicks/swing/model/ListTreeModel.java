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

   public ListTreeModel(final boolean noChildrenMeansLeaf)
   {
      _noChildrenMeansLeaf = noChildrenMeansLeaf;
   }

   public ListTreeModel()
   {
      this(false);
   }

   public ListTreeNode<E> createRoot(final E value)
   {
      if (_root != null)
      {
         throw new IllegalStateException("The model already has a root!");
      }

      _root = new ListTreeNode<E>(this, false, value);

      return _root;
   }

   public ListTreeNode<E> clear()
   {
      if (_root == null)
      {
         return null;
      }
      else
      {
         final ListTreeNode<E> result = _root;

         _root.updateModel(null);

         return result;
      }
   }

   @Override
   public ListTreeNode<E> getChild(final Object parent, final int index)
   {
      final ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);

      return simpleTreeNode.getChildAt(index);
   }

   @Override
   public int getChildCount(final Object parent)
   {
      final ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);

      return simpleTreeNode.getChildCount();
   }

   @Override
   public int getIndexOfChild(final Object parent, final Object child)
   {
      final ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(parent);

      return simpleTreeNode.getChildListView().indexOf(child);
   }

   @Override
   public ListTreeNode<E> getRoot()
   {
      return _root;
   }

   @Override
   public boolean isLeaf(final Object node)
   {
      final ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(node);

      return _noChildrenMeansLeaf ? simpleTreeNode.getChildCount() == 0 : !simpleTreeNode.getAllowsChildren();
   }

   @Override
   public void addTreeModelListener(final TreeModelListener listener)
   {
      _listeners.add(listener);
   }

   @Override
   public void removeTreeModelListener(final TreeModelListener listener)
   {
      _listeners.remove(listener);
   }

   @Override
   @SuppressWarnings("unchecked")
   public void valueForPathChanged(final TreePath path, final Object newValue)
   {
      final Object node = path.getLastPathComponent();
      final ListTreeNode<E> simpleTreeNode = checkAndConvertToNode(node);

      simpleTreeNode.setValue((E) newValue);
   }

   @SuppressWarnings("unchecked")
   public List<ListTreeNode<E>> getPathListToRoot(ListTreeNode<E> node)
   {
      if (node == null || node._model != this)
      {
         return Collections.EMPTY_LIST;
      }

      final LinkedList<ListTreeNode<E>> result = new LinkedList<ListTreeNode<E>>();

      while (node != null)
      {
         result.addFirst(node);
         node = node._parent;
      }

      return result;
   }

   public TreePath getPathToRoot(final ListTreeNode<E> node)
   {
      return new TreePath(getPathListToRoot(node).toArray());
   }

   protected void fireNodeChanged(final ListTreeNode<E> parent, final int index)
   {
      final TreeModelEvent event = createTreeModelEvent(parent, index);

      for (final TreeModelListener listener : _listeners)
      {
         listener.treeNodesChanged(event);
      }
   }

   protected void fireNodesInserted(final ListTreeNode<E> parent, final int fromIndex,
         final Collection<? extends ListTreeNode<E>> nodes)
   {
      final TreeModelEvent event = createTreeModelEvent(parent, fromIndex, nodes);

      for (final TreeModelListener listener : _listeners)
      {
         listener.treeNodesInserted(event);
      }
   }

   protected void fireNodesRemoved(final ListTreeNode<E> parent, final int index,
         final Collection<? extends ListTreeNode<E>> nodes)
   {
      final TreeModelEvent event = createTreeModelEvent(parent, index, nodes);

      for (final TreeModelListener listener : _listeners)
      {
         listener.treeNodesRemoved(event);
      }
   }

   protected TreeModelEvent createTreeModelEvent(final ListTreeNode<E> parent, final int index)
   {
      return new TreeModelEvent(this, getPathToRoot(parent), new int[] { index },
            new Object[] { parent._children.get(index) });
   }

   protected TreeModelEvent createTreeModelEvent(final ListTreeNode<E> parent, final int fromIndex,
         final Collection<? extends ListTreeNode<E>> nodes)
   {
      final int[] indices = new int[nodes.size()];
      for (int i = 0; i < indices.length; ++i)
      {
         indices[i] = fromIndex + i;
      }

      return new TreeModelEvent(this, getPathToRoot(parent), indices, nodes.toArray());
   }

   @SuppressWarnings("unchecked")
   private ListTreeNode<E> checkAndConvertToNode(final Object o)
   {
      if (!(o instanceof ListTreeNode))
      {
         throw new IllegalArgumentException("Provided node must be of type " + ListTreeNode.class.getName());
      }

      final ListTreeNode<E> simpleTreeNode = (ListTreeNode<E>) o;

      if (simpleTreeNode._model != this)
      {
         throw new IllegalArgumentException("Provided node is not contained in this model");
      }

      return simpleTreeNode;
   }

}
