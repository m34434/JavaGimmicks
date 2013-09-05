package net.sf.javagimmicks.concurrent;

import java.util.Collection;

public interface MultiLockProvider<K>
{
   MultiReadWriteLock<K> newLock(Collection<K> resources);
}
