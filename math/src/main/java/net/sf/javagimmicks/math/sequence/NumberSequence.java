package net.sf.javagimmicks.math.sequence;

import java.math.BigInteger;

/**
 * A generic interface for {@link Number} sequences.
 * 
 * @param <N>
 *           the type of {@link Number}s that this {@link NumberSequence}
 *           generates.
 */
public interface NumberSequence<N extends Number>
{
   /**
    * Returns the {@link Number} at the given index
    * 
    * @param index
    *           the index of the {@link Number} to retrieve from this instance
    * @return the resulting {@link Number}
    * @throws IndexOutOfBoundsException
    *            if the given index is invalid for this {@link NumberSequence}
    */
   N get(BigInteger index) throws IndexOutOfBoundsException;
}
