package net.sf.javagimmicks.collections8.transformer;

import java.util.function.Consumer;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.Transforming;

class TransformingConsumer<F, T> implements Transforming<F, T>, Consumer<F>
{
   protected final Consumer<? super T> _internalConsumer;
   private final Function<F, T> _transformer;   
   
   TransformingConsumer(Consumer<? super T> _internalConsumer, Function<F, T> _transformer)
   {
      this._internalConsumer = _internalConsumer;
      this._transformer = _transformer;
   }

   @Override
   public void accept(F f)
   {
      _internalConsumer.accept(_transformer.apply(f));
   }

   @Override
   public Function<F, T> getTransformerFunction()
   {
      return _transformer;
   }

}
