package net.sf.javagimmicks.cdi;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Provides factory methods to create literal instances of {@link Annotation}
 * types - this is required for selecting CDI beans via non-annotation based
 * APIs.
 * <p>
 * Generated instances are thread-safe and can be reused.
 * <p>
 * Generated instances mostly work like {@link AnnotationLiteral} (and also take
 * much code of it) but does not require subclassing it in order to create a
 * literal. Instead a factory method/builder approach is done.
 * <p>
 * Internally works with {@link Proxy} and a generic {@link InvocationHandler}
 * 
 * @see AnnotationLiteral
 */
public class AnnotationLiteralHelper
{
   private AnnotationLiteralHelper()
   {}

   /**
    * Creates a new {@link Annotation} literal for the given type with the
    * member values specified as {@link Map}&lt;String, Object&gt;
    * 
    * @param annotationType
    *           the {@link Annotation} type to create a literal for
    * @param memberValues
    *           the member values of the literal to create
    * @throws IllegalArgumentException
    *            if there is no value specified for at least one member
    * @return the resulting {@link Annotation} literal instance
    */
   @SuppressWarnings("unchecked")
   public static <A extends Annotation> A annotation(final Class<A> annotationType,
         final Map<String, Object> memberValues)
   {
      return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), new Class<?>[] { annotationType },
            new AnnoationInvocationHandler<A>(annotationType, memberValues));
   }

   /**
    * Creates a new {@link Annotation} literal for the given type without any
    * member values
    * 
    * @param annotationType
    *           the {@link Class} of the {@link Annotation} type to create a
    *           literal for
    * @param <A>
    *           the {@link Annotation} type to create a literal for
    * @throws IllegalArgumentException
    *            if there is no value specified for at least one member
    * @return the resulting {@link Annotation} literal instance
    */
   public static <A extends Annotation> A annotation(final Class<A> annotationType)
   {
      return annotation(annotationType, Collections.<String, Object> emptyMap());
   }

   /**
    * Creates a builder (of type {@link Builder}) for the given
    * {@link Annotation} type that allows passing member values in a fluent way.
    * 
    * @param annotationType
    *           the {@link Class} of the {@link Annotation} type to create a
    *           literal for
    * @param <A>
    *           the {@link Annotation} type to create a literal for
    * @return a {@link Builder} for creating {@link Annotation} instances
    */
   public static <A extends Annotation> Builder<A> annotationWithMembers(final Class<A> annotationType)
   {
      return new Builder<A>(annotationType);
   }

   /**
    * A builder object for generating {@link Annotation} literals
    */
   public static class Builder<A extends Annotation>
   {
      private final Class<A> _class;
      private final Map<String, Object> _memberValues = new HashMap<String, Object>();

      private Builder(final Class<A> clazz)
      {
         _class = clazz;
      }

      /**
       * Adds a new annotation member value.
       * 
       * @param memberName
       *           the name of the member
       * @param value
       *           the value for the member
       * @return the {@link Builder} itself
       * @throws IllegalArgumentException
       *            if the given member name does not match a member of the
       *            underlying {@link Annotation}
       */
      public Builder<A> member(final String memberName, final Object value)
      {
         try
         {
            _class.getMethod(memberName);
         }
         catch (final NoSuchMethodException ex)
         {
            throw new IllegalArgumentException("'" + memberName + "' is not a member of " + _class);
         }

         _memberValues.put(memberName, value);

         return this;
      }

      /**
       * Creates the final {@link Annotation} literal
       * 
       * @throws IllegalArgumentException
       *            if there is no value specified for at least one member
       * @return the generated {@link Annotation} literal
       */
      public A get()
      {
         return annotation(_class, _memberValues);
      }
   }

   private static class AnnoationInvocationHandler<A extends Annotation> implements
         InvocationHandler, Serializable
   {
      private static final long serialVersionUID = -2288090370761073210L;

      private final Class<A> _class;
      private final Map<String, Object> _memberValues;
      private final Method[] _members;

      private final Integer _fixedHashCode;

      public AnnoationInvocationHandler(final Class<A> clazz, final Map<String, Object> memberValues)
      {
         _class = clazz;
         _members = clazz.getDeclaredMethods();
         _memberValues = memberValues;
         _fixedHashCode = _members.length == 0 ? 0 : null;

         for (final Method member : _members)
         {
            final String memberName = member.getName();

            if (!_memberValues.containsKey(memberName))
            {
               throw new IllegalArgumentException("No value specified for annotation member '" + memberName + "'!");
            }
         }
      }

      @Override
      public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
      {
         final String methodName = method.getName();
         final Class<?>[] typeParameters = method.getParameterTypes();
         final int typeParameterCount = typeParameters.length;

         if ("equals".equals(methodName) && typeParameterCount == 1
               && Object.class.equals(typeParameters[0]))
         {
            return isEquals(args[0]);
         }
         else if ("hashCode".equals(methodName) && typeParameterCount == 0)
         {
            return getHashCode();
         }
         else if ("annotationType".equals(methodName) && typeParameterCount == 0)
         {
            return _class;
         }
         else if ("toString".equals(methodName) && typeParameterCount == 0)
         {
            return _class.toString();
         }
         else
         {
            return null;
         }
      }

      private boolean isEquals(final Object other)
      {
         if (other == this)
         {
            return true;
         }
         if (other == null)
         {
            return false;
         }
         if (other instanceof Annotation)
         {
            final Annotation that = (Annotation) other;
            if (_class.equals(that.annotationType()))
            {
               for (final Method member : _members)
               {
                  final Object thisValue = _memberValues.get(member.getName());
                  final Object thatValue = getMemberValue(member, that);
                  if (thisValue instanceof byte[] && thatValue instanceof byte[])
                  {
                     if (!Arrays.equals((byte[]) thisValue, (byte[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof short[] && thatValue instanceof short[])
                  {
                     if (!Arrays.equals((short[]) thisValue, (short[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof int[] && thatValue instanceof int[])
                  {
                     if (!Arrays.equals((int[]) thisValue, (int[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof long[] && thatValue instanceof long[])
                  {
                     if (!Arrays.equals((long[]) thisValue, (long[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof float[] && thatValue instanceof float[])
                  {
                     if (!Arrays.equals((float[]) thisValue, (float[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof double[] && thatValue instanceof double[])
                  {
                     if (!Arrays.equals((double[]) thisValue, (double[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof char[] && thatValue instanceof char[])
                  {
                     if (!Arrays.equals((char[]) thisValue, (char[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof boolean[] && thatValue instanceof boolean[])
                  {
                     if (!Arrays.equals((boolean[]) thisValue, (boolean[]) thatValue))
                        return false;
                  }
                  else if (thisValue instanceof Object[] && thatValue instanceof Object[])
                  {
                     if (!Arrays.equals((Object[]) thisValue, (Object[]) thatValue))
                        return false;
                  }
                  else
                  {
                     if (!thisValue.equals(thatValue))
                        return false;
                  }
               }
               return true;
            }
         }
         return false;
      }

      private int getHashCode()
      {
         if (_fixedHashCode != null)
         {
            return _fixedHashCode;
         }
         else
         {
            int hashCode = 0;
            for (final Method member : _members)
            {
               final String memberName = member.getName();
               final int memberNameHashCode = 127 * memberName.hashCode();
               final Object value = _memberValues.get(memberName);
               int memberValueHashCode;
               if (value instanceof boolean[])
               {
                  memberValueHashCode = Arrays.hashCode((boolean[]) value);
               }
               else if (value instanceof short[])
               {
                  memberValueHashCode = Arrays.hashCode((short[]) value);
               }
               else if (value instanceof int[])
               {
                  memberValueHashCode = Arrays.hashCode((int[]) value);
               }
               else if (value instanceof long[])
               {
                  memberValueHashCode = Arrays.hashCode((long[]) value);
               }
               else if (value instanceof float[])
               {
                  memberValueHashCode = Arrays.hashCode((float[]) value);
               }
               else if (value instanceof double[])
               {
                  memberValueHashCode = Arrays.hashCode((double[]) value);
               }
               else if (value instanceof byte[])
               {
                  memberValueHashCode = Arrays.hashCode((byte[]) value);
               }
               else if (value instanceof char[])
               {
                  memberValueHashCode = Arrays.hashCode((char[]) value);
               }
               else if (value instanceof Object[])
               {
                  memberValueHashCode = Arrays.hashCode((Object[]) value);
               }
               else
               {
                  memberValueHashCode = value.hashCode();
               }
               hashCode += memberNameHashCode ^ memberValueHashCode;
            }
            return hashCode;
         }
      }

      private static Object getMemberValue(final Method member, final Annotation instance)
      {
         final Object value = invoke(member, instance);
         if (value == null)
         {
            throw new IllegalArgumentException("Annotation member value " + instance.getClass().getName() + "."
                  + member.getName() + " must not be null");
         }
         return value;
      }

      private static Object invoke(final Method method, final Object instance)
      {
         try
         {
            if (!method.isAccessible())
               method.setAccessible(true);
            return method.invoke(instance);
         }
         catch (final IllegalArgumentException e)
         {
            throw new RuntimeException("Error checking value of member method " + method.getName() + " on "
                  + method.getDeclaringClass(), e);
         }
         catch (final IllegalAccessException e)
         {
            throw new RuntimeException("Error checking value of member method " + method.getName() + " on "
                  + method.getDeclaringClass(), e);
         }
         catch (final InvocationTargetException e)
         {
            throw new RuntimeException("Error checking value of member method " + method.getName() + " on "
                  + method.getDeclaringClass(), e);
         }
      }
   }
}
