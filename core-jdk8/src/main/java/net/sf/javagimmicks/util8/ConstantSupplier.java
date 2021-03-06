package net.sf.javagimmicks.util8;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * An implementation of {@link Supplier} which holds a constant object of the
 * required type and returns it upon every call to {@link #get()}
 */
public class ConstantSupplier<E> implements Supplier<E>
{
   private final E _value;

   /**
    * Create a new instance for the given constant value
    * 
    * @param value
    *           the value to use for every creation call
    */
   public ConstantSupplier(final E value)
   {
      _value = value;
   }

   @Override
   public E get()
   {
      return _value;
   }

   @Override
   public String toString()
   {
      return Objects.toString(get());
   }
}
