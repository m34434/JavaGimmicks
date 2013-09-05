package net.sf.javagimmicks.swing.model;

import java.util.List;

import javax.swing.ComboBoxModel;

public class ComboBoxModelAdapter<E> extends ListModelAdapter<E> implements ComboBoxModel
{
	private static final long serialVersionUID = -5100057877489621837L;

	protected Object m_oSelectedItem;

	public ComboBoxModelAdapter()
	{
		super();
	}

	public ComboBoxModelAdapter(List<E> oInternalList)
	{
		super(oInternalList);
	}
	
    public Object getSelectedItem()
    {
        return m_oSelectedItem;
    }

    public void setSelectedItem(Object oItem)
    {
        if(m_oSelectedItem != null && !m_oSelectedItem.equals(oItem) || m_oSelectedItem == null && oItem != null)
        {
            m_oSelectedItem = oItem;
            fireContentsChanged(this, -1, -1);
        }
    }
    
	@Override
	public ComboBoxModelAdapter<E> subList(int iFromIndex, int iToIndex)
	{
		return new SubListDecorator<E>(this, iFromIndex, iToIndex);
	}

	@Override
	public boolean equals(Object o)
	{
        if(!(o instanceof ComboBoxModelAdapter<?>))
        {
            return false;
        }

        ComboBoxModelAdapter<?> oOther = (ComboBoxModelAdapter<?>) o;

        boolean bSelectedEqual = (this.m_oSelectedItem == null && oOther.m_oSelectedItem == null)
        	|| this.m_oSelectedItem == null
        	|| oOther.m_oSelectedItem == null
        	|| (this.m_oSelectedItem.equals(oOther.m_oSelectedItem));
        
        return bSelectedEqual && super.equals(o);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() + (1 << 15) + (m_oSelectedItem == null ? 0 : m_oSelectedItem.hashCode());
	}

	// Unfortunately I had to duplicate the code from ListModelListDecorator to here
	// in order to keep the type ComboBoxModelListDecorator also for the sub list.
	// Maybe there is a much better way. If you know one, please send me your proposal.
    protected static class SubListDecorator<E> extends ComboBoxModelAdapter<E>
    {
		private static final long serialVersionUID = -5301211360041031237L;

		protected final int m_iOffset;
        protected final ComboBoxModelAdapter<E> m_oParent;
        
        protected SubListDecorator(ComboBoxModelAdapter<E> oParent, int iFromIndex, int iToIndex)
        {
            super(oParent._internalList.subList(iFromIndex, iToIndex));
            
            m_oParent = oParent;
            m_iOffset = iFromIndex;
        }

        protected void fireContentsChanged(Object source, int index0, int index1)
        {
            super.fireContentsChanged(source, index0, index1);
            
            if(source == this && index0 >= 0 && index1 >= 0)
            {
                m_oParent.fireContentsChanged(m_oParent, m_iOffset + index0, m_iOffset + index1);
            }
        }

        protected void fireIntervalAdded(Object source, int index0, int index1)
        {
            super.fireIntervalAdded(source, index0, index1);

            if(source == this)
            {
                m_oParent.fireIntervalAdded(m_oParent, m_iOffset + index0, m_iOffset + index1);
            }
        }

        protected void fireIntervalRemoved(Object source, int index0, int index1)
        {
            super.fireIntervalRemoved(source, index0, index1);

            if(source == this)
            {
                m_oParent.fireIntervalRemoved(m_oParent, m_iOffset + index0, m_iOffset + index1);
            }
        }
    }
}
