package net.sf.javagimmicks.concurrent;

import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;

public interface MultiReadWriteLock<K> extends ReadWriteLock
{
   Collection<K> getResources();
   
   MultiLock<K> readLock();
   MultiLock<K> writeLock();
}
