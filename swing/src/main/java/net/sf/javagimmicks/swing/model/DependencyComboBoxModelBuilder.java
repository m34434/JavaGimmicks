package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import net.sf.javagimmicks.swing.model.DependencyComboBoxModel.CompositeKey;

/**
 * A fluent API builder for creating a hierarchy of inter-depending
 * {@link DependencyComboBoxModel}s.
 * <p>
 * The fluent API is very simple: {@link #add(Object)} adds an element to the
 * currently built {@link ComboBoxModel}, {@link #children()} internally
 * switches down one level and allows to specify elements for a depending
 * {@link ComboBoxModel} (and so on) until a call to {@link #parent()} which
 * comes back to the original level and allows to specify more elements there.
 * The resulting {@link List} of {@link ComboBoxModel}s can finally be retrieved
 * via {@link #buildModels()}.
 * <p>
 * For a better understanding, please have a look at this example from the test
 * code of this class:
 * 
 * <pre>
 * // @formatter:off
 * new DependencyComboBoxModelBuilder()
 *    .add(&quot;A&quot;).children()
 *       .add(&quot;A1&quot;).children()
 *          .add(&quot;A1x&quot;)
 *          .add(&quot;A1y&quot;)
 *          .add(&quot;A1z&quot;).parent()
 *       .add(&quot;A2&quot;).children()
 *          .add(&quot;A2x&quot;)
 *          .add(&quot;A2y&quot;)
 *          .add(&quot;A2z&quot;).parent()
 *       .add(&quot;A3&quot;).children()
 *          .add(&quot;A3x&quot;)
 *          .add(&quot;A3y&quot;)
 *          .add(&quot;A3z&quot;).parent()
 *       .add(&quot;A4&quot;).children()
 *          .add(&quot;A4x&quot;)
 *          .add(&quot;A4y&quot;)
 *          .add(&quot;A4z&quot;).parent().parent()
 *          
 *    .add(&quot;B&quot;).children()
 *       .add(&quot;B1&quot;).children()
 *          .add(&quot;B1x&quot;)
 *          .add(&quot;B1y&quot;)
 *          .add(&quot;B1z&quot;).parent()
 *       .add(&quot;B2&quot;).children()
 *          .add(&quot;B2x&quot;)
 *          .add(&quot;B2y&quot;)
 *          .add(&quot;B2z&quot;).parent().parent()
 *    
 *    .add(&quot;C&quot;).children()
 *       .add(&quot;C1&quot;).children()
 *          .add(&quot;C1x&quot;)
 *          .add(&quot;C1y&quot;)
 *          .add(&quot;C1z&quot;).parent()
 *       .add(&quot;C2&quot;).children()
 *          .add(&quot;C2x&quot;)
 *          .add(&quot;C2y&quot;)
 *          .add(&quot;C2z&quot;).parent()
 *       .add(&quot;C3&quot;).children()
 *          .add(&quot;C3x&quot;)
 *          .add(&quot;C3y&quot;)
 *          .add(&quot;C3z&quot;).parent().parent()
 *    
 *    .buildModels();
 * // @formatter:on
 * </pre>
 */
public class DependencyComboBoxModelBuilder
{
   private Map<CompositeKey, Vector<Object>> _modelData = new HashMap<CompositeKey, Vector<Object>>();
   private Stack<Object> _parentsStack = new Stack<Object>();
   private Object _lastAdded;
   private List<Object> _currentData = new ArrayList<Object>();

   /**
    * Adds a new item to the currently built {@link ComboBoxModel}.
    * 
    * @param item
    *           the item to add
    * @return this instance
    */
   public DependencyComboBoxModelBuilder add(final Object item)
   {
      _currentData.add(item);
      _lastAdded = item;

      return this;
   }

   /**
    * Switches down one level to {@link #add(Object) add} elements for the
    * next-level {@link DependencyComboBoxModel} which will depend on the last
    * {@link #add(Object) added} values up all levels.
    * 
    * @return this instance but switched down one level
    * @throws IllegalStateException
    *            if no values were yet {@link #add(Object) added} on the current
    *            level
    */
   public DependencyComboBoxModelBuilder children()
   {
      if (_lastAdded == null)
      {
         throw new IllegalStateException("No objects added yet!");
      }

      addCurrentElements();

      // Go up one level
      _parentsStack.push(_lastAdded);
      _lastAdded = null;

      return this;
   }

   /**
    * Finishes collecting contents for the current internal
    * {@link DependencyComboBoxModel} and goes back up one level in the
    * hierarchy.
    * 
    * @return the current instance but switched up one level
    * @throws IllegalStateException
    *            if the builder is currently on the top level
    */
   public DependencyComboBoxModelBuilder parent()
   {
      if (_parentsStack.isEmpty())
      {
         throw new IllegalStateException("Already on the top level!");
      }

      addCurrentElements();

      _lastAdded = _parentsStack.pop();

      return this;
   }

   /**
    * Returns a {@link List} containing a {@link ComboBoxModel} for each level
    * that was modeled so far on this instance.
    * <p>
    * The first {@link ComboBoxModel} in the result will always be a normal
    * {@link DefaultComboBoxModel} - all other ones will be respective
    * {@link DependencyComboBoxModel}s having all theirs predecessors as parent.
    * 
    * @return the resulting {@link List} of {@link ComboBoxModel}s
    */
   public List<ComboBoxModel> buildModels()
   {
      addCurrentElements();

      // Build a list which contains the list of model data entries per key size
      final List<List<Entry<CompositeKey, Vector<Object>>>> dataByLevel = new ArrayList<List<Entry<CompositeKey, Vector<Object>>>>();

      for (final Entry<CompositeKey, Vector<Object>> entry : _modelData.entrySet())
      {
         // Get the key size and that of the result list
         final int keySize = entry.getKey().getSize();
         final int dataLevelCount = dataByLevel.size();

         // If the key size is bigger or equal, we must enlarge
         // the result list by the missing number of levels
         if (keySize >= dataLevelCount)
         {
            for (int i = 0; i <= (keySize - dataLevelCount); ++i)
            {
               dataByLevel.add(new ArrayList<Entry<CompositeKey, Vector<Object>>>());
            }
         }

         // Now, it's safe to call get(iKeySize) and add the entry
         dataByLevel.get(keySize).add(entry);
      }

      // Now create the models level by level
      final List<ComboBoxModel> result = new ArrayList<ComboBoxModel>(dataByLevel.size());
      for (final List<Entry<CompositeKey, Vector<Object>>> levelData : dataByLevel)
      {
         ComboBoxModel model;

         // On the first level, we just create a DefaultComboBoxModel
         if (result.isEmpty())
         {
            assert levelData.size() == 1;

            model = new DefaultComboBoxModel(levelData.get(0).getValue());
         }

         // On each subsequent level, we must create a DependencyComboBoxModel
         // and fill it with all model data from this level
         else
         {
            // The result List automatically contains all parent model in the
            // right order
            final DependencyComboBoxModel depModel = new DependencyComboBoxModel(result);

            // Loop over the level entries and register them in the model
            for (final Entry<CompositeKey, Vector<Object>> entry : levelData)
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
      if (_currentData.isEmpty())
      {
         return;
      }

      // Add the objects added on this level to the
      // model data map under the right composite key
      final CompositeKey key = new CompositeKey(_parentsStack.toArray());
      Vector<Object> data = _modelData.get(key);

      if (data == null)
      {
         data = new Vector<Object>();
         _modelData.put(key, data);
      }

      data.addAll(_currentData);
      _currentData.clear();
   }
}
