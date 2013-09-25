package net.sf.javagimmicks.concurrent.locks;

import java.util.Collection;

interface LockRegistry<K>
{
   boolean isSharedFree(Collection<K> resources);

   void registerShared(Collection<K> resources);

   void unregisterShared(Collection<K> resources);

   boolean isExclusiveFree(Collection<K> resources);

   void registerExclusive(Collection<K> resources);

   void unregisterExclusive(Collection<K> resources);
}
