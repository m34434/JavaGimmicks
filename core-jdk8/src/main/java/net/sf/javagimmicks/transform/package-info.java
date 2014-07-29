/**
 * Contains a very basic API for transforming objects into other ones
 * (and optionally vice versa).
 * <p>
 * It leverages Java 8's {@link java.util.function.Function} interface
 * which can transform one type of objects into other ones (of same or different
 * type) and an extension interface {@link net.sf.javagimmicks.transform.BidiFunction}
 * which can additionally do an opposite transformation.
 * <p>
 * The two interfaces {@link net.sf.javagimmicks.transform.Transforming} and
 * {@link net.sf.javagimmicks.transform.BidiTransforming} act as a sort of marker
 * interfaces for objects that can carry a {@link java.util.function.Function}
 * or {@link net.sf.javagimmicks.transform.BidiFunction}.
 * 
 * @author Michael Scholz
 */
package net.sf.javagimmicks.transform;