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

/**
 * A {@link TreeModel} implementation that uses {@link ListTreeNode}s as
 * internal node implementation.
 * 
 * @param <E>
 *           the type of values that the internal {@link ListTreeNode}s can
 *           carry
 */
public class ListTreeModel<E> implements TreeModel
{
   protected final List<TreeModelListener> _listeners = new ArrayList<TreeModelListener>();
   protected final boolean _noChildrenMeansLeaf;

   protected ListTreeNode<E> _root;

   /**
    * Creates a new instance with the given setting for leaf interpretation (see
    * {@link #isLeaf(Object)}).
    * 
    * @param noChildrenMeansLeaf
    *           if a node is always considered a leaf if it has no children or
    *           only then when it is in {@link ListTreeNode#isDedicatedLeaf()
    *           dedicated leaf mode}
    * @see #isLeaf(Object)
    * @see ListTreeNode#isDedicatedLeaf()
    */
   public ListTreeModel(final boolean noChildrenMeansLeaf)
   {
      _noChildrenMeansLeaf = noChildrenMeansLeaf;
   }

   /**
    * Creates a new instance that only considers child {@link ListTreeNode}s as
    * a leaf if they are in {@link ListTreeNode#isDedicatedLeaf() dedicated leaf
    * mode}.
    * 
    * @see #isLeaf(Object)
    * @see ListTreeNode#isDedicatedLeaf()
    */
   public ListTreeModel()
   {
      this(false);
   }

   /**
    * Creates and returns the initial root {@link ListTreeNode} for the given
    * value. The returns instances can from then easily be used to build or
    * maintain the whole {@link ListTreeModel}.
    * 
    * @param value
    *           the value to set within the generated {@link ListTreeNode}
    * @return the new root {@link ListTreeNode}
    * @throws IllegalStateException
    *            if the current instance has already a root
    */
   public ListTreeNode<E> createRoot(final E value) throws IllegalStateException
   {
      if (_root != null)
      {
         throw new IllegalStateException("The model already has a root!");
      }

      _root = new ListTreeNode<E>(this, false, value);

      return _root;
   }

   /**
    * Removes the internal root {@link ListTreeNode} (if existing) and returns
    * it. The removed root {@link ListTreeNode} and all it's children will also
    * be recursively unlinked from this {@link ListTreeModel}.
    * 
    * @return the removed root {@link ListTreeNode} or {@code null} if there was
    *         none
    */
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
         _root = null;

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

      return simpleTreeNode.getChildList().indexOf(child);
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

      return _noChildrenMeansLeaf ? simpleTreeNode.isLeaf() : simpleTreeNode.isDedicatedLeaf();
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

   /**
    * Builds a {@link List} of {@link ListTreeNode}s that represents the path
    * within the current {@link ListTreeModel} from the root
    * {@link ListTreeNode} to the given {@link ListTreeNode}.
    * <p>
    * The returned {@link List} will contain the root {@link ListTreeNode} as
    * the first element and the given one as the last element.
    * 
    * @param node
    *           the {@link ListTreeNode} to which to find a path
    * @return the resulting {@link List} representing the path or an empty one
    *         if the given node was {@code null} or does not belong to this
    *         {@link ListTreeModel}
    */
   public List<ListTreeNode<E>> getPathListToRoot(ListTreeNode<E> node)
   {
      if (node == null || node._model != this)
      {
         return Collections.emptyList();
      }

      final LinkedList<ListTreeNode<E>> result = new LinkedList<ListTreeNode<E>>();

      while (node != null)
      {
         result.addFirst(node);
         node = node._parent;
      }

      return result;
   }

   /**
    * An alternative to {@link #getPathListToRoot(ListTreeNode)} which returns
    * the resulting path as a {@link TreePath} object.
    * 
    * @param node
    *           the {@link ListTreeNode} to which to find a path
    * @return the resulting {@link TreePath} representing the path - will be
    *         empty if the given node was {@code null} or does not belong to
    *         this {@link ListTreeModel}
    */
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
