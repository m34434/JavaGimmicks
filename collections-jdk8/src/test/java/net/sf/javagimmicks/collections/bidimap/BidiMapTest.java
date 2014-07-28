package net.sf.javagimmicks.collections.bidimap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BidiMapTest
{
   protected BidiMap<Integer, String> _bidiMap;

   @Before
   public void setUp()
   {
      _bidiMap = new DualBidiMap<Integer, String>(new TreeMap<Integer, String>(), new HashMap<String, Integer>());
   }

   @Test
   public void testBasic()
   {
      addReferencePairs();
      assertEquals(_referencePairs.size(), _bidiMap.size());
      validateSize();
      validateReferencePairs();
   }

   @Test
   public void testAddNewPairs()
   {
      addReferencePairs();

      addNewPairs();
      assertEquals(_referencePairs.size(), _bidiMap.size());
      validateSize();
      validateNewPairs();
   }

   @Test
   public void testRemove()
   {
      addReferencePairs();
      removeReferencePairs();
      validateSize();

      addReferencePairs();
      removeReferencePairsInverted();
      validateSize();
   }

   @Test
   public void testDoubleInvert()
   {
      addReferencePairs();

      validateDoubleInvert();
   }

   @Test
   public void testEntries()
   {
      addReferencePairs();

      updateAndRemoveEntries();
   }

   @Test
   public void testAddNullValue()
   {
      try
      {
         _bidiMap.put(666, null);
         Assert.fail(IllegalArgumentException.class.getName() + " expected!");
      }
      catch (final IllegalArgumentException ignore)
      {
      }
   }

   protected void addReferencePairs()
   {
      for (final Iterator<Pair<Integer, String>> iterator = _referencePairs.iterator(); iterator.hasNext();)
      {
         assertNull(put(iterator.next()));

         if (iterator.hasNext())
         {
            assertNull(putInverted(iterator.next()));
         }
      }
   }

   protected void validateReferencePairs()
   {
      for (final Pair<Integer, String> pair : _referencePairs)
      {
         validate(pair);
      }

      for (final Pair<Integer, String> pair : _referencePairs)
      {
         validateInverted(pair);
      }
   }

   protected void removeReferencePairs()
   {
      for (final Pair<Integer, String> pair : _referencePairs)
      {
         assertTrue(containsKey(pair.a));
         assertTrue(containsValue(pair.b));
         assertTrue(containsKeyInverted(pair.b));
         assertTrue(containsValueInverted(pair.a));

         assertEquals(pair.b, remove(pair.a));

         assertFalse(containsKey(pair.a));
         assertFalse(containsValue(pair.b));
         assertFalse(containsKeyInverted(pair.b));
         assertFalse(containsValueInverted(pair.a));
      }
   }

   protected void removeReferencePairsInverted()
   {
      for (final Pair<Integer, String> pair : _referencePairs)
      {
         assertTrue(containsKey(pair.a));
         assertTrue(containsValue(pair.b));
         assertTrue(containsKeyInverted(pair.b));
         assertTrue(containsValueInverted(pair.a));

         assertEquals(pair.a, removeInverted(pair.b));

         assertFalse(containsKey(pair.a));
         assertFalse(containsValue(pair.b));
         assertFalse(containsKeyInverted(pair.b));
         assertFalse(containsValueInverted(pair.a));
      }
   }

   protected void addNewPairs()
   {
      for (final Iterator<Pair<Integer, String>> iterator = _newPairs.iterator(); iterator.hasNext();)
      {
         Pair<Integer, String> pair = iterator.next();
         final String value = get(pair.a);

         assertEquals(value, put(pair));

         if (iterator.hasNext())
         {
            pair = iterator.next();
            final Integer invertedValue = getInverted(pair.b);
            assertEquals(invertedValue, putInverted(pair));
         }
      }
   }

   protected void updateAndRemoveEntries()
   {
      for (final Entry<Integer, String> entry : _bidiMap.entrySet())
      {
         assertEquals(entry.getValue(), entry.setValue("_" + entry.getValue() + "_"));
      }

      final List<Pair<Integer, String>> referencePairsClone = new ArrayList<Pair<Integer, String>>(_referencePairs);
      for (final Iterator<Entry<Integer, String>> entryIter = _bidiMap.entrySet().iterator(); entryIter.hasNext();)
      {
         final Entry<Integer, String> entry = entryIter.next();
         final Pair<Integer, String> pair = new Pair<Integer, String>(entry.getKey(), entry.getValue().substring(1, 2));

         referencePairsClone.remove(pair);

         entryIter.remove();
      }

      assertTrue(referencePairsClone.isEmpty());
      validateSize();
      assertEquals(0, _bidiMap.size());
   }

   protected void validateNewPairs()
   {
      for (final Pair<Integer, String> pair : _newPairs)
      {
         validate(pair);
      }

      for (final Pair<Integer, String> pair : _newPairs)
      {
         validateInverted(pair);
      }
   }

   protected void validateSize()
   {
      assertEquals(_bidiMap.size(), _bidiMap.inverseBidiMap().size());
   }

   protected void validateDoubleInvert()
   {
      assertEquals(_bidiMap, _bidiMap.inverseBidiMap().inverseBidiMap());
   }

   protected String get(final Integer a)
   {
      return _bidiMap.get(a);
   }

   protected Integer getKey(final String a)
   {
      return _bidiMap.getKey(a);
   }

   protected Integer getInverted(final String a)
   {
      return _bidiMap.inverseBidiMap().get(a);
   }

   protected String getKeyInverted(final Integer a)
   {
      return _bidiMap.inverseBidiMap().getKey(a);
   }

   protected void validate(final Integer a, final String b)
   {
      assertEquals(b, get(a));
      assertEquals(b, getKeyInverted(a));
   }

   protected void validateInverted(final Integer a, final String b)
   {
      assertEquals(a, getInverted(b));
      assertEquals(a, getKey(b));
   }

   protected void validate(final Pair<Integer, String> p)
   {
      validate(p.a, p.b);
   }

   protected void validateInverted(final Pair<Integer, String> p)
   {
      validateInverted(p.a, p.b);
   }

   protected String put(final Integer a, final String b)
   {
      return _bidiMap.put(a, b);
   }

   protected String put(final Pair<Integer, String> p)
   {
      return put(p.a, p.b);
   }

   protected String remove(final Integer a)
   {
      return _bidiMap.remove(a);
   }

   protected Integer putInverted(final Integer a, final String b)
   {
      return _bidiMap.inverseBidiMap().put(b, a);
   }

   protected Integer putInverted(final Pair<Integer, String> p)
   {
      return putInverted(p.a, p.b);
   }

   protected Integer removeInverted(final String b)
   {
      return _bidiMap.inverseBidiMap().remove(b);
   }

   protected boolean containsKey(final Integer a)
   {
      return _bidiMap.containsKey(a);
   }

   protected boolean containsValue(final String b)
   {
      return _bidiMap.containsValue(b);
   }

   protected boolean containsKeyInverted(final String b)
   {
      return _bidiMap.inverseBidiMap().containsKey(b);
   }

   protected boolean containsValueInverted(final Integer a)
   {
      return _bidiMap.inverseBidiMap().containsValue(a);
   }

   protected static <A, B> void add(final Collection<Pair<A, B>> c, final Pair<A, B> p)
   {
      c.add(p);
   }

   protected static <A, B> void add(final Collection<Pair<A, B>> c, final A a, final B b)
   {
      add(c, new Pair<A, B>(a, b));
   }

   protected static List<Pair<Integer, String>> _referencePairs = new ArrayList<Pair<Integer, String>>();
   protected static List<Pair<Integer, String>> _newPairs = new ArrayList<Pair<Integer, String>>();

   static
   {
      add(_referencePairs, 1, "h");
      add(_referencePairs, 2, "g");
      add(_referencePairs, 3, "f");
      add(_referencePairs, 4, "e");
      add(_referencePairs, 5, "d");
      add(_referencePairs, 6, "c");
      add(_referencePairs, 7, "b");
      add(_referencePairs, 8, "a");

      add(_newPairs, 1, "hh");
      add(_newPairs, 2, "gg");
   }

   public static class Pair<A, B>
   {
      public final A a;
      public final B b;

      public Pair(final A a, final B b)
      {
         this.a = a;
         this.b = b;
      }

      public Pair<B, A> invert()
      {
         return new Pair<B, A>(b, a);
      }

      @Override
      public int hashCode()
      {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((a == null) ? 0 : a.hashCode());
         result = prime * result + ((b == null) ? 0 : b.hashCode());
         return result;
      }

      @Override
      public boolean equals(final Object obj)
      {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         final Pair<?, ?> other = (Pair<?, ?>) obj;
         if (a == null)
         {
            if (other.a != null) return false;
         }
         else if (!a.equals(other.a)) return false;
         if (b == null)
         {
            if (other.b != null) return false;
         }
         else if (!b.equals(other.b)) return false;
         return true;
      }
   }
}
