package net.sf.javagimmicks.concurrent.locks;


public class MultiLockProviderFactory
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
