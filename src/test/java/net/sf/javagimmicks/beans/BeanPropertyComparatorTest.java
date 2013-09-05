package net.sf.javagimmicks.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import net.sf.javagimmicks.beans.BeanPropertyComparator;
import net.sf.javagimmicks.beans.BeanPropertyComparator.SortOrder;

public class BeanPropertyComparatorTest
{
	protected final Person _person1 = new Person("Apple", "Andy", 23);
	protected final Person _person2 = new Person("Apple", "Andy", 85);
	protected final Person _person3 = new Person("Apple", "Eddy", 23);
	protected final Person _person4 = new Person("Peach", "Peter", 42);
	protected final Person _person5 = new Person("Peach", "Andy", 42);
	
	@Test
	public void testAutomaticProperties()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>(Person.class);
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertTrue(comparator.compare(_person1, _person3) < 0);
		assertTrue(comparator.compare(_person1, _person4) < 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
	
	@Test
	public void testAutomaticPropertiesWithSortOrder()
	{
		BeanPropertyComparator<Person> comparator = new BeanPropertyComparator<Person>(Person.class);
		comparator.setSortOrder("LastName", SortOrder.DESCENDING);
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertTrue(comparator.compare(_person1, _person3) < 0);
		assertTrue(comparator.compare(_person1, _person4) > 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
	
	@Test
	public void testAllPropertiesWithList1()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("LastName", "FirstName", "Age");
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertTrue(comparator.compare(_person1, _person3) < 0);
		assertTrue(comparator.compare(_person1, _person4) < 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
		
	@Test
	public void testAllPropertiesWithList2()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("FirstName", "LastName", "Age");
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertTrue(comparator.compare(_person1, _person3) < 0);
		assertTrue(comparator.compare(_person1, _person4) < 0);
		assertTrue(comparator.compare(_person3, _person5) > 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
		
	@Test
	public void testAllPropertiesWithList3()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("Age", "FirstName", "LastName");
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertTrue(comparator.compare(_person1, _person3) < 0);
		assertTrue(comparator.compare(_person1, _person4) < 0);
		assertTrue(comparator.compare(_person3, _person5) < 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
		
	@Test
	public void testTwoProperties1()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("LastName","FirstName");
		
		assertEquals(0, comparator.compare(_person1, _person2));
		assertTrue(comparator.compare(_person3, _person5) < 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
		
	@Test
	public void testTwoProperties2()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("FirstName", "LastName");
		
		assertEquals(0, comparator.compare(_person1, _person2));
		assertTrue(comparator.compare(_person3, _person5) > 0);

		assertEquals(0, comparator.compare(_person1, _person1));
	}
	
	@Test
	public void testOneProperty1()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("LastName");
		
		assertEquals(0, comparator.compare(_person1, _person2));
		assertEquals(0, comparator.compare(_person1, _person3));
		assertTrue(comparator.compare(_person1, _person4) < 0);
		assertEquals(0, comparator.compare(_person4, _person5));

		assertEquals(0, comparator.compare(_person1, _person1));
	}
	
	@Test
	public void testOneProperty2()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("Age");
		
		assertTrue(comparator.compare(_person1, _person2) < 0);
		assertEquals(0, comparator.compare(_person1, _person3));
		assertTrue(comparator.compare(_person1, _person4) < 0);
		assertEquals(0, comparator.compare(_person4, _person5));

		assertEquals(0, comparator.compare(_person1, _person1));
	}
	
	@Test
	public void testInversion()
	{
		Comparator<Person> comparator = new BeanPropertyComparator<Person>("LastName", "FirstName", "Age");

		assertInversion(comparator, _person1, _person2);
		assertInversion(comparator, _person1, _person3);
		assertInversion(comparator, _person1, _person4);
		
		assertInversion(comparator, _person1, _person1);
	}
	
	protected static void assertInversion(Comparator<Person> comparator, Person p1, Person p2)
	{
		assertEquals(comparator.compare(p1, p2), comparator.compare(p2, p1) * -1);
	}
		
	public static final class Person
	{
		protected final String _lastName;
		protected final String _firstName;
		protected final int _age;
		
		public Person(String lastName, String firstName, int age)
		{
			_lastName = lastName;
			_firstName = firstName;
			_age = age;
		}

		public String getLastName()
		{
			return _lastName;
		}

		public String getFirstName()
		{
			return _firstName;
		}

		public int getAge()
		{
			return _age;
		}
	}
}
