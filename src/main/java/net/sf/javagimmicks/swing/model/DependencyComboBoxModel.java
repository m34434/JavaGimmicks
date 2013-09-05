package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class DependencyComboBoxModel extends AbstractListModel implements ComboBoxModel
{
    private static final long serialVersionUID = -2427435990181503056L;

    private final List<ComboBoxModel> _parentModels;
    private final Map<CompositeKey, ComboBoxList> _modelData = new HashMap<CompositeKey, ComboBoxList>();
    private final ListDataListener _listDataListener = new ListDataListener()
    {
        public void contentsChanged(ListDataEvent e)
        {
            if(e.getIndex0() == -1 && e.getIndex1() == -1)
            {
               update();
            }
        }

        public void intervalAdded(ListDataEvent e) {}
        public void intervalRemoved(ListDataEvent e) {}
    };
    
    private ComboBoxList _currentModelData;
    
    public DependencyComboBoxModel(List<ComboBoxModel> parentModels)
    {
        _parentModels = new ArrayList<ComboBoxModel>(parentModels);
        
        for(ComboBoxModel parentModel : _parentModels)
        {
            parentModel.addListDataListener(_listDataListener);
        }
        
        update();
    }
    
    public DependencyComboBoxModel(ComboBoxModel... parentModels)
    {
        this(Arrays.asList(parentModels));
    }
    
    public void registerModel(Object[] modelData, Object... parentValues)
    {
        registerModel(Arrays.asList(modelData), parentValues);
    }
    
    public void registerModel(List<?> modelData, Object... parentValues)
    {
        registerModel(new CompositeKey(parentValues), modelData);
    }
    
    public Object getSelectedItem()
    {
        return _currentModelData.getSelected();
    }
    
    void registerModel(CompositeKey key, List<?> modelData)
    {
        _modelData.put(key, new ComboBoxList(modelData));
        
        update();
    }

    public void setSelectedItem(Object anItem)
    {
        Object oSelectedItem = getSelectedItem();
        if((oSelectedItem != null && !oSelectedItem.equals(anItem)) || oSelectedItem == null && anItem != null)
        {
            _currentModelData.setSelected(anItem);
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getElementAt(int index)
    {
        return _currentModelData.getList().get(index);
    }

    public int getSize()
    {
        return _currentModelData.getList().size();
    }
    
    private void update()
    {
        Object[] parentValues = new Object[_parentModels.size()];
        for(ListIterator<ComboBoxModel> iterParentModels = _parentModels.listIterator(); iterParentModels.hasNext();)
        {
            parentValues[iterParentModels.nextIndex()] = iterParentModels.next().getSelectedItem();
        }

        int oldSize = _currentModelData == null ? 0 : getSize();
        
        ComboBoxList currentModelData = _modelData.get(new CompositeKey(parentValues));
        if(currentModelData == null)
        {
            _currentModelData = new ComboBoxList(Collections.EMPTY_LIST);
        }
        else
        {
            _currentModelData = currentModelData;
            if(_currentModelData.getSelected() == null && !_currentModelData.getList().isEmpty())
            {
                _currentModelData.setSelected(_currentModelData.getList().get(0));
            }
        }
        
        int newSize = getSize();
        
        fireContentsChanged(this, 0, Math.min(oldSize, newSize));
        if(newSize > oldSize)
        {
            fireIntervalAdded(this, oldSize, newSize);
        }
        else if(newSize < oldSize)
        {
            fireIntervalRemoved(this, newSize, oldSize);
        }
        
        fireContentsChanged(this, -1, -1);
    }
    
    private static class ComboBoxList
    {
        private Object _selectedItem;
        private List<?> _data;

        public ComboBoxList(List<?> data)
        {
            _data = data;
        }

        public Object getSelected()
        {
            return _selectedItem;
        }

        public void setSelected(Object item)
        {
            _selectedItem = item;
        }
        
        public List<?> getList()
        {
            return _data;
        }
    }

    static class CompositeKey
    {
        private Object[] _keys;
        
        public CompositeKey(Object... keys)
        {
            _keys = keys;
        }
        
        public int getSize()
        {
            return _keys.length;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj == this)
            {
                return true;
            }
            
            if(!(obj instanceof CompositeKey))
            {
                return false;
            }
            
            CompositeKey other = (CompositeKey)obj;
            
            return Arrays.equals(_keys, other._keys);
        }

        @Override
        public int hashCode()
        {
            int result = 0;
            for(Object keyPart : _keys)
            {
                result = result * 17 + keyPart.hashCode();
            }
            
            result += 748923479;
            
            return result;
        }
    }
}
