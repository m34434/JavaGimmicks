package net.sf.javagimmicks.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractRingTest
{
   protected abstract Ring<String> createRing();

   private Ring<String> _ring;

   @Before
   public void setUp() throws Exception
   {
      _ring = createRing();

      assertTrue(_ring.add("A"));
      assertTrue(_ring.add("B"));
      assertTrue(_ring.add("C"));
      assertTrue(_ring.add("D"));
      assertTrue(_ring.add("E"));
   }

   @Test
   public void testContent() throws Exception
   {
      assertEquals(5, _ring.size());

      Cursor<String> traverser = _ring.traverser();
      assertEquals("A", traverser.get());
      assertEquals("B", traverser.next());
      assertEquals("C", traverser.next());
      assertEquals("D", traverser.next());
      assertEquals("E", traverser.next());

      Iterator<String> iterator = _ring.iterator();
      assertEquals("A", iterator.next());
      assertEquals("B", iterator.next());
      assertEquals("C", iterator.next());
      assertEquals("D", iterator.next());
      assertEquals("E", iterator.next());
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testRemove() throws Exception
   {
      assertTrue(_ring.contains("C"));
      _ring.remove("C");
      assertFalse(_ring.contains("C"));

      List<String> aeList = Arrays.asList(new String[] { "A", "E" });

      assertTrue(_ring.containsAll(aeList));
      _ring.removeAll(aeList);
      assertFalse(_ring.contains("A"));
      assertFalse(_ring.contains("E"));
      assertFalse(_ring.containsAll(aeList));
   }

   @Test
   public void testTraverserReadOperations() throws Exception
   {
      Traverser<String> traverser = _ring.traverser();
      assertEquals("A", traverser.get());

      assertEquals("D", traverser.next(3));
      assertEquals("B", traverser.next(3));
      assertEquals("E", traverser.next(3));
      assertEquals("C", traverser.next(3));
      assertEquals("A", traverser.next(3));

      assertEquals("C", traverser.previous(3));
      assertEquals("E", traverser.previous(3));
      assertEquals("B", traverser.previous(3));
      assertEquals("D", traverser.previous(3));
      assertEquals("A", traverser.previous(3));

      assertEquals("D", traverser.next(8));
      assertEquals("B", traverser.next(13));
      assertEquals("E", traverser.next(18));
      assertEquals("C", traverser.next(23));
      assertEquals("A", traverser.next(28));

      assertEquals("D", traverser.next(-2));
      assertEquals("B", traverser.next(-7));
      assertEquals("E", traverser.next(-12));
      assertEquals("C", traverser.next(-17));
      assertEquals("A", traverser.next(-22));

      assertEquals("D", traverser.previous(-3));
      assertEquals("B", traverser.previous(-8));
      assertEquals("E", traverser.previous(-13));
      assertEquals("C", traverser.previous(-18));
      assertEquals("A", traverser.previous(-23));

      Cursor<String> traverser2 = traverser.traverser();
      assertEquals(traverser.get(), traverser2.get());

      assertEquals("B", traverser.next());
      assertEquals("B", traverser2.next());

      Iterator<String> iterator = traverser.iterator();
      assertEquals("B", iterator.next());
      assertEquals("C", iterator.next());
      assertEquals("D", iterator.next());
      assertEquals("E", iterator.next());
      assertEquals("A", iterator.next());
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testTraverserWriteOperations() throws Exception
   {
      Traverser<String> traverser = _ring.traverser();
      assertEquals("A", traverser.get());

      assertEquals("A", traverser.remove());
      assertEquals("B", traverser.get());

      traverser.insertBefore("A");
      assertEquals("B", traverser.get());

      assertEquals("A", traverser.previous());

      traverser.insertAfter(Arrays.asList("A1", "A2"));
      assertEquals("A", traverser.get());

      while (!traverser.isEmpty())
         traverser.remove();
      assertTrue(_ring.isEmpty());
      assertTrue(traverser.isEmpty());

      try
      {
         traverser.get();
         fail(NoSuchElementException.class.getSimpleName() + " expected");
      }
      catch (NoSuchElementException ex)
      {
      }
      
      traverser.insertAfter("A");
      assertEquals("A", traverser.get());
   }

   @Test
   public void testConcurrentModification() throws Exception
   {
      Traverser<String> t11 = _ring.traverser();
      Traverser<String> t21 = _ring.traverser();
      Cursor<String> t12 = t11.traverser();
      Cursor<String> t22 = t21.traverser();
      Iterator<String> it1 = _ring.iterator();
      Iterator<String> it2 = t11.iterator();

      t11.insertBefore("F");

      try
      {
         t21.next();
         fail("ConcurrentModificationException expected!");
      }
      catch (ConcurrentModificationException ex)
      {
      }

      try
      {
         t12.previous();
         fail("ConcurrentModificationException expected!");
      }
      catch (ConcurrentModificationException ex)
      {
      }

      try
      {
         t22.remove();
         fail("ConcurrentModificationException expected!");
      }
      catch (ConcurrentModificationException ex)
      {
      }

      try
      {
         it1.next();
         fail("ConcurrentModificationException expected!");
      }
      catch (ConcurrentModificationException ex)
      {
      }

      try
      {
         it2.next();
         fail("ConcurrentModificationException expected!");
      }
      catch (ConcurrentModificationException ex)
      {
      }
   }

   @Test
   public void testInsert() throws Exception
   {
      Cursor<String> traverser = _ring.traverser();

      traverser.insertAfter("ABB");
      assertEquals(6, _ring.size());
      assertEquals("ABB", traverser.next());

      traverser.insertBefore("AAB");
      assertEquals(7, _ring.size());
      assertEquals("AAB", traverser.previous());

      List<String> xyList = Arrays.asList(new String[] { "X", "Y" });
      traverser.insertAfter(xyList);
      assertEquals(9, _ring.size());
      assertEquals("X", traverser.next());
      assertEquals("Y", traverser.next());

      traverser.next();
      traverser.insertBefore(xyList);
      assertEquals(11, _ring.size());
      assertEquals("Y", traverser.previous());
      assertEquals("X", traverser.previous());
   }

   @Test
   public void testIteratorRemove() throws Exception
   {
      Iterator<?> it1 = _ring.iterator();

      try
      {
         it1.remove();
         fail("IllegalStateException expected!");
      }
      catch (IllegalStateException ex)
      {
      }

      it1.next();
      it1.remove();

      try
      {
         it1.remove();
         fail("IllegalStateException expected!");
      }
      catch (IllegalStateException ex)
      {
      }
   }

   @Test
   public void testReplace() throws Exception
   {
      Cursor<String> traverser = _ring.traverser();

      traverser.set("V");
      traverser.next();
      traverser.set("W");
      traverser.next();
      traverser.set("X");
      traverser.next();
      traverser.set("Y");
      traverser.next();
      traverser.set("Z");

      Iterator<String> iterator = _ring.iterator();
      assertEquals("V", iterator.next());
      assertEquals("W", iterator.next());
      assertEquals("X", iterator.next());
      assertEquals("Y", iterator.next());
      assertEquals("Z", iterator.next());
   }

   @Test
   public void testClear() throws Exception
   {
      _ring.clear();

      assertTrue(_ring.isEmpty());

      try
      {
         _ring.traverser().remove();
      }
      catch (NoSuchElementException ex)
      {
      }

      try
      {
         _ring.iterator().remove();
      }
      catch (NoSuchElementException ex)
      {
      }

      try
      {
         _ring.traverser().set("X");
      }
      catch (NoSuchElementException ex)
      {
      }
   }
}
