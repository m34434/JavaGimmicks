package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

public class Injection<E>
{
   private final Class<E> _class;
   private final List<Class<?>> _typeParameters;
   private final Set<Annotation> _annotations;

   private final String _name;

   public static <E> Builder<E> build(final BeanManager beanManager)
   {
      return new Builder<E>(beanManager);
   }

   public static <E> Builder<E> build()
   {
      return build(null);
   }

   public Injection(final Class<E> clazz, final List<Class<?>> typeParameters, final Collection<Annotation> annotations)
   {
      if (clazz == null)
      {
         throw new IllegalArgumentException("Class must be specified!");
      }

      _class = clazz;
      _typeParameters = typeParameters != null ? new ArrayList<Class<?>>(typeParameters) : Collections
            .<Class<?>> emptyList();
      _annotations = annotations != null ? new HashSet<Annotation>(annotations) : Collections.<Annotation> emptySet();

      _name = null;
   }

   public Injection(final Class<E> clazz, final Annotation... annotations)
   {
      this(clazz, null, Arrays.asList(annotations));
   }

   public Injection(final String name)
   {
      if (name == null || name.length() == 0)
      {
         throw new IllegalArgumentException("Name must be specified!");
      }

      _name = name;
      _class = null;
      _typeParameters = null;
      _annotations = null;
   }

   public boolean isTypeBased()
   {
      return _class != null;
   }

   public boolean isNameBased()
   {
      return _name != null;
   }

   public Class<E> getType()
   {
      return _class;
   }

   public List<Class<?>> getTypeParameters()
   {
      return Collections.unmodifiableList(_typeParameters);
   }

   public Set<Annotation> getAnnotations()
   {
      return Collections.unmodifiableSet(_annotations);
   }

   public String getName()
   {
      return _name;
   }

   @SuppressWarnings("unchecked")
   public E getInstance(final BeanManager beanManager)
   {
      // Type-based case
      if (isTypeBased())
      {
         final Bean<?> bean = beanManager
               .resolve(beanManager.getBeans(_class, _annotations.toArray(new Annotation[0])));

         if (bean == null)
         {
            throw new UnsatisfiedResolutionException("Unable to resolve a bean for " + _class + " with bindings "
                  + _annotations);
         }

         final CreationalContext<?> cc = beanManager.createCreationalContext(bean);
         return _class.cast(beanManager.getReference(bean, _class, cc));
      }

      // Name-based case
      else if (isNameBased())
      {
         final Bean<?> bean = beanManager.resolve(beanManager.getBeans(_name));
         if (bean == null)
         {
            throw new UnsatisfiedResolutionException("Unable to resolve a bean name " + _name);
         }
         final CreationalContext<?> cc = beanManager.createCreationalContext(bean);
         return (E) beanManager.getReference(bean, bean.getBeanClass(), cc);
      }
      else
      {
         return null;
      }
   }

   public static class Builder<E>
   {
      private final BeanManager _beanManager;

      private Class<E> _class;
      private final List<Class<?>> _typeParameters = new ArrayList<Class<?>>();
      private final Set<Annotation> _annotations = new HashSet<Annotation>();
      private String _name;

      private Builder(final BeanManager beanManager)
      {
         _beanManager = beanManager;
      }

      public Builder<E> setClass(final Class<E> clazz)
      {
         checkHasName();

         _class = clazz;

         return this;
      }

      public Builder<E> addTypeParameters(final Collection<Class<?>> typeParameters)
      {
         checkHasName();

         _typeParameters.addAll(typeParameters);

         return this;
      }

      public Builder<E> addTypeParameters(final Class<?>... typeParameters)
      {
         return addTypeParameters(Arrays.asList(typeParameters));
      }

      public Builder<E> addAnnotations(final Collection<Annotation> annotations)
      {
         checkHasName();

         _annotations.addAll(annotations);

         return this;
      }

      public Builder<E> addAnnotations(final Annotation... annotation)
      {
         return addAnnotations(Arrays.asList(annotation));
      }

      public Builder<E> setName(final String name)
      {
         checkHasType();

         _name = name;

         return this;
      }

      public Injection<E> getInjection()
      {
         if (_name != null)
         {
            return new Injection<E>(_name);
         }
         else if (_class != null)
         {
            return new Injection<E>(_class, _typeParameters, _annotations);
         }
         else
         {
            throw new IllegalStateException("Neither a name nor a type is set in this builder!");
         }
      }

      public E getInstance()
      {
         final BeanManager beanManager = _beanManager == null ? CDIContext.getBeanManager() : _beanManager;
         return getInjection().getInstance(beanManager);
      }

      private void checkHasName()
      {
         if (_name != null)
         {
            throw new IllegalStateException("There is already a name set on this builder!");
         }
      }

      private void checkHasType()
      {
         if (_class != null)
         {
            throw new IllegalStateException("There is already a type set on this builder!");
         }
      }

   }
}
