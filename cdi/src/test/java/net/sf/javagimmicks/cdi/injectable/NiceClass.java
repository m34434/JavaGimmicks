package net.sf.javagimmicks.cdi.injectable;

public class NiceClass
{
   private final String _niceness;
   public NiceClass(final String niceness)
   {
      _niceness = niceness;
   }

   public String getNiceness()
   {
      return _niceness;
   }
}