package net.sf.javagimmicks.concurrent.locks;

import java.util.HashMap;
import java.util.TreeMap;

import net.sf.javagimmicks.lang.Factory;

/**
 * Serves as central {@link Factory} for {@link MultiLockProvider} instances.
 * Instances can only be retrieved by the static getters
 * {@link #getHashBasedInstance()} and {@link #getTreeBasedInstance()}.
 * 
 * @param <K>
 *           The type of the internally used resource identifiers
 */
public class MultiLockProviderFactory<K> implements Factory<MultiLockProvider<K>>
{
   private static final MultiLockProviderFactory<Object> HASH_INSTANCE = new MultiLockProviderFactory<Object>(
         new HashLockRegistryFactory<Object>());

   private static final MultiLockProviderFactory<Object> TREE_INSTANCE = new MultiLockProviderFactory<Object>(
         new TreeLockRegistryFactory<Object>());

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

   private final Factory<LockRegistry<K>> _registryFactory;

   private MultiLockProviderFactory(final Factory<LockRegistry<K>> registryFactory)
   {
      _registryFactory = registryFactory;
   }

   @Override
   public MultiLockProvider<K> create()
   {
      return new RegistryLockProvider<K>(_registryFactory.create());
   }

   private static class HashLockRegistryFactory<K> implements Factory<LockRegistry<K>>
   {
      @Override
      public LockRegistry<K> create()
      {
         return DefaultLockRegistry.createHashBasedInstance();
      }
   }

   private static class TreeLockRegistryFactory<K> implements Factory<LockRegistry<K>>
   {
      @Override
      public LockRegistry<K> create()
      {
         return DefaultLockRegistry.createTreeBasedInstance();
      }
   }
}
