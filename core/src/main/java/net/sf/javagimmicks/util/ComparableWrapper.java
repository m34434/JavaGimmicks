package net.sf.javagimmicks.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;

/**
 * This class adds {@link Comparable} abilities to a given non-
 * {@link Comparable} object by decorating it with a dynamic proxy.
 * <p>
 * As usual for dynamic proxies, the decorated object must implement an
 * interface. It is also necessary to write an extension interface to the
 * decorated one which additionally extends {@link Comparable}. A
 * {@link ComparableWrapper} can only create the proxy object for that new
 * interface.
 * 
 * @param <T>
 *           the (non-{@link Comparable}) type of objects to wrap
 * @param <C>
 *           the ({@link Comparable}) type of proxies to create
 */
public class ComparableWrapper<T, C extends Comparable<C>>
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

   private final Class<C> _proxyClass;
   private final Constructor<C> _proxyConstructor;

   private final Class<T> _nonComparableType;

   private final Comparator<T> _comparator;

   /**
    * Creates a new {@link ComparableWrapper} instance which can be reused to
    * create {@link Comparable} proxies for any delegate objects implementing
    * the provided non-{@link Comparable} interface.
    * 
    * @param classLoader
    *           the {@link ClassLoader} to use for creating proxy instances
    * @param nonComparableType
    *           an interface type that delegate objects to wrap must implement
    * @param comparableType
    *           the proxy interface the extends {@link Comparable} and the
    *           original delegate interface
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @param <C>
    *           the ({@link Comparable}) type of proxies to create
    * @return the resulting {@link ComparableWrapper}
    */
   @SuppressWarnings("unchecked")
   public static <T, C extends Comparable<C>> ComparableWrapper<T, C> create(final ClassLoader classLoader,
         final Class<T> nonComparableType,
         final Class<C> comparableType,
         final Comparator<T> comparator)
   {
      if (!nonComparableType.isAssignableFrom(comparableType))
      {
         throw new IllegalArgumentException(String.format(
               "Comparable class '%1$s' must extend the given non-comparable type '%2$s'!", comparableType,
               nonComparableType));
      }

      final Class<C> proxyClass = (Class<C>) Proxy.getProxyClass(classLoader, comparableType);
      final Constructor<C> proxyConstructor = getConstructor(proxyClass);

      return new ComparableWrapper<T, C>(nonComparableType,
            proxyClass, proxyConstructor, comparator);
   }

   /**
    * Creates a new {@link ComparableWrapper} instance which can be reused to
    * create {@link Comparable} proxies for any delegate objects implementing
    * the provided non-{@link Comparable} interface.
    * 
    * @param nonComparableType
    *           an interface type that delegate objects to wrap must implement
    * @param comparableType
    *           the proxy interface the extends {@link Comparable} and the
    *           original delegate interface
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @param <C>
    *           the ({@link Comparable}) type of proxies to create
    * @return the resulting {@link ComparableWrapper}
    */
   public static <T, C extends Comparable<C>> ComparableWrapper<T, C> create(final Class<T> nonComparableType,
         final Class<C> comparableType,
         final Comparator<T> comparator)
   {
      return create(comparableType.getClassLoader(), nonComparableType, comparableType, comparator);
   }

   private ComparableWrapper(final Class<T> nonComparableType, final Class<C> proxyClass,
         final Constructor<C> proxyConstructor,
         final Comparator<T> comparator)
   {
      _proxyClass = proxyClass;
      _proxyConstructor = proxyConstructor;

      _nonComparableType = nonComparableType;

      _comparator = comparator;
   }

   /**
    * Returns the {@link Class} that created proxy instances will have.
    * 
    * @return the {@link Class} that created proxy instances will have
    */
   public Class<C> getProxyClass()
   {
      return _proxyClass;
   }

   /**
    * Returns the {@link Comparator} that is internally used for comparing
    * within the created proxies.
    * 
    * @return the {@link Comparator} that is internally used for comparing
    *         within the created proxies
    */
   public Comparator<T> getComparator()
   {
      return _comparator;
   }

   /**
    * Decorates a given {@code T} instance with a proxy of type {@code C}.
    * 
    * @param delegate
    *           the delegate object to decorate
    * @return the resulting proxy of type {@code C}
    */
   public C wrap(final T delegate)
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("Delegate is null!");
      }
      else if (!_nonComparableType.isAssignableFrom(delegate.getClass()))
      {
         throw new IllegalArgumentException(
               String.format(
                     "Delegate object of type '%1$s' does not extend the specified non-comparable interface '%2$s'!",
                     delegate.getClass(),
                     _nonComparableType));
      }

      final ComparableInvocationHandler invocationHandler = new ComparableInvocationHandler(delegate);

      try
      {
         return _proxyConstructor.newInstance(invocationHandler);
      }
      catch (final Exception ignore)
      {
         return null;
      }
   }

   private static <C extends Comparable<C>> Constructor<C> getConstructor(final Class<C> proxyClass)
   {
      try
      {
         return proxyClass.getConstructor(InvocationHandler.class);
      }
      catch (final Exception ex)
      {
         return null;
      }
   }

   protected class ComparableInvocationHandler implements InvocationHandler
   {
      protected final T _delegate;

      public ComparableInvocationHandler(final T delegate)
      {
         _delegate = delegate;
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
