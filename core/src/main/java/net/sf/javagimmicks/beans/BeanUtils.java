package net.sf.javagimmicks.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains a small set of utility methods for dealing with bean-link classes.
 */
public class BeanUtils
{
   /**
    * Extracts the names of all bean properties from the specified class into a
    * {@link List}.
    * <p>
    * This is done by finding public non-void no-argument methods starting with
    * 'get' or public no-argument methods starting with 'is' and returning a
    * {@link Boolean} or {@code boolean}.
    * 
    * @param beanClass
    *           the class to extract the property names from
    * @return the {@link List} containing the extracted property names
    */
   public static List<String> extractPropertyNames(final Class<?> beanClass)
   {
      final List<String> result = new ArrayList<String>();

      for (final Method m : beanClass.getMethods())
      {
         final String methodName = m.getName();
         if (m.getParameterTypes().length == 0 && Modifier.isPublic(m.getModifiers()))
         {
            if (isIsMethod(m))
            {
               result.add(methodName.substring(2));
            }
            else if (isGetMethod(m))
            {
               result.add(methodName.substring(3));
            }
         }
      }

      return result;
   }

   /**
    * Finds and returns the wrapper type for any primitive type. Non-primitive
    * types will result in themselves.
    * 
    * @param type
    *           the {@link Class} object of the type to find the wrapper type
    *           for
    * @return the resulting wrapper type for any primitive type or the input
    *         type for any non-primitive type
    */
   public static Class<?> getWrapperType(final Class<?> type)
   {
      if (!type.isPrimitive())
      {
         return type;
      }

      if (Integer.TYPE.equals(type))
      {
         return Integer.class;
      }
      else if (Boolean.TYPE.equals(type))
      {
         return Boolean.class;
      }
      else if (Long.TYPE.equals(type))
      {
         return Long.class;
      }
      else if (Character.TYPE.equals(type))
      {
         return Character.class;
      }
      else if (Double.TYPE.equals(type))
      {
         return Double.class;
      }
      else if (Byte.TYPE.equals(type))
      {
         return Byte.class;
      }
      else if (Float.TYPE.equals(type))
      {
         return Float.class;
      }
      else if (Short.TYPE.equals(type))
      {
         return Short.class;
      }
      else
      {
         return type;
      }
   }

   /**
    * Checks if a given {@link Method} is a valid bean <b>is</b> method.
    * <p>
    * Note that this implementation does not take care about possibly thrown
    * {@link Exception}s.
    * 
    * @param m
    *           the {@link Method} to check
    * @return if the {@link Method} is a valid bean <b>is</b> method
    */
   public static boolean isIsMethod(final Method m)
   {
      final String methodName = m.getName();

      return m.getParameterTypes().length == 0 &&
            Modifier.isPublic(m.getModifiers()) &&
            methodName.length() > 2 &&
            methodName.startsWith("is") &&
            (m.getReturnType().equals(Boolean.TYPE) ||
            m.getReturnType().equals(Boolean.class));
   }

   /**
    * Checks if a given {@link Method} is a valid bean <b>get</b> method.
    * <p>
    * Note that this implementation does not take care about possibly thrown
    * {@link Exception}s.
    * 
    * @param m
    *           the {@link Method} to check
    * @return if the {@link Method} is a valid bean <b>get</b> method
    */
   public static boolean isGetMethod(final Method m)
   {
      final String methodName = m.getName();

      return m.getParameterTypes().length == 0 &&
            Modifier.isPublic(m.getModifiers()) &&
            methodName.length() > 3 &&
            methodName.startsWith("get") &&
            !m.getReturnType().equals(Void.TYPE) &&
            !"getClass".equals(methodName);
   }
}
