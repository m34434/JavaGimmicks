package net.sf.javagimmicks.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

/**
 * An implementation of {@link Supplier} which creates instances following the
 * prototype pattern - i.e. clones a given prototype object.
 * 
 * @param <E>
 *           the type of the instances to create; must implement the
 *           {@link Cloneable} interface
 */
public class PrototypeSupplier<E extends Cloneable> implements Supplier<E>
{
   protected final E _prototype;
   protected final Method _cloneMethod;

   /**
    * Generates a new instance from the given prototype object. The prototype
    * must implement the {@link Cloneable} interface. Additionally it's
    * <code>clone()</code> method must be public
    * 
    * @param prototype
    *           the prototype object to clone when instances should be created
    */
   public PrototypeSupplier(final E prototype)
   {
      if (prototype == null)
      {
         throw new IllegalArgumentException("Prototype may not be null!");
      }

      _prototype = prototype;

      try
      {
         _cloneMethod = _prototype.getClass().getMethod("clone", new Class[0]);
      }
      catch (final NoSuchMethodException e)
      {
         throw new IllegalArgumentException("Cannot access clone() method of given prototype!");
      }

      // Make the method accessible to ensure later, that there is not
      // IllegalAccessException
      if (!_cloneMethod.isAccessible())
      {
         _cloneMethod.setAccessible(true);
      }

      if (!Modifier.isPublic(_cloneMethod.getModifiers()))
      {
         throw new IllegalArgumentException("clone() method of given prototype is not public!");
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public E get()
   {
      try
      {
         return (E) _cloneMethod.invoke(_prototype, new Object[0]);
      }

      // Should not occur since we ensured in the constructor that the clone()
      // method is public
      catch (final IllegalAccessException e)
      {
         throw new IllegalStateException("The invoked clone() method is not accessible", e);
      }

      // The clone() method of the prototype threw something - this can only be
      // a RuntimeException or an Error
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

         // Normally - we cannot get here
         else
         {
            return null;
         }
      }
   }
}
