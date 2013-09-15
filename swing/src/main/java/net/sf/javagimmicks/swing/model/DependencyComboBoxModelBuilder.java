package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import net.sf.javagimmicks.swing.model.DependencyComboBoxModel.CompositeKey;

public class DependencyComboBoxModelBuilder
{
    private Map<CompositeKey, Vector<Object>> _modelData = new HashMap<CompositeKey, Vector<Object>>();
    private Stack<Object> _parentsStack = new Stack<Object>();
    private Object _lastAdded;
    private List<Object> _currentData = new ArrayList<Object>();
    
    public DependencyComboBoxModelBuilder add(Object item)
    {
        _currentData.add(item);
        _lastAdded = item;
        
        return this;
    }
    
    public DependencyComboBoxModelBuilder children()
    {
        if(_lastAdded == null)
        {
            throw new IllegalStateException("No objects added yet!");
        }
        
        addCurrentElements();

        // Go up one level
        _parentsStack.push(_lastAdded);
        _lastAdded = null;
        
        return this;
    }
    
    public DependencyComboBoxModelBuilder parent()
    {
        if(_parentsStack.isEmpty())
        {
            throw new IllegalStateException("Already on the top level!");
        }
        
        addCurrentElements();
        
        _lastAdded = _parentsStack.pop();
        
        return this;
    }
    
    public List<ComboBoxModel> buildModels()
    {
        addCurrentElements();
        
        // Build a list which contains the list of model data entries per key size
        List<List<Entry<CompositeKey, Vector<Object>>>> dataByLevel = new ArrayList<List<Entry<CompositeKey, Vector<Object>>>>();
                
        for(Entry<CompositeKey, Vector<Object>> entry : _modelData.entrySet())
        {
            // Get the key size and that of the result list
            int keySize = entry.getKey().getSize();
            int dataLevelCount = dataByLevel.size();
            
            // If the key size is bigger or equal, we must enlarge
            // the result list by the missing number of levels
            if(keySize >= dataLevelCount)
            {
                for(int i = 0; i <= (keySize - dataLevelCount); ++i)
                {
                    dataByLevel.add(new ArrayList<Entry<CompositeKey, Vector<Object>>>());
                }
            }
            
            // Now, it's safe to call get(iKeySize) and add the entry
            dataByLevel.get(keySize).add(entry);
        }

        // Now create the models level by level
        List<ComboBoxModel> result = new ArrayList<ComboBoxModel>(dataByLevel.size());
        for(List<Entry<CompositeKey, Vector<Object>>> levelData : dataByLevel)
        {
            ComboBoxModel model;

            // On the first level, we just create a DefaultComboBoxModel 
            if(result.isEmpty())
            {
                assert levelData.size() == 1;
                
                model = new DefaultComboBoxModel(levelData.get(0).getValue());
            }
            
            // On each subsequent level, we must create a DependencyComboBoxModel
            // and fill it with all model data from this level
            else
            {
                // The result List automatically contains all parent model in the right order
                DependencyComboBoxModel depModel = new DependencyComboBoxModel(result);
                
                // Loop over the level entries and register them in the model
                for(Entry<CompositeKey, Vector<Object>> entry : levelData)
                {
                    depModel.registerModel(entry.getKey(), entry.getValue());
                }
                
                model = depModel;
            }
            
            // Add the final model to the result list
            result.add(model);
        }

        return result;
    }
    
    private void addCurrentElements()
    {
        if(_currentData.isEmpty())
        {
            return;
        }
        
        // Add the objects added on this level to the
        // model data map under the right composite key
        CompositeKey key = new CompositeKey(_parentsStack.toArray());
        Vector<Object> data = _modelData.get(key);
        
        if(data == null)
        {
            data = new Vector<Object>();
            _modelData.put(key, data);
        }
        
        data.addAll(_currentData);
        _currentData.clear();
    }
}
