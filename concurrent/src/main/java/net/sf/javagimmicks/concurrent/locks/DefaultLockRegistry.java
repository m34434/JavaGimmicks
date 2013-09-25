package net.sf.javagimmicks.concurrent.locks;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.lang.Factory;

class DefaultLockRegistry<K> implements LockRegistry<K>, Serializable
{
   private static final long serialVersionUID = -785304313135882910L;

   private static final Factory<Collection<Thread>> HASHSET_SHARED_COLLECTION_FACTORY = new HashSetSharedCollectionFactory();

   public static <K> DefaultLockRegistry<K> createHashBasedInstance()
   {
      return new DefaultLockRegistry<K>(new HashMap<K, Thread>(), new HashMap<K, Collection<Thread>>(),
            HASHSET_SHARED_COLLECTION_FACTORY);
   }

   public static <K> DefaultLockRegistry<K> createTreeBasedInstance()
   {
      return new DefaultLockRegistry<K>(new TreeMap<K, Thread>(), new TreeMap<K, Collection<Thread>>(),
            HASHSET_SHARED_COLLECTION_FACTORY);
   }

   protected final Map<K, Thread> _exRegistry;
   protected final Map<K, Collection<Thread>> _shRegistry;
   protected final Factory<Collection<Thread>> _shCollectionFactory;

   public DefaultLockRegistry(final Map<K, Thread> exRegistry, final Map<K, Collection<Thread>> shRegistry,
         final Factory<Collection<Thread>> sharedCollectionFactory)
   {
      _exRegistry = exRegistry;
      _shRegistry = shRegistry;
      _shCollectionFactory = sharedCollectionFactory;
   }

   @Override
   public boolean isSharedFree(final Collection<K> resources)
   {
      for (final K resource : resources)
      {
         if (_shRegistry.containsKey(resource))
         {
            return false;
         }
      }

      return true;
   }

   @Override
   public void registerShared(final Collection<K> resources)
   {
      final Thread currentThread = Thread.currentThread();

      for (final K resource : resources)
      {
         Collection<Thread> threadsForResource = _shRegistry.get(resource);
         if (threadsForResource == null)
         {
            threadsForResource = _shCollectionFactory.create();
            _shRegistry.put(resource, threadsForResource);
         }

         threadsForResource.add(currentThread);
      }
   }

   @Override
   public void unregisterShared(final Collection<K> resources)
   {
      final Thread currentThread = Thread.currentThread();

      for (final K resource : resources)
      {
         final Collection<Thread> threadsForResource = _shRegistry.get(resource);
         if (threadsForResource == null)
         {
            continue;
         }

         threadsForResource.remove(currentThread);

         if (threadsForResource.isEmpty())
         {
            _shRegistry.remove(resource);
         }
      }
   }

   @Override
   public boolean isExclusiveFree(final Collection<K> resources)
   {
      for (final K resource : resources)
      {
         if (_exRegistry.containsKey(resource))
         {
            return false;
         }
      }

      return true;
   }

   @Override
   public void registerExclusive(final Collection<K> resources)
   {
      final Thread currentThread = Thread.currentThread();

      for (final K resource : resources)
      {
         _exRegistry.put(resource, currentThread);
      }
   }

   @Override
   public void unregisterExclusive(final Collection<K> resources)
   {
      for (final K resource : resources)
      {
         _exRegistry.remove(resource);
      }
   }

   private static final class HashSetSharedCollectionFactory implements Factory<Collection<Thread>>, Serializable
   {
      private static final long serialVersionUID = 3613642441786733902L;

      @Override
      public Collection<Thread> create()
      {
         return new HashSet<Thread>();
      }
   }
}
