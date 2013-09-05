package net.sf.javagimmicks.concurrent.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import net.sf.javagimmicks.lang.Factory;

public class DefaultLockRegistry<K> implements LockRegistry<K>, Serializable
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

   public DefaultLockRegistry(Map<K, Thread> exRegistry, Map<K, Collection<Thread>> shRegistry,
         Factory<Collection<Thread>> sharedCollectionFactory)
   {
      _exRegistry = exRegistry;
      _shRegistry = shRegistry;
      _shCollectionFactory = sharedCollectionFactory;
   }
   
   public boolean isSharedFree(Collection<K> resources)
   {
      for (K resource : resources)
      {
         if (_shRegistry.containsKey(resource))
         {
            return false;
         }
      }

      return true;
   }

   public void registerShared(Collection<K> resources)
   {
      Thread currentThread = Thread.currentThread();

      for (K resource : resources)
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

   public void unregisterShared(Collection<K> resources)
   {
      Thread currentThread = Thread.currentThread();

      for (K resource : resources)
      {
         Collection<Thread> threadsForResource = _shRegistry.get(resource);
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

   public boolean isExclusiveFree(Collection<K> resources)
   {
      for (K resource : resources)
      {
         if (_exRegistry.containsKey(resource))
         {
            return false;
         }
      }

      return true;
   }

   public void registerExclusive(Collection<K> resources)
   {
      Thread currentThread = Thread.currentThread();

      for (K resource : resources)
      {
         _exRegistry.put(resource, currentThread);
      }
   }

   public void unregisterExclusive(Collection<K> resources)
   {
      for (K resource : resources)
      {
         _exRegistry.remove(resource);
      }
   }
   
   private static final class HashSetSharedCollectionFactory implements Factory<Collection<Thread>>, Serializable
   {
      private static final long serialVersionUID = 3613642441786733902L;

      public Collection<Thread> create()
      {
         return new HashSet<Thread>();
      }
   }
}
