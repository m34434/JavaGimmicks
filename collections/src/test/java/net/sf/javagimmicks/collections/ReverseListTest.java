package net.sf.javagimmicks.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

public class ReverseListTest
{
   protected List<String> _list;
   protected List<String> _reverse;
   
   @Before
   public void setUp()
   {
      _list = new ArrayList<String>(Arrays.asList(new String[]{"a", "b", "c", "d", "e"}));
      _reverse = new ReverseList<String>(_list);
      
//      Collections.reverse(_list);
//      _reverse = _list;
   }
   
   @Test
   public void testSize()
   {
      assertEquals(_list.isEmpty(), _reverse.isEmpty());
      assertEquals(_list.size(), _reverse.size());
   }
   
   @Test
   public void testGet()
   {
      int listSize = _list.size();
      for(int i = 0; i < listSize; ++i)
      {
         assertSame(_list.get(i), _reverse.get(listSize - i - 1));
      }
   }
   
   @Test
   public void testListIterator()
   {
      ListIterator<String> itr = _reverse.listIterator();
      testIteratorIndeces(itr, -1);
      
      itr.add("g");
      itr.previous();
      testIteratorIndeces(itr, -1);
      
      itr.next();
      testIteratorIndeces(itr, 0);
      
      itr.add("f");
      testIteratorIndeces(itr, 1);
      
      itr.next();
      testIteratorIndeces(itr, 2);
      
      itr.set("e2");
      testIteratorIndeces(itr, 2);
      
      itr.add("e1");
      testIteratorIndeces(itr, 3);
      
      itr.previous();
      testIteratorIndeces(itr, 2);
      
      itr.previous();
      testIteratorIndeces(itr, 1);
      
      itr.add("f0");
      testIteratorIndeces(itr, 2);

      testListContent("g", "f", "f0", "e2", "e1", "d", "c", "b", "a");
   }

   protected void testListContent(String... expected)
   {
      assertEquals(expected.length, _reverse.size());
      
      ListIterator<String> itr = _reverse.listIterator();
      while(itr.hasNext())
      {
         assertEquals(expected[itr.nextIndex()], itr.next());
      }
      
      testIteratorIndeces(itr, expected.length - 1);
   }

   protected static void testIteratorIndeces(ListIterator<?> itr, int previousIndex)
   {
      assertEquals(previousIndex, itr.previousIndex());
      assertEquals(previousIndex + 1, itr.nextIndex());
   }
}
