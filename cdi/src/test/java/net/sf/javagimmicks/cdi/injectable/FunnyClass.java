package net.sf.javagimmicks.cdi.injectable;

public class FunnyClass
{
   private Coolness _coolness;

   public FunnyClass(final String fake)
   {}

   public Coolness getCoolness()
   {
      return _coolness;
   }

   public void setCoolness(final Coolness coolness)
   {
      this._coolness = coolness;
   }
}
