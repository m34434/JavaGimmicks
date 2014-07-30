package net.sf.javagimmicks.collections8.transformer;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.javagimmicks.collections8.transformer.TransformerUtils;

public class TransformingCollectionTest extends AbstractTransformingCollectionTest
{
   @Override
   protected Collection<String> buildCollection()
   {
      return new ArrayList<String>();
   }

   @Override
   protected Collection<Integer> decorate(Collection<String> base)
   {
      return TransformerUtils.decorate(base, getTransformer());
   }

   @Override
   protected Collection<Integer> decorateBidi(Collection<String> base)
   {
      return TransformerUtils.decorate(base, getBidiTransformer());
   }  
}
