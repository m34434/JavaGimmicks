package net.sf.javagimmicks.lang;

/**
 * Some basic utility methods - currently only for comparing objects in a
 * null-safe way.
 */
public class LangUtils
{
   public static boolean equalsNullSafe(final Object o1, final Object o2)
   {
      return (o1 == o2) || (o1 != null && o1.equals(o2));
   }

   public static int hashCodeNullSafe(final Object o)
   {
      return o != null ? o.hashCode() : 0;
   }
}
