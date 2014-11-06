package net.sf.javagimmicks.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * An implementation of {@link Supplier} that creates instances by calling a
 * constructor on a given {@link Class} via reflection
 * 
 * @param <E>
 *           the type of the instances to create
 */
public class ReflectionSupplier<E> implements Supplier<E>
{
   protected final Constructor<? extends E> _constructor;
   protected final Supplier<Object[]> _argSupplier;

   /**
    * Configures this {@link ReflectionSupplier} to create instances by calling
    * the given {@link Constructor} taking the constructor arguments from the
    * given argument {@link Supplier}. The constructor must be public, must
    * belong to a non-abstract class and may not throw checked {@link Exception
    * Exceptions}.
    * 
    * @param constructor
    *           the {@link Constructor} to invoke to create new instances
    * @param argSupplier
    *           the {@link Supplier} to invoke to get the constructor arguments
    * @throws IllegalArgumentException
    *            if the given {@link Constructor} is not public, throws any
    *            checked {@link Exception} or belongs to an abstract class
    * @throws SecurityException
    *            if the given {@link Constructor} cannot be set accessible
    */
   public ReflectionSupplier(final Constructor<? extends E> constructor, final Supplier<Object[]> argSupplier)
         throws IllegalArgumentException, SecurityException
   {
      final Class<? extends E> declaringClass = constructor.getDeclaringClass();

      if (Modifier.isAbstract(declaringClass.getModifiers()))
      {
         throw new IllegalArgumentException("The given contructor belongs to an abstract class: "
               + declaringClass.getName());
      }

      if (!Modifier.isPublic(constructor.getModifiers()))
      {
         throw new IllegalArgumentException("Contructor must be public and accessible!");
      }

      // Make the constructor accessible to ensure later, that there is not
      // IllegalAccessException
      if (!constructor.isAccessible())
      {
         constructor.setAccessible(true);
      }

      for (final Class<?> exceptionClass : constructor.getExceptionTypes())
      {
         if (!RuntimeException.class.isAssignableFrom(exceptionClass) && !Error.class.isAssignableFrom(exceptionClass))
         {
            throw new IllegalArgumentException("The given constructor can throw an Exception of type " + exceptionClass
                  + " which is a checked Exception!");
         }
      }

      _constructor = constructor;
      _argSupplier = argSupplier != null ? argSupplier : EMPTY_ARG_SUPPLIER;
   }

   /**
    * Configures this {@link ReflectionSupplier} to create instances by calling
    * the {@link Constructor} identified by the given {@link Class} object and
    * list of constructor argument types on the given type taking the
    * constructor arguments from the given argument {@link Supplier}. The
    * constructor must be public, must belong to a non-abstract class and may
    * not throw checked {@link Exception}s.
    * 
    * @param type
    *           the {@link Class} object identifying the type to generate
    *           instances of
    * @param argTypes
    *           an array of {@link Class} objects identifying the parameter
    *           types of the constructor to find on the given type
    * @param argSupplier
    *           the {@link Supplier} to invoke to get the constructor arguments
    * @throws IllegalArgumentException
    *            if the given {@link Constructor} is not public, throws any
    *            checked {@link Exception} or belongs to an abstract class
    * @throws NoSuchMethodException
    *            if there is no matching {@link Constructor} on the given class
    * @throws SecurityException
    *            if access to the identified {@link Constructor} is secured via
    *            {@link SecurityManager}
    */
   public ReflectionSupplier(final Class<? extends E> type, final Class<?>[] argTypes,
         final Supplier<Object[]> argSupplier)
         throws IllegalArgumentException, SecurityException, NoSuchMethodException
   {
      this(type.getConstructor(argTypes), argSupplier);
   }

   /**
    * Configures this {@link ReflectionSupplier} to create instances by calling
    * the default {@link Constructor} on the given type The constructor must be
    * public and accessible, must belong to a non-abstract class and may not
    * throw checked {@link Exception}s.
    * 
    * @param type
    *           the {@link Class} object identifying the type to generate
    *           instances of
    * @throws IllegalArgumentException
    *            if the default {@link Constructor} is not public, throws any
    *            checked {@link Exception} or belongs to an abstract class
    * @throws NoSuchMethodException
    *            if there is no default {@link Constructor} on the given class
    * @throws SecurityException
    *            if access to the default {@link Constructor} is secured via
    *            {@link SecurityManager}
    */
   public ReflectionSupplier(final Class<? extends E> type) throws IllegalArgumentException, SecurityException,
         NoSuchMethodException
   {
      this(type, new Class<?>[0], null);
   }

   @Override
   public final E get()
   {
      try
      {
         return _constructor.newInstance(_argSupplier.get());
      }

      // Should not occur since we ensured in the constructor that the called
      // constructor is public
      catch (final IllegalAccessException e)
      {
         throw new IllegalStateException("The invoked clone() method is not accessible", e);
      }

      // Should not occur since we ensured in our constructor that the called
      // constructor belongs to a non-abstract class
      catch (final InstantiationException e)
      {
         return null;
      }

      // The called constructor threw something - this can only be a
      // RuntimeException or an Error
      catch (final InvocationTargetException e)
      {
         final Throwable originalThrowable = e.getTargetException();

         if (originalThrowable instanceof Error)
         {
            throw (Error) originalThrowable;
         }
         else if (originalThrowable instanceof RuntimeException)
         {
            throw (RuntimeException) originalThrowable;
         }

         // Normally - we cannot get here, since we checked the thrown checked
         // Exceptions in our constructor
         else
         {
            return null;
         }
      }
   }

   private static final Supplier<Object[]> EMPTY_ARG_SUPPLIER = new Supplier<Object[]>()
   {
      @Override
      public Object[] get()
      {
         return new Object[0];
      }
   };
}
