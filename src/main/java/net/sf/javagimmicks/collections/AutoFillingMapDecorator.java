package net.sf.javagimmicks.collections;

import java.util.Map;

import net.sf.javagimmicks.collections.decorators.AbstractMapDecorator;
import net.sf.javagimmicks.lang.Factory;

public class AutoFillingMapDecorator<K, V> extends AbstractMapDecorator<K, V>
{
   private static final long serialVersionUID = -955460173448590006L;

   public static <K, V> Map<K, V> decorate(Map<K, V> decorated, Factory<V> valueFactory)
   {
      return new AutoFillingMapDecorator<K, V>(decorated, valueFactory);
   }
   
   protected final Factory<V> _valueFactory;
   
   protected AutoFillingMapDecorator(Map<K, V> decorated, Factory<V> valueFactory)
   {
      super(decorated);
      _valueFactory = valueFactory;
   }

   @Override
   @SuppressWarnings("unchecked")
   public V get(Object key)
   {
      if(!containsKey(key))
      {
         put((K)key, _valueFactory.create());
      }
      
      return super.get(key);
   }
}
