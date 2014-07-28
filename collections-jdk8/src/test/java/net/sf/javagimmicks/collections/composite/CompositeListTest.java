package net.sf.javagimmicks.collections.composite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

import net.sf.javagimmicks.collections.ListComparator;
import net.sf.javagimmicks.collections.ReverseList;
import net.sf.javagimmicks.collections.builder.ListBuilder;

public class CompositeListTest
{
   private List<String> _l1;
   private List<String> _l2;
   private List<String> _l3;
   private List<String> _l4;
   private List<String> _l5;
   
   private List<String> _composite;
   
   @Before
   @SuppressWarnings("unchecked")
   public void prepare()
   {
      _l1 = ListBuilder.create(new ArrayList<String>()).addAll("A", "B", "C").get();
      _l2 = new ArrayList<String>();
      _l3 = ListBuilder.create(new LinkedList<String>()).addAll("D", "E", "F").get();
      _l4 = Collections.emptyList();
      _l5 = Collections.singletonList("G");
      
      _composite = CompositeUtils.list(_l1, _l2, _l3, _l4, _l5);
   }
   
   @Test
   public void testEquality()
   {
      List<String> reference = buildReference(_l1, _l2, _l3, _l4, _l5);

      assertEquals(reference.size(), _composite.size());
      
      Comparator<List<String>> comparator = ListComparator.getComparableInstance();
      assertEquals(0, comparator.compare(reference, _composite));
   }
   
   @Test
   public void testEqualityReverse()
   {
      // This will especially test, if the internal ListIterator works right in the reverse way
      
      List<String> referenceReverse = ReverseList.decorate(buildReference(_l1, _l2, _l3, _l4, _l5));
      List<String> compositeReverse = ReverseList.decorate(_composite);

      assertEquals(referenceReverse.size(), compositeReverse.size());

      Comparator<List<String>> comparator = ListComparator.getComparableInstance();
      assertEquals(0, comparator.compare(referenceReverse, compositeReverse));
   }
   
   @Test
   public void testRemove0()
   {
      _composite.remove(0);
      assertFalse(_composite.contains("A"));
      assertFalse(_l1.contains("A"));
   }

   @Test
   public void testRemove1()
   {
      _composite.remove(1);
      assertFalse(_composite.contains("B"));
      assertFalse(_l1.contains("B"));
   }

   @Test
   public void testRemove2()
   {
      _composite.remove(2);
      assertFalse(_composite.contains("C"));
      assertFalse(_l1.contains("C"));
   }

   @Test
   public void testRemove3()
   {
      _composite.remove(3);
      assertFalse(_composite.contains("D"));
      assertFalse(_l3.contains("D"));
   }

   @Test
   public void testRemove4()
   {
      _composite.remove(4);
      assertFalse(_composite.contains("E"));
      assertFalse(_l3.contains("E"));
   }

   @Test
   public void testRemove5()
   {
      _composite.remove(5);
      assertFalse(_composite.contains("F"));
      assertFalse(_l3.contains("F"));
   }

   @Test
   public void testRemove6()
   {
      try
      {
         _composite.remove(6);
         fail(UnsupportedOperationException.class.getSimpleName() + " expected!");
      }
      catch(UnsupportedOperationException ex) {}
   }
   
   @Test
   public void testRemoveA()
   {
      _composite.remove("A");
      assertFalse(_composite.contains("A"));
      assertFalse(_l1.contains("A"));
   }

   @Test
   public void testRemoveB()
   {
      _composite.remove("B");
      assertFalse(_composite.contains("B"));
      assertFalse(_l1.contains("B"));
   }

   @Test
   public void testRemoveC()
   {
      _composite.remove("C");
      assertFalse(_composite.contains("C"));
      assertFalse(_l1.contains("C"));
   }

   @Test
   public void testRemoveD()
   {
      _composite.remove("D");
      assertFalse(_composite.contains("D"));
      assertFalse(_l3.contains("D"));
   }

