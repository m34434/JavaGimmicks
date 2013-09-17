package net.sf.javagimmicks.cdi.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import net.sf.javagimmicks.cdi.injectable.Coolness;

@Qualifier
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
public @interface Cool
{
   // @Nonbinding Disables this property for injection point matching
   // i.e. it's okay to use it at @Inject but skip it at @Produces
   @Nonbinding
   Coolness coolness() default Coolness.NORMAL;
}
