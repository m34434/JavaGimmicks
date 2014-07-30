package net.sf.javagimmicks.collections8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.javagimmicks.collections8.Cursor;
import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;

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

      RingCursor<String> ringCursor = _ring.cursor();
      assertEquals("A", ringCursor.get());
      assertEquals("B", ringCursor.next());
      assertEquals("C", ringCursor.next());
      assertEquals("D", ringCursor.next());
      assertEquals("E", ringCursor.next());

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
   public void testRingCursorReadOperations() throws Exception
   {
      RingCursor<String> ringCursor = _ring.cursor();
      assertEquals("A", ringCursor.get());

      assertEquals("D", ringCursor.next(3));
      assertEquals("B", ringCursor.next(3));
      assertEquals("E", ringCursor.next(3));
      assertEquals("C", ringCursor.next(3));
      assertEquals("A", ringCursor.next(3));

      assertEquals("C", ringCursor.previous(3));
      assertEquals("E", ringCursor.previous(3));
      assertEquals("B", ringCursor.previous(3));
      assertEquals("D", ringCursor.previous(3));
      assertEquals("A", ringCursor.previous(3));

      assertEquals("D", ringCursor.next(8));
      assertEquals("B", ringCursor.next(13));
      assertEquals("E", ringCursor.next(18));
      assertEquals("C", ringCursor.next(23));
      assertEquals("A", ringCursor.next(28));

      assertEquals("D", ringCursor.next(-2));
      assertEquals("B", ringCursor.next(-7));
      assertEquals("E", ringCursor.next(-12));
      assertEquals("C", ringCursor.next(-17));
      assertEquals("A", ringCursor.next(-22));

      assertEquals("D", ringCursor.previous(-3));
      assertEquals("B", ringCursor.previous(-8));
      assertEquals("E", ringCursor.previous(-13));
      assertEquals("C", ringCursor.previous(-18));
      assertEquals("A", ringCursor.previous(-23));

      RingCursor<String> ringCursor2 = ringCursor.cursor();
      assertEquals(ringCursor.get(), ringCursor2.get());

      assertEquals("B", ringCursor.next());
      assertEquals("B", ringCursor2.next());

      Iterator<String> iterator = ringCursor.iterator();
      assertEquals("B", iterator.next());
      assertEquals("C", iterator.next());
      assertEquals("D", iterator.next());
      assertEquals("E", iterator.next());
      assertEquals("A", iterator.next());
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testRingCursorWriteOperations() throws Exception
   {
      RingCursor<String> ringCursor = _ring.cursor();
      assertEquals("A", ringCursor.get());

      assertEquals("A", ringCursor.remove());
      assertEquals("B", ringCursor.get());

      ringCursor.insertBefore("A");
      assertEquals("B", ringCursor.get());

      assertEquals("A", ringCursor.previous());

      ringCursor.insertAfter(Arrays.asList("A1", "A2"));
      assertEquals("A", ringCursor.get());

      while (!ringCursor.isEmpty())
         ringCursor.remove();
      assertTrue(_ring.isEmpty());
      assertTrue(ringCursor.isEmpty());

      try
      {
         ringCursor.get();
         fail(NoSuchElementException.class.getSimpleName() + " expected");
      }
      catch (NoSuchElementException ex)
      {
      }
      
      ringCursor.insertAfter("A");
      assertEquals("A", ringCursor.get());
   }

   @Test
   public void testConcurrentModification() throws Exception
   {
      RingCursor<String> t11 = _ring.cursor();
      RingCursor<String> t21 = _ring.cursor();
      RingCursor<String> t12 = t11.cursor();
      RingCursor<String> t22 = t21.cursor();
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
      Cursor<String> ringCursor = _ring.cursor();

      ringCursor.insertAfter("ABB");
      assertEquals(6, _ring.size());
      assertEquals("ABB", ringCursor.next());

      ringCursor.insertBefore("AAB");
      assertEquals(7, _ring.size());
      assertEquals("AAB", ringCursor.previous());

      List<String> xyList = Arrays.asList(new String[] { "X", "Y" });
      ringCursor.insertAfter(xyList);
      assertEquals(9, _ring.size());
      assertEquals("X", ringCursor.next());
      assertEquals("Y", ringCursor.next());

      ringCursor.next();
      ringCursor.insertBefore(xyList);
      assertEquals(11, _ring.size());
      assertEquals("Y", ringCursor.previous());
      assertEquals("X", ringCursor.previous());
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
      Cursor<String> ringCursor = _ring.cursor();

      ringCursor.set("V");
      ringCursor.next();
      ringCursor.set("W");
      ringCursor.next();
      ringCursor.set("X");
      ringCursor.next();
      ringCursor.set("Y");
      ringCursor.next();
      ringCursor.set("Z");

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
         _ring.cursor().remove();
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
         _ring.cursor().set("X");
      }
      catch (NoSuchElementException ex)
      {
      }
   }
}