   @Test
   public void testRemoveE()
   {
      _composite.remove("E");
      assertFalse(_composite.contains("E"));
      assertFalse(_l3.contains("E"));
   }

   @Test
   public void testRemoveF()
   {
      _composite.remove("F");
      assertFalse(_composite.contains("F"));
      assertFalse(_l3.contains("F"));
   }

   @Test
   public void testRemoveG()
   {
      try
      {
         _composite.remove("G");
         fail(UnsupportedOperationException.class.getSimpleName() + " expected!");
      }
      catch(UnsupportedOperationException ex) {}
   }
   
   @Test
   public void testRemoveMany()
   {
      _composite.remove("A");
      assertFalse(_composite.contains("A"));
      assertFalse(_l1.contains("A"));

      _composite.remove("B");
      assertFalse(_composite.contains("B"));
      assertFalse(_l1.contains("B"));

      _composite.remove("C");
      assertFalse(_composite.contains("C"));
      assertFalse(_l1.contains("C"));

      _composite.remove("D");
      assertFalse(_composite.contains("D"));
      assertFalse(_l3.contains("D"));

      _composite.remove("E");
      assertFalse(_composite.contains("E"));
      assertFalse(_l3.contains("E"));
      
      _composite.remove("F");
      assertFalse(_composite.contains("F"));
      assertFalse(_l3.contains("F"));
   }
   
   @Test
   public void testRemoveManyByIndex()
   {
      assertEquals("B", _composite.remove(1));
      assertFalse(_composite.contains("B"));
      assertFalse(_l1.contains("B"));

      assertEquals("D", _composite.remove(2));
      assertFalse(_composite.contains("D"));
      assertFalse(_l2.contains("D"));

      assertEquals("A", _composite.remove(0));
      assertFalse(_composite.contains("A"));
      assertFalse(_l1.contains("A"));

      assertEquals("F", _composite.remove(2));
      assertFalse(_composite.contains("F"));
      assertFalse(_l3.contains("F"));

      assertEquals("E", _composite.remove(1));
      assertFalse(_composite.contains("E"));
      assertFalse(_l3.contains("E"));
      
      assertEquals("C", _composite.remove(0));
      assertFalse(_composite.contains("C"));
      assertFalse(_l3.contains("C"));
      
      try
      {
         _composite.remove(0);
         fail(UnsupportedOperationException.class.getSimpleName() + " expected!");
      }
      catch(UnsupportedOperationException ex) {}
   }
   
   @Test
   public void testAdd()
   {
      int sizeBefore;
      
      // Add an element to _l1
      sizeBefore = _composite.size();
      _l1.add("X");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("X"));

      // Add an element to _l2
      sizeBefore = _composite.size();
      _l2.add("Y");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("Y"));

      // Add an element to _l3
      sizeBefore = _composite.size();
      _l3.add("Z");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("Z"));
   }
   
   @Test
   public void testListIterator()
   {
      ListIterator<String> it = _composite.listIterator(3);
      
      assertEquals(2, it.previousIndex());
      assertEquals(3, it.nextIndex());
      
      assertEquals("D", it.next());
      assertEquals(3, it.previousIndex());
      assertEquals(4, it.nextIndex());

      assertEquals("D", it.previous());
      assertEquals(2, it.previousIndex());
      assertEquals(3, it.nextIndex());

      assertEquals("C", it.previous());
      assertEquals(1, it.previousIndex());
      assertEquals(2, it.nextIndex());

      assertEquals("C", it.next());
      assertEquals(2, it.previousIndex());
      assertEquals(3, it.nextIndex());
      
      assertEquals("D", it.next());
      assertEquals(3, it.previousIndex());
      assertEquals(4, it.nextIndex());
   }
   
   @SafeVarargs
   private static <E> List<E> buildReference(List<? extends E>... lists)
   {
      final ArrayList<E> result = new ArrayList<E>();

      for (List<? extends E> c : lists)
      {
         result.addAll(c);
      }

      return result;
   }

}
