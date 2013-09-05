package net.sf.javagimmicks.concurrent.impl;

import net.sf.javagimmicks.concurrent.MultiLockProvider;

public class DefaultLockProviderFactory
{
   public static <K> MultiLockProvider<K> createHashBasedInstance()
   {
      LockRegistry<K> registry = DefaultLockRegistry.createHashBasedInstance();
      return new RegistryLockProvider<K>(registry);
   }

   public static <K> MultiLockProvider<K> createTreeBasedInstance()
   {
      LockRegistry<K> registry = DefaultLockRegistry.createTreeBasedInstance();
      return new RegistryLockProvider<K>(registry);
   }
}
