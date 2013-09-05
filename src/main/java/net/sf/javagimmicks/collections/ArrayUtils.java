package net.sf.javagimmicks.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils
{
	private ArrayUtils()
	{
	}

	public static <E> List<E> asList(final E... array)
	{
	   return Arrays.asList(array);
	}
	
	public static List<Integer> asList(final int... array)
	{
		return new AbstractList<Integer>()
		{
			public Integer get(int index)
			{
				return array[index];
			}

			public Integer set(int index, Integer element)
			{
				int result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}
	
	public static List<Long> asList(final long... array)
	{
		return new AbstractList<Long>()
		{
			public Long get(int index)
			{
				return array[index];
			}

			public Long set(int index, Long element)
			{
				long result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Byte> asList(final byte... array)
	{
		return new AbstractList<Byte>()
		{
			public Byte get(int index)
			{
				return array[index];
			}

			public Byte set(int index, Byte element)
			{
				byte result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Short> asList(final short... array)
	{
		return new AbstractList<Short>()
		{
			public Short get(int index)
			{
				return array[index];
			}

			public Short set(int index, Short element)
			{
				short result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Float> asList(final float... array)
	{
		return new AbstractList<Float>()
		{
			public Float get(int index)
			{
				return array[index];
			}

			public Float set(int index, Float element)
			{
				float result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Double> asList(final double... array)
	{
		return new AbstractList<Double>()
		{
			public Double get(int index)
			{
				return array[index];
			}

			public Double set(int index, Double element)
			{
				double result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Boolean> asList(final boolean... array)
	{
		return new AbstractList<Boolean>()
		{
			public Boolean get(int index)
			{
				return array[index];
			}

			public Boolean set(int index, Boolean element)
			{
				boolean result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}
		};
	}

	public static List<Character> asList(final char... array)
	{
		return new AbstractList<Character>()
		{
			public Character get(int index)
			{
				return array[index];
			}

			public Character set(int index, Character element)
			{
				char result = array[index];
				array[index] = element;
				return result;
			}

			public int size()
			{
				return array.length;
			}

		};
	}

   public static Integer[] wrap(final int... array)
   {
      return asList(array).toArray(new Integer[0]);
   }

   public static Long[] wrap(final long... array)
   {
      return asList(array).toArray(new Long[0]);
   }

   public static Byte[] wrap(final byte... array)
   {
      return asList(array).toArray(new Byte[0]);
   }

   public static Short[] wrap(final short... array)
   {
      return asList(array).toArray(new Short[0]);
   }

   public static Float[] wrap(final float... array)
   {
      return asList(array).toArray(new Float[0]);
   }

   public static Double[] wrap(final double... array)
   {
      return asList(array).toArray(new Double[0]);
   }

   public static Boolean[] wrap(final boolean... array)
   {
      return asList(array).toArray(new Boolean[0]);
   }

   public static Character[] wrap(final char... array)
   {
      return asList(array).toArray(new Character[0]);
   }
}
