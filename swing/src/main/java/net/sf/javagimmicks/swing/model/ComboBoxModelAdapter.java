package net.sf.javagimmicks.swing.model;

import java.util.List;

import javax.swing.ComboBoxModel;

import net.sf.javagimmicks.lang.LangUtils;

/**
 * A {@link ComboBoxModel} implementation that is also a {@link List}.
 */
public class ComboBoxModelAdapter<E> extends ListModelAdapter<E> implements ComboBoxModel
{
   private static final long serialVersionUID = -5100057877489621837L;

   protected Object _selectedItem;

   public ComboBoxModelAdapter()
   {
      super();
   }

   /**
    * Creates a new instance wrapping a given {@link List}.
    * 
    * @param internalList
    *           the {@link List} to use internally for element management
    */
   public ComboBoxModelAdapter(final List<E> internalList)
   {
      super(internalList);
   }

   @Override
   public Object getSelectedItem()
   {
      return _selectedItem;
   }

   @Override
   public void setSelectedItem(final Object item)
   {
      if (_selectedItem != null && !_selectedItem.equals(item) || _selectedItem == null && item != null)
      {
         _selectedItem = item;
         fireContentsChanged(this, -1, -1);
      }
   }

   @Override
   public ComboBoxModelAdapter<E> subList(final int from, final int to)
   {
      return new SubListDecorator<E>(this, from, to);
   }

   @Override
   public boolean equals(final Object o)
   {
      if (!(o instanceof ComboBoxModelAdapter<?>))
      {
         return false;
      }

      final ComboBoxModelAdapter<?> other = (ComboBoxModelAdapter<?>) o;

      return LangUtils.equalsNullSafe(this._selectedItem, other._selectedItem) && super.equals(o);
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + (1 << 15) + (_selectedItem == null ? 0 : _selectedItem.hashCode());
   }

   // Unfortunately I had to duplicate the code from ListModelListDecorator to
   // here
   // in order to keep the type ComboBoxModelListDecorator also for the sub
   // list.
   // Maybe there is a much better way. If you know one, please send me your
   // proposal.
   protected static class SubListDecorator<E> extends ComboBoxModelAdapter<E>
   {
      private static final long serialVersionUID = -5301211360041031237L;

      protected final int m_iOffset;
      protected final ComboBoxModelAdapter<E> m_oParent;

      protected SubListDecorator(final ComboBoxModelAdapter<E> oParent, final int iFromIndex, final int iToIndex)
      {
         super(oParent._internalList.subList(iFromIndex, iToIndex));

         m_oParent = oParent;
         m_iOffset = iFromIndex;
      }

      @Override
      protected void fireContentsChanged(final Object source, final int index0, final int index1)
      {
         super.fireContentsChanged(source, index0, index1);

         if (source == this && index0 >= 0 && index1 >= 0)
         {
            m_oParent.fireContentsChanged(m_oParent, m_iOffset + index0, m_iOffset + index1);
         }
      }

      @Override
      protected void fireIntervalAdded(final Object source, final int index0, final int index1)
      {
         super.fireIntervalAdded(source, index0, index1);

         if (source == this)
         {
            m_oParent.fireIntervalAdded(m_oParent, m_iOffset + index0, m_iOffset + index1);
         }
      }

      @Override
      protected void fireIntervalRemoved(final Object source, final int index0, final int index1)
      {
         super.fireIntervalRemoved(source, index0, index1);

         if (source == this)
         {
            m_oParent.fireIntervalRemoved(m_oParent, m_iOffset + index0, m_iOffset + index1);
         }
      }
   }
}
