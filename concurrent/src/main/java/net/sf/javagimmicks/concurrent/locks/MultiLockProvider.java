package net.sf.javagimmicks.concurrent.locks;

/**
 * This is the entry point to the {@link MultiLock} API - it creates and manages
 * {@link MultiReadWriteLock} instances and the internal resource identifiers
 * used to handle for acquisition and release management.
 * 
 * @param <K>
 *           The type of the internally used resource identifiers
 */
public interface MultiLockProvider<K>
{
   /**
    * Creates a new {@link MultiReadWriteLock} that is associated with the given
    * resource identifiers.
    * 
    * @param resourceIds
    *           the resource identifiers to associate the new
    *           {@link MultiReadWriteLock} with
    * @return the new {@link MultiReadWriteLock} instance
    */
   MultiReadWriteLock<K> newLock(Iterable<K> resourceIds);

   /**
    * Creates a new {@link MultiReadWriteLock} that is associated with the given
    * resource identifiers.
    * 
    * @param resourceIds
    *           the resource identifiers to associate the new
    *           {@link MultiReadWriteLock} with
    * @return the new {@link MultiReadWriteLock} instance
    */
   MultiReadWriteLock<K> newLock(K... resourceIds);
}
