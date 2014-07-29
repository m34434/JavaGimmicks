package net.sf.javagimmicks.beans;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A {@link Comparator} implementation that compares any bean-like objects by
 * comparing a specified set of their bean properties via reflection.
 * <p>
 * The set of properties (and optionally a sorting order for any of them) must
 * be specified in any of the constructors.
 * <p>
 * 
 * @param <T>
 *           the type of objects that may be compared by this comparator
 */
public class BeanPropertyComparator<T> implements Comparator<T>, Serializable
{
   private static final long serialVersionUID = 2201059572853849944L;

   /**
    * An enumeration for specifying the sorting order for the single properties.
    */
   public static enum SortOrder
   {
      ASCENDING, DESCENDING, NONE
   };

   protected List<String> _beanProperties;
   protected Map<String, SortOrder> _sortOrders = new HashMap<String, SortOrder>();

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties.
    * 
    * @param propertyNames
    *           the names of the properties to use for comparing.
    */
   public BeanPropertyComparator(final String... propertyNames)
   {
      this(Arrays.asList(propertyNames));
   }

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties.
    * 
    * @param propertyNames
    *           the names of the properties to use for comparing.
    */
   public BeanPropertyComparator(final List<String> propertyNames)
   {
      _beanProperties = new ArrayList<String>(propertyNames);
   }

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties using the specified sorting orders.
    * 
    * @param propertyNames
    *           the names of the properties to use for comparing.
    * @param sortOrders
    *           a {@link Map} associating a sorting order to any of the
    *           properties used for comparing
    */
   public BeanPropertyComparator(final List<String> propertyNames, final Map<String, SortOrder> sortOrders)
   {
      this(propertyNames);

      if (sortOrders != null)
      {
         _sortOrders.putAll(sortOrders);
      }
   }

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties using the specified sorting orders.
    * 
    * @param propertyNames
    *           the names of the properties to use for comparing.
    * @param sortOrders
    *           a {@link List} of sorting orders used for the specified property
    *           names; the association is done via the list index
    */
   public BeanPropertyComparator(final List<String> propertyNames, final List<SortOrder> sortOrders)
   {
      if (sortOrders.size() != propertyNames.size())
      {
         throw new IllegalArgumentException("Number of properties and sort orders must match!");
      }

      _beanProperties = new ArrayList<String>(propertyNames.size());

      final Iterator<String> iterPropertyNames = propertyNames.iterator();
      final Iterator<SortOrder> itersortOrders = sortOrders.iterator();

      while (iterPropertyNames.hasNext())
      {
         final String propertyName = iterPropertyNames.next();
         final SortOrder sortOrder = itersortOrders.next();

         _beanProperties.add(propertyName);
         _sortOrders.put(propertyName, sortOrder);
      }
   }

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified class
    * automatically extracting the bean property names used for comparing via
    * reflection.
    * 
    * @param beanClass
    *           the class to use for property extraction
    */
   public BeanPropertyComparator(final Class<? extends T> beanClass)
   {
      _beanProperties = BeanUtils.extractPropertyNames(beanClass);
   }

   /**
    * Creates a new {@link BeanPropertyComparator} using no properties for
    * comparing. New property names can be set afterwards via
    * {@link #setBeanPropertyNames(List)}.
    */
   public BeanPropertyComparator()
   {
      _beanProperties = new ArrayList<String>();
   }

   /**
    * Gets the {@link List} of the names of the properties used for comparing.
    * 
    * @return the {@link List} of the names of the properties used for comparing
    */
   public List<String> getBeanPropertyNames()
   {
      return Collections.unmodifiableList(_beanProperties);
   }

   /**
    * Sets the {@link List} of the names of the properties used for comparing.
    * 
    * @param beanProperties
    *           the names of the properties to be used for comparing
    */
   public void setBeanPropertyNames(final List<String> beanProperties)
   {
      _beanProperties = new ArrayList<String>(beanProperties);
   }

   /**
    * Gets the sorting order for the specified bean property as a
    * {@link SortOrder} object
    * 
    * @param propertyName
    *           the name of the property to get the sorting order for
    * @return the sorting order for the specified bean property as a
    *         {@link SortOrder} object
    */
   public SortOrder getSortOrder(final String propertyName)
   {
      if (!_beanProperties.contains(propertyName))
      {
         return SortOrder.NONE;
      }

      final SortOrder result = _sortOrders.get(propertyName);

      return result != null ? result : SortOrder.ASCENDING;
   }

   /**
    * Sets the sorting order for the specified bean property
    * 
    * @param propertyName
    *           the name of the property to set the sorting order for
    * @param sortOrder
    *           the new sorting order as {@link SortOrder} object
    */
   public void setSortOrder(final String propertyName, final SortOrder sortOrder)
   {
      if (sortOrder == SortOrder.NONE || sortOrder == null)
      {
         _sortOrders.remove(propertyName);
         _beanProperties.remove(propertyName);
      }
      else
      {
         if (!_beanProperties.contains(propertyName))
         {
            _beanProperties.add(propertyName);
         }

         _sortOrders.put(propertyName, sortOrder);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public int compare(final T o1, final T o2)
   {
      for (final String propertyName : _beanProperties)
      {
         Object propertyValue1;
         Object propertyValue2;
         try
         {
            propertyValue1 = invokeGetter(o1, propertyName);
            propertyValue2 = invokeGetter(o2, propertyName);
         }
         catch (final Exception ex)
         {
            if (ex instanceof RuntimeException)
            {
               throw (RuntimeException) ex;
            }
            else
            {
               throw new RuntimeException("Error invoking bean getter for property '" + propertyName + "'!", ex);
            }
         }

         @SuppressWarnings("rawtypes")
         int result = ((Comparable) propertyValue1).compareTo(propertyValue2);

         if (result != 0)
         {
            final SortOrder sortOrder = getSortOrder(propertyName);

            if (sortOrder == SortOrder.DESCENDING)
            {
               result *= -1;
            }

            return result;
         }
      }

      return 0;
   }

   protected Object invokeGetter(final Object target, final String propertyName) throws SecurityException,
         NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
      final Method m = target.getClass().getMethod("get" + propertyName);

      return m.invoke(target);
   }
}
