package net.sf.javagimmicks.swing.model;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.transform.Transformer;

/**
 * An abstract base implementation of {@link TypedParentTreeNode} that provides
 * some basics for child handling.
 */
public abstract class AbstractTypedParentTreeNode<V, CV, C extends TypedChildTreeNode<? extends CV, V, ? extends TypedParentTreeNode<?, ?, ?>>>
      extends AbstractTypedTreeNode<V> implements TypedParentTreeNode<V, CV, C>
{
   protected AbstractTypedParentTreeNode(final V value, final boolean noChildrenMeansLeaf)
   {
      super(value, true, noChildrenMeansLeaf);
   }

   protected AbstractTypedParentTreeNode(final V value)
   {
      super(value, true);
   }

   @Override
   public Enumeration<? extends C> children()
   {
      final List<? extends C> childNodesList = getChildNodes();
      return Collections.enumeration(childNodesList);
   }

   @Override
   public C getChildAt(final int childIndex)
   {
      return buildChildNode(getChildValues().get(childIndex));
   }

   @Override
   public int getChildCount()
   {
      return getChildValues().size();
   }

   @Override
   public int getIndex(final TreeNode node)
   {
      if (!(node instanceof TypedTreeNode<?>))
      {
         return -1;
      }

      final Object childValue = ((TypedTreeNode<?>) node).getValue();

      return getChildValues().indexOf(childValue);
   }

   protected List<C> getChildNodes()
   {
      final List<CV> childValues = getChildValues();

      if (childValues == null || childValues.isEmpty())
      {
         return Collections.emptyList();
      }

      return TransformerUtils.decorate(childValues, _valueToNodeTransformer);
   }

   abstract protected C buildChildNode(CV childValue);

   private final Transformer<CV, C> _valueToNodeTransformer = new Transformer<CV, C>()
   {
      @Override
      public C transform(final CV childValue)
      {
         return buildChildNode(childValue);
      }
   };
}
