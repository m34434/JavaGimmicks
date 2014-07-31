package net.sf.javagimmicks.collections8.composite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.javagimmicks.collections.builder.ListBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompositeIteratorTest
{
   private Iterator<String> it1;
   private Iterator<String> it2;
   private Iterator<String> cIt;
   private ArrayList<String> collection1;
   private ArrayList<String> collection2;

   @Before
   public void setup()
   {
      collection1 = ListBuilder.<String> createArrayList().addAll("a", "b").toCollection();
      collection2 = ListBuilder.<String> createArrayList().addAll("c", "d").toCollection();

      it1 = collection1.iterator();
      it2 = collection2.iterator();

      cIt = CompositeUtils.iterator(it1, it2);
   }

   @After
   public void tearDown()
   {
      collection1.clear();
      collection2.clear();

      collection1 = null;
      collection2 = null;
      it1 = null;
      it2 = null;
      cIt = null;
   }

   @Test
   public void testNextHasNext()
   {
      assertTrue(it1.hasNext());
      assertTrue(it2.hasNext());
      assertTrue(cIt.hasNext());

      assertEquals("a", cIt.next());

      assertTrue(it1.hasNext());
      assertTrue(it2.hasNext());
      assertTrue(cIt.hasNext());

      assertEquals("b", cIt.next());

      assertFalse(it1.hasNext());
      assertTrue(it2.hasNext());
      assertTrue(cIt.hasNext());

      assertEquals("c", cIt.next());

      assertFalse(it1.hasNext());
      assertTrue(it2.hasNext());
      assertTrue(cIt.hasNext());

      assertEquals("d", cIt.next());

      assertFalse(it1.hasNext());
      assertFalse(it2.hasNext());
      assertFalse(cIt.hasNext());

      try
      {
         cIt.next();
         fail("NoSuchElementException expected!");
      }
      catch (final NoSuchElementException ex)
      {
      }
   }

   @Test
   public void testRemove()
   {
      try
      {
         cIt.remove();
         fail("IllegalStateException expected!");
      }
      catch (final IllegalStateException ex)
      {
      }

      cIt.next();
      cIt.remove();

      cIt.next();
      cIt.remove();

      assertTrue(collection1.isEmpty());

      cIt.next();
      cIt.remove();

      cIt.next();
      cIt.remove();

      assertTrue(collection2.isEmpty());

      try
      {
         cIt.remove();
         fail("IllegalStateException expected!");
      }
      catch (final IllegalStateException ex)
      {
      }

   }
}
