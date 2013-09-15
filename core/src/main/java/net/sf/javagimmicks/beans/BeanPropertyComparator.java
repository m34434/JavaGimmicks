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
 * A {@link Comparator} implementation that compares any bean-like objects
 * by comparing a specified set of their bean properties via reflection.
 * <p>
 * The set of properties (and optionally a sorting order for any of them)
 * must be specified in any of the constructors.
 * <p>
 * @param <T>  the type of objects that may be compared by this comparator
 */
public class BeanPropertyComparator<T> implements Comparator<T>, Serializable
{
   private static final long serialVersionUID = 2201059572853849944L;

   /**
    * An enumeration for specifying the sorting order for the single properties.
    */
   public static enum SortOrder {ASCENDING, DESCENDING, NONE};
	
	protected List<String> _beanProperties;
	protected Map<String, SortOrder> _sortOrders = new HashMap<String, SortOrder>();
	
	/**
	 * Creates a new {@link BeanPropertyComparator} for the specified bean
	 * properties.
	 * @param propertyNames the names of the properties to use for comparing. 
	 */
	public BeanPropertyComparator(String... propertyNames)
	{
		this(Arrays.asList(propertyNames));
	}

   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties.
    * @param propertyNames the names of the properties to use for comparing. 
    */
	public BeanPropertyComparator(List<String> propertyNames)
	{
		_beanProperties = new ArrayList<String>(propertyNames);
	}
	
   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties using the specified sorting orders.
    * @param propertyNames the names of the properties to use for comparing.
    * @param sortOrders a {@link Map} associating a sorting order to any of the properties
    *        used for comparing 
    */
	public BeanPropertyComparator(List<String> propertyNames, Map<String, SortOrder> sortOrders)
	{
		this(propertyNames);
		
		if(sortOrders != null)
		{
			_sortOrders.putAll(sortOrders);
		}
	}
	
   /**
    * Creates a new {@link BeanPropertyComparator} for the specified bean
    * properties using the specified sorting orders.
    * @param propertyNames the names of the properties to use for comparing.
    * @param sortOrders a {@link List} of sorting orders used for the specified property names;
    *        the association is done via the list index 
    */
	public BeanPropertyComparator(List<String> propertyNames, List<SortOrder> sortOrders)
	{
		if(sortOrders.size() != propertyNames.size())
		{
			throw new IllegalArgumentException("Number of properties and sort orders must match!");
		}
		
		_beanProperties = new ArrayList<String>(propertyNames.size());
		
		Iterator<String> iterPropertyNames = propertyNames.iterator();
		Iterator<SortOrder> itersortOrders = sortOrders.iterator();
		
		while(iterPropertyNames.hasNext())
		{
			String propertyName = iterPropertyNames.next();
			SortOrder sortOrder = itersortOrders.next();
			
			_beanProperties.add(propertyName);
			_sortOrders.put(propertyName, sortOrder);
		}
	}
	
	/**
    * Creates a new {@link BeanPropertyComparator} for the specified class
    * automatically extracting the bean property names used for comparing
    * via reflection.
	 * @param beanClass the class to use for property extraction
	 */
	public BeanPropertyComparator(Class<? extends T> beanClass)
	{
		_beanProperties = BeanUtils.extractPropertyNames(beanClass);
	}
	
   /**
    * Creates a new {@link BeanPropertyComparator} using no properties
    * for comparing. New property names can be set afterwards
    * via {@link #setBeanPropertyNames(List)}.
    */
	public BeanPropertyComparator()
	{
		_beanProperties = Collections.emptyList();
	}

	/**
	 * Gets the {@link List} of the names of the properties used for comparing.
	 * @return the {@link List} of the names of the properties used for comparing
	 */
	public List<String> getBeanPropertyNames()
	{
		return Collections.unmodifiableList(_beanProperties);
	}

   /**
    * Sets the {@link List} of the names of the properties used for comparing.
    */
	public void setBeanPropertyNames(List<String> beanProperties)
	{
		_beanProperties = new ArrayList<String>(beanProperties);
	}

	/**
	 * Gets the sorting order for the specified bean property as a {@link SortOrder} object
	 * @param propertyName the name of the property to get the sorting order for
	 * @return the sorting order for the specified bean property as a {@link SortOrder} object
	 */
	public SortOrder getSortOrder(String propertyName)
	{
		if(!_beanProperties.contains(propertyName))
		{
			return SortOrder.NONE;
		}
		
		SortOrder result = _sortOrders.get(propertyName);
		
		return result != null ? result : SortOrder.ASCENDING; 
	}
	
	/**
    * Sets the sorting order for the specified bean property
	 * @param propertyName the name of the property to set the sorting order for
	 * @param sortOrder the new sorting order as {@link SortOrder} object
	 */
	public void setSortOrder(String propertyName, SortOrder sortOrder)
	{
		if(sortOrder == SortOrder.NONE || sortOrder == null)
		{
			_sortOrders.remove(propertyName);
			_beanProperties.remove(propertyName);
		}
		else
		{
			if(!_beanProperties.contains(propertyName))
			{
				_beanProperties.add(propertyName);
			}
			
			_sortOrders.put(propertyName, sortOrder);
		}
	}

	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2)
	{
		for(String propertyName : _beanProperties)
		{
			Object propertyValue1;
			Object propertyValue2;
			try
			{
				propertyValue1 = invokeGetter(o1, propertyName);
				propertyValue2 = invokeGetter(o2, propertyName);
			}
			catch(Exception ex)
			{
				if(ex instanceof RuntimeException)
				{
					throw (RuntimeException)ex;
				}
				else
				{
					throw new RuntimeException("Error invoking bean getter for property '" +  propertyName + "'!", ex);
				}
			}
			
			@SuppressWarnings("rawtypes")
         int result = ((Comparable)propertyValue1).compareTo(propertyValue2);
			
			if(result != 0)
			{
				SortOrder sortOrder = getSortOrder(propertyName);
				
				if(sortOrder == SortOrder.DESCENDING)
				{
					result *= -1;
				}
				
				return result;
			}
		}
		
		return 0;
	}
	
	protected Object invokeGetter(Object target, String propertyName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method m = target.getClass().getMethod("get" + propertyName);
		
		return m.invoke(target);
	}
}
