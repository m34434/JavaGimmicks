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

/**
 * A {@link ComboBoxModel} implementation that switches it's internal content
 * depending on the selected values in any given number of other
 * {@link ComboBoxModel}s.
 * <p>
 * To do so, the developer must register the different switching dependent
 * models using either {@link #registerModel(List, Object...)} or
 * {@link #registerModel(Object[], Object...)} before putting instances in
 * service.
 * <p>
 * For creating a hierarchy of inter-depending {@link DependencyComboBoxModel}s
 * please use {@link DependencyComboBoxModelBuilder}.
 */
public class DependencyComboBoxModel extends AbstractListModel implements ComboBoxModel
{
   private static final long serialVersionUID = -2427435990181503056L;

   private final List<ComboBoxModel> _parentModels;
   private final Map<CompositeKey, ComboBoxList> _modelData = new HashMap<CompositeKey, ComboBoxList>();
   private final ListDataListener _listDataListener = new ListDataListener()
   {
      @Override
      public void contentsChanged(final ListDataEvent e)
      {
         if (e.getIndex0() == -1 && e.getIndex1() == -1)
         {
            update();
         }
      }

      @Override
      public void intervalAdded(final ListDataEvent e)
      {}

      @Override
      public void intervalRemoved(final ListDataEvent e)
      {}
   };

   private ComboBoxList _currentModelData;

   /**
    * Creates a new instance that depends on the given {@link List} of other
    * {@link ComboBoxModel}s.
    * 
    * @param parentModels
    *           the {@link ComboBoxModel}s that this instance depends on
    */
   public DependencyComboBoxModel(final List<ComboBoxModel> parentModels)
   {
      _parentModels = new ArrayList<ComboBoxModel>(parentModels);

      for (final ComboBoxModel parentModel : _parentModels)
      {
         parentModel.addListDataListener(_listDataListener);
      }

      update();
   }

   /**
    * Creates a new instance that depends on the given list of other
    * {@link ComboBoxModel}s.
    * 
    * @param parentModels
    *           the {@link ComboBoxModel}s that this instance depends on
    */
   public DependencyComboBoxModel(final ComboBoxModel... parentModels)
   {
      this(Arrays.asList(parentModels));
   }

   /**
    * Registers a new switchable model content for the given combination of
    * values in the parent models.
    * <p>
    * The order of elements in the combination of parent values must match the
    * order of parent {@link ComboBoxModel}s as they were provided within
    * {@link #DependencyComboBoxModel(List)} or
    * {@link #DependencyComboBoxModel(ComboBoxModel...)}.
    * 
    * @param modelData
    *           the model contents to apply if the given combination of parent
    *           values is selected within the parent {@link ComboBoxModel}s
    * @param parentValues
    *           the combination of values that have to be selected within the
    *           parent {@link ComboBoxModel}s in order to apply the given
    *           elements as model content
    */
   public void registerModel(final Object[] modelData, final Object... parentValues)
   {
      registerModel(Arrays.asList(modelData), parentValues);
   }

   /**
    * Registers a new switchable model content for the given combination of
    * values in the parent models.
    * <p>
    * The order of elements in the combination of parent values must match the
    * order of parent {@link ComboBoxModel}s as they were provided within
    * {@link #DependencyComboBoxModel(List)} or
    * {@link #DependencyComboBoxModel(ComboBoxModel...)}.
    * 
    * @param modelData
    *           the model contents to apply if the given combination of parent
    *           values is selected within the parent {@link ComboBoxModel}s
    * @param parentValues
    *           the combination of values that have to be selected within the
    *           parent {@link ComboBoxModel}s in order to apply the given
    *           elements as model content
    */
   public void registerModel(final List<?> modelData, final Object... parentValues)
   {
      registerModel(new CompositeKey(parentValues), modelData);
   }

   @Override
   public Object getSelectedItem()
   {
      return _currentModelData.getSelected();
   }

   @Override
   public void setSelectedItem(final Object anItem)
   {
      final Object oSelectedItem = getSelectedItem();
      if ((oSelectedItem != null && !oSelectedItem.equals(anItem)) || oSelectedItem == null && anItem != null)
      {
         _currentModelData.setSelected(anItem);
         fireContentsChanged(this, -1, -1);
      }
   }

   @Override
   public Object getElementAt(final int index)
   {
      return _currentModelData.getList().get(index);
   }

   @Override
   public int getSize()
   {
      return _currentModelData.getList().size();
   }

   void registerModel(final CompositeKey key, final List<?> modelData)
   {
      _modelData.put(key, new ComboBoxList(modelData));

      update();
   }

   private void update()
   {
      final Object[] parentValues = new Object[_parentModels.size()];
      for (final ListIterator<ComboBoxModel> iterParentModels = _parentModels.listIterator(); iterParentModels
            .hasNext();)
      {
         parentValues[iterParentModels.nextIndex()] = iterParentModels.next().getSelectedItem();
      }

      final int oldSize = _currentModelData == null ? 0 : getSize();

      final ComboBoxList currentModelData = _modelData.get(new CompositeKey(parentValues));
      if (currentModelData == null)
      {
         _currentModelData = new ComboBoxList(Collections.EMPTY_LIST);
      }
      else
      {
         _currentModelData = currentModelData;
         if (_currentModelData.getSelected() == null && !_currentModelData.getList().isEmpty())
         {
            _currentModelData.setSelected(_currentModelData.getList().get(0));
         }
      }

      final int newSize = getSize();

      fireContentsChanged(this, 0, Math.min(oldSize, newSize));
      if (newSize > oldSize)
      {
         fireIntervalAdded(this, oldSize, newSize);
      }
      else if (newSize < oldSize)
      {
         fireIntervalRemoved(this, newSize, oldSize);
      }

      fireContentsChanged(this, -1, -1);
   }

   private static class ComboBoxList
   {
      private Object _selectedItem;
      private List<?> _data;

      public ComboBoxList(final List<?> data)
      {
         _data = data;
      }

      public Object getSelected()
      {
         return _selectedItem;
      }

      public void setSelected(final Object item)
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

      public CompositeKey(final Object... keys)
      {
         _keys = keys;
      }

      public int getSize()
      {
         return _keys.length;
      }

      @Override
      public boolean equals(final Object obj)
      {
         if (obj == this)
         {
            return true;
         }

         if (!(obj instanceof CompositeKey))
         {
            return false;
         }

         final CompositeKey other = (CompositeKey) obj;

         return Arrays.equals(_keys, other._keys);
      }

      @Override
      public int hashCode()
      {
         int result = 0;
         for (final Object keyPart : _keys)
         {
            result = result * 17 + keyPart.hashCode();
         }

         result += 748923479;

         return result;
      }
   }
}
