package net.sf.javagimmicks.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

/**
 * A set of utility methods for arrays especially for converting arrays into
 * instances of {@link List} with implicit boxing of primitive types as well as
 * methods for boxing primitive type arrays into respective wrapped type arrays.
 */
public class ArrayUtils
{
   private ArrayUtils()
   {}

   /**
    * Wraps the given array into a fixed-size {@link List} of the given array
    * type.
    * 
    * @param array
    *           the array to wrap
    * @param <E>
    *           the type of elements of the {@link List} to create
    * @return the resulting {@link List}
    * 
    * @see Arrays#asList(Object...)
    */
   public static <E> List<E> asList(final E... array)
   {
      return Arrays.asList(array);
   }

   /**
    * Wraps the given <code>int</code> array into a fixed-size {@link List} of
    * {@link Integer}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Integer> asList(final int... array)
   {
      return new AbstractList<Integer>()
      {
         @Override
         public Integer get(final int index)
         {
            return array[index];
         }

         @Override
         public Integer set(final int index, final Integer element)
         {
            final int result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>long</code> array into a fixed-size {@link List} of
    * {@link Long}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Long> asList(final long... array)
   {
      return new AbstractList<Long>()
      {
         @Override
         public Long get(final int index)
         {
            return array[index];
         }

         @Override
         public Long set(final int index, final Long element)
         {
            final long result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>byte</code> array into a fixed-size {@link List} of
    * {@link Byte}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Byte> asList(final byte... array)
   {
      return new AbstractList<Byte>()
      {
         @Override
         public Byte get(final int index)
         {
            return array[index];
         }

         @Override
         public Byte set(final int index, final Byte element)
         {
            final byte result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>short</code> array into a fixed-size {@link List} of
    * {@link Short}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Short> asList(final short... array)
   {
      return new AbstractList<Short>()
      {
         @Override
         public Short get(final int index)
         {
            return array[index];
         }

         @Override
         public Short set(final int index, final Short element)
         {
            final short result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>float</code> array into a fixed-size {@link List} of
    * {@link Float}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Float> asList(final float... array)
   {
      return new AbstractList<Float>()
      {
         @Override
         public Float get(final int index)
         {
            return array[index];
         }

         @Override
         public Float set(final int index, final Float element)
         {
            final float result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>double</code> array into a fixed-size {@link List}
    * of {@link Double}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Double> asList(final double... array)
   {
      return new AbstractList<Double>()
      {
         @Override
         public Double get(final int index)
         {
            return array[index];
         }

         @Override
         public Double set(final int index, final Double element)
         {
            final double result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>boolean</code> array into a fixed-size {@link List}
    * of {@link Boolean}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Boolean> asList(final boolean... array)
   {
      return new AbstractList<Boolean>()
      {
         @Override
         public Boolean get(final int index)
         {
            return array[index];
         }

         @Override
         public Boolean set(final int index, final Boolean element)
         {
            final boolean result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }
      };
   }

   /**
    * Wraps the given <code>char</code> array into a fixed-size {@link List} of
    * {@link Character}s.
    * 
    * @param array
    *           the array to wrap
    * @return the resulting {@link List}
    */
   public static List<Character> asList(final char... array)
   {
      return new AbstractList<Character>()
      {
         @Override
         public Character get(final int index)
         {
            return array[index];
         }

         @Override
         public Character set(final int index, final Character element)
         {
            final char result = array[index];
            array[index] = element;
            return result;
         }

         @Override
         public int size()
         {
            return array.length;
         }

      };
   }

   /**
    * Converts the given <code>int</code> array into a {@link Integer} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Integer[] wrap(final int... array)
   {
      return asList(array).toArray(new Integer[0]);
   }

   /**
    * Converts the given <code>long</code> array into a {@link Long} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Long[] wrap(final long... array)
   {
      return asList(array).toArray(new Long[0]);
   }

   /**
    * Converts the given <code>byte</code> array into a {@link Byte} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Byte[] wrap(final byte... array)
   {
      return asList(array).toArray(new Byte[0]);
   }

   /**
    * Converts the given <code>short</code> array into a {@link Short} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Short[] wrap(final short... array)
   {
      return asList(array).toArray(new Short[0]);
   }

   /**
    * Converts the given <code>float</code> array into a {@link Float} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Float[] wrap(final float... array)
   {
      return asList(array).toArray(new Float[0]);
   }

   /**
    * Converts the given <code>double</code> array into a {@link Double} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Double[] wrap(final double... array)
   {
      return asList(array).toArray(new Double[0]);
   }

   /**
    * Converts the given <code>boolean</code> array into a {@link Boolean}
    * array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Boolean[] wrap(final boolean... array)
   {
      return asList(array).toArray(new Boolean[0]);
   }

   /**
    * Converts the given <code>char</code> array into a {@link Character} array.
    * 
    * @param array
    *           the given array
    * @return the converted array
    */
   public static Character[] wrap(final char... array)
   {
      return asList(array).toArray(new Character[0]);
   }
}
