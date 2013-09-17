package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import net.sf.javagimmicks.lang.Factory;

public class InjectionFactory<E> implements Factory<E>
{
   private final Class<? extends E> _class;

   @Inject
   private Instance<Object> _instances;

   public InjectionFactory(final Class<? extends E> clazz)
   {
      _class = clazz;
   }

   @Override
   public final E create()
   {
      final Class<? extends E> clazz;
      final Set<Annotation> annotations = new HashSet<Annotation>();

      synchronized (this)
      {
         clazz = getType();
         addAnnotations(annotations);
      }

      final Instance<? extends E> instance = getInstances().select(clazz, annotations.toArray(new Annotation[0]));

      return select(instance);
   }

   protected Class<? extends E> getType()
   {
      return _class;
   }

   protected void addAnnotations(final Set<Annotation> annotations)
   {}

   protected E select(final Instance<? extends E> instance)
   {
      return instance.get();
   }

   private Instance<Object> getInstances()
   {
      return _instances;
   }
}
