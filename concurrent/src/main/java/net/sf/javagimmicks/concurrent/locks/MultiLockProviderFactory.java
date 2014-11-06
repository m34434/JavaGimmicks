package net.sf.javagimmicks.concurrent.locks;

import java.util.HashMap;
import java.util.TreeMap;

import net.sf.javagimmicks.util.Supplier;

/**
 * Serves as central {@link Supplier} for {@link MultiLockProvider} instances.
 * Instances can only be retrieved by the static getters
 * {@link #getHashBasedInstance()} and {@link #getTreeBasedInstance()}.
 * 
 * @param <K>
 *           The type of the internally used resource identifiers
 */
public class MultiLockProviderFactory<K> implements Supplier<MultiLockProvider<K>>
{
   private static final MultiLockProviderFactory<Object> HASH_INSTANCE = new MultiLockProviderFactory<Object>(
         new HashLockRegistrySupplier<Object>());

   private static final MultiLockProviderFactory<Object> TREE_INSTANCE = new MultiLockProviderFactory<Object>(
         new TreeLockRegistrySupplier<Object>());

   /**
    * Returns the hash-based {@link MultiLockProviderFactory} instance which
    * manages resource identifiers using {@link HashMap}s.
    * 
    * @return the hash-bases instance
    */
   @SuppressWarnings("unchecked")
   public static <K> MultiLockProviderFactory<K> getHashBasedInstance()
   {
      return (MultiLockProviderFactory<K>) HASH_INSTANCE;
   }

   /**
    * Returns the tree-based {@link MultiLockProviderFactory} instance which
    * manages resource identifiers using {@link TreeMap}s.
    * 
    * @return the hash-bases instance
    */
   @SuppressWarnings("unchecked")
   public static <K> MultiLockProviderFactory<K> getTreeBasedInstance()
   {
      return (MultiLockProviderFactory<K>) TREE_INSTANCE;
   }

   private final Supplier<LockRegistry<K>> _registrySupplier;

   private MultiLockProviderFactory(final Supplier<LockRegistry<K>> registrySupplier)
   {
      _registrySupplier = registrySupplier;
   }

   @Override
   public MultiLockProvider<K> get()
   {
      return new RegistryLockProvider<K>(_registrySupplier.get());
   }

   private static class HashLockRegistrySupplier<K> implements Supplier<LockRegistry<K>>
   {
      @Override
      public LockRegistry<K> get()
      {
         return DefaultLockRegistry.createHashBasedInstance();
      }
   }

   private static class TreeLockRegistrySupplier<K> implements Supplier<LockRegistry<K>>
   {
      @Override
      public LockRegistry<K> get()
      {
         return DefaultLockRegistry.createTreeBasedInstance();
      }
   }
}
