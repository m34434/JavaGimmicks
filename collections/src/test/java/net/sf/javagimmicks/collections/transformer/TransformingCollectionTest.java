package net.sf.javagimmicks.collections.transformer;

import java.util.ArrayList;
import java.util.Collection;

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
