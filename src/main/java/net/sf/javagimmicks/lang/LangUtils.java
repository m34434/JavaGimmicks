package net.sf.javagimmicks.lang;

public class LangUtils
{  
   public static boolean equalsNullSafe(Object o1, Object o2)
   {
      return (o1 == o2) || (o1 != null && o1.equals(o2));
   }
   
   public static int hashCodeNullSafe(Object o)
   {
      return o != null ? o.hashCode() : 0;
   }
}
