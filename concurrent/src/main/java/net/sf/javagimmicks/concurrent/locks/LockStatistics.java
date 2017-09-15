package net.sf.javagimmicks.concurrent.locks;

import java.io.IOException;
import java.io.Writer;

/**
 * Provides statistical (and other) information about currently held locks.
 * 
 * @param <K>
 *           The type of the internally used resource identifiers
 */
public interface LockStatistics<K>
{
   /**
    * Dumps detailed information about the internally held locks into the give
    * {@link Writer}.
    * 
    * @param w
    *           the {@link Writer} to dump into
    */
   void dump(Writer w) throws IOException;
}