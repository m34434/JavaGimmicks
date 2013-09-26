package net.sf.javagimmicks.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;

/**
 * This class add {@link Comparable} abilities to a given non-{@link Comparable}
 * object (which must implement an interface) by decorating the interface type
 * with a dynamic proxy.
 * <p>
 * <b>Note that the developer must take care about writing an extension
 * interface to the actual one which extends {@link Comparable}. This class can
 * only create the proxy object for that new interface.
 */
public class ComparableWrapper
{
   private static final Method COMPARE_TO_METHOD;

   static
   {
      Method method = null;
      try
      {
         method = Comparable.class.getMethod("compareTo", new Class[] { Object.class });
      }
      catch (final Exception ignore)
      {
         // Cannot occur
      }

      COMPARE_TO_METHOD = method;
   }

   private ComparableWrapper()
   {}

   /**
    * Decorates a given non-{@link Comparable} with {@link Comparable} abilities
    * using the given {@link Comparator}.
    * 
    * @param delegate
    *           the delegate object to decorate
    * @param nonComparableClass
    *           an interface type that the delegate object implements
    * @param comparableClass
    *           the proxy interface the extends {@link Comparable} and the
    *           original delegate interface
    * @param comparator
    *           the {@link Comparator} to use for creating the proxy
    * @return the result proxy implementing the given {@link Comparable}
    *         interface
    */
   @SuppressWarnings("unchecked")
   public static <T, C extends Comparable<C>> C wrap(final T delegate, final Class<T> nonComparableClass,
         final Class<C> comparableClass,
         final Comparator<T> comparator)
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("Delegate is null!");
      }
      else if (!nonComparableClass.isAssignableFrom(comparableClass))
      {
         throw new IllegalArgumentException(String.format(
               "Comparable class '%1$s' must extend the given non-comparable type '%2$s'!", comparableClass,
               nonComparableClass));
      }
      else if (!nonComparableClass.isAssignableFrom(delegate.getClass()))
      {
         throw new IllegalArgumentException(
               String.format(
                     "Class of delegate object '%1$s' must extend the specified non-comparable type '%2$s'!",
                     delegate.getClass(),
                     nonComparableClass));
      }

      return (C) Proxy.newProxyInstance(comparableClass.getClassLoader(), new Class[] {
            comparableClass }, new ComparableInvocationHandler<T>(delegate, comparator));
   }

   protected static class ComparableInvocationHandler<T> implements InvocationHandler
   {
      protected final T _delegate;
      protected final Comparator<T> _comparator;

      public ComparableInvocationHandler(final T delegate, final Comparator<T> comparator)
      {
         _delegate = delegate;
         _comparator = comparator;
      }

      @SuppressWarnings("unchecked")
      @Override
      public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
      {
         if (COMPARE_TO_METHOD.equals(method))
         {
            return _comparator.compare(_delegate, (T) args[0]);
         }
         else
         {
            return method.invoke(_delegate, args);
         }
      }
   }
}
