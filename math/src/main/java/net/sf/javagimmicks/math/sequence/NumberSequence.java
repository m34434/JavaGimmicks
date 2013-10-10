package net.sf.javagimmicks.math.sequence;

import java.math.BigInteger;

public interface NumberSequence<N extends Number>
{
   N get(BigInteger index);
}
