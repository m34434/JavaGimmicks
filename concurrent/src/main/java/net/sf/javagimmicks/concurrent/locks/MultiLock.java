package net.sf.javagimmicks.concurrent.locks;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

public interface MultiLock<K> extends Lock 
{
   public Collection<K> getResources();
   public boolean isLockedByThisThread();
}
