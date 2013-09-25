package net.sf.javagimmicks.concurrent.locks;

import java.util.Collection;

public interface MultiLockProvider<K>
{
   MultiReadWriteLock<K> newLock(Collection<K> resources);
}
