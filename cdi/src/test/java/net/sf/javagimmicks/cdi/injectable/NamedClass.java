package net.sf.javagimmicks.cdi.injectable;

import javax.inject.Inject;
import javax.inject.Named;

@Named(NamedClass.NAME)
public class NamedClass
{
   public static final String NAME = "THEname";

   @Inject
   private NiceClass _niceClass;

   public String getNicenessFromNiceClass()
   {
      return _niceClass.getNiceness();
   }
}
