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
 * interface which must also be provided with the construction methods.
 * <p>
 * It is also recommended to write an own extension interface to the decorated
 * one which additionally extends {@link Comparable}. See the following example:
 * 
 * <pre>
 * private static interface StringWrapper
 * {
 *    String get();
 * }
 * 
 * private static interface StringWrapperComparable extends StringWrapper, Comparable&lt;StringWrapperComparable&gt;
 * {}
 * </pre>
 * 
 * Then you can use {@link ComparableWrapper} to create dynamic proxies for the
 * {@link Comparable} extension interface:
 * 
 * <pre>
 * final ComparableWrapper&lt;StringWrapper, StringWrapperComparable&gt; wrapper =
 *       ComparableWrapper.create(StringWrapper.class, StringWrapperComparable.class, COMPARATOR);
 * 
 * final StringWrapperComparable w1 = wrapper.wrap(new StringWrapperImpl(&quot;1&quot;));
 * final StringWrapperComparable w2 = wrapper.wrap(new StringWrapperImpl(&quot;2&quot;));
 * final StringWrapperComparable w3 = wrapper.wrap(new StringWrapperImpl(&quot;3&quot;));
 * </pre>
 * 
 * <p>
 * However, if you do not provide such an interface, {@link ComparableWrapper}
 * can also create proxies which will implement {@link Comparable} in addition
 * to the provided delegate interface, but then you have to take care about
 * casting to {@link Comparable} by your own:
 * 
 * <pre>
 * final ComparableWrapper&lt;StringWrapper, StringWrapper&gt; wrapper =
 *       ComparableWrapper.create(StringWrapper.class, COMPARATOR);
 * 
 * final StringWrapper w1 = wrapper.wrap(new StringWrapperImpl(&quot;1&quot;));
 * final StringWrapper w2 = wrapper.wrap(new StringWrapperImpl(&quot;2&quot;));
 * final StringWrapper w3 = wrapper.wrap(new StringWrapperImpl(&quot;3&quot;));
 * 
 * assertTrue(((Comparable&lt;StringWrapper&gt;) w1).compareTo(w2) &lt; 0);
 * </pre>
 * 
 * @param <T>
 *           the (non-{@link Comparable}) type of objects to wrap
 * @param <C>
 *           the type of proxies to create
 */
public class ComparableWrapper<T, C>
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
    *           the proxy interface that must extend {@link Comparable} and the
    *           non-{@link Comparable} delegate interface
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @param <C>
    *           the ({@link Comparable}) type of proxies to create
    * @return the resulting {@link ComparableWrapper}
    * @throws IllegalArgumentException
    *            if {@code comparableType} does not extend
    *            {@code nonComparableType} or it does not follow dynamic proxy
    *            prerequisites (see
    *            {@link Proxy#getProxyClass(ClassLoader, Class...)})
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

      return createInternal(nonComparableType, proxyClass, comparator);
   }

   /**
    * Creates a new {@link ComparableWrapper} instance which can be reused to
    * create {@link Comparable} proxies for any delegate objects implementing
    * the provided non-{@link Comparable} interface.
    * 
    * @param nonComparableType
    *           an interface type that delegate objects to wrap must implement
    * @param comparableType
    *           the proxy interface that must extend {@link Comparable} and the
    *           non-{@link Comparable} delegate interface
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @param <C>
    *           the ({@link Comparable}) type of proxies to create
    * @return the resulting {@link ComparableWrapper}
    * @throws IllegalArgumentException
    *            if {@code comparableType} does not extend
    *            {@code nonComparableType} or it does not follow dynamic proxy
    *            prerequisites (see
    *            {@link Proxy#getProxyClass(ClassLoader, Class...)})
    */
   public static <T, C extends Comparable<C>> ComparableWrapper<T, C> create(final Class<T> nonComparableType,
         final Class<C> comparableType,
         final Comparator<T> comparator)
   {
      return create(comparableType.getClassLoader(), nonComparableType, comparableType, comparator);
   }

   /**
    * Creates a new {@link ComparableWrapper} instance which can be reused to
    * create {@link Comparable} proxies for any delegate objects implementing
    * the provided non-{@link Comparable} interface - returned proxies will
    * always implement the {@link Comparable} interface in addition to the
    * provided one.
    * 
    * @param classLoader
    *           the {@link ClassLoader} to use for creating proxy instances
    * @param nonComparableType
    *           an interface type that delegate objects to wrap must implement
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @return the resulting {@link ComparableWrapper}
    * @throws IllegalArgumentException
    *            if {@code comparableType} does not follow dynamic proxy
    *            prerequisites (see
    *            {@link Proxy#getProxyClass(ClassLoader, Class...)})
    */
   @SuppressWarnings("unchecked")
   public static <T> ComparableWrapper<T, T> create(final ClassLoader classLoader,
         final Class<T> nonComparableType,
         final Comparator<T> comparator)
   {
      return createInternal(nonComparableType,
            (Class<T>) Proxy.getProxyClass(classLoader, new Class[] { nonComparableType, Comparable.class }),
            comparator);
   }

   /**
    * Creates a new {@link ComparableWrapper} instance which can be reused to
    * create {@link Comparable} proxies for any delegate objects implementing
    * the provided non-{@link Comparable} interface - returned proxies will
    * always implement the {@link Comparable} interface in addition to the
    * provided one.
    * 
    * @param nonComparableType
    *           an interface type that delegate objects to wrap must implement
    * @param comparator
    *           the {@link Comparator} to use for creating proxies
    * @param <T>
    *           the (non-{@link Comparable}) type of objects to wrap
    * @return the resulting {@link ComparableWrapper}
    * @throws IllegalArgumentException
    *            if {@code comparableType} does not follow dynamic proxy
    *            prerequisites (see
    *            {@link Proxy#getProxyClass(ClassLoader, Class...)})
    */
   public static <T> ComparableWrapper<T, T> create(final Class<T> nonComparableType,
         final Comparator<T> comparator)
   {
      return create(nonComparableType.getClassLoader(), nonComparableType, comparator);
   }

   private static <T, C> ComparableWrapper<T, C> createInternal(final Class<T> nonComparableType,
         final Class<C> proxyClass,
         final Comparator<T> comparator)
   {
      final Constructor<C> proxyConstructor = getConstructor(proxyClass);

      return new ComparableWrapper<T, C>(nonComparableType,
            proxyClass, proxyConstructor, comparator);
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

   private static <C> Constructor<C> getConstructor(final Class<C> proxyClass)
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
