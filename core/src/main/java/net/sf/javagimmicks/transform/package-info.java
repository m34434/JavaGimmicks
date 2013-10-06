/**
 * Contains a very basic API for transforming objects into other ones
 * (and optionally vice versa).
 * <p>
 * It defines two interfaces {@link net.sf.javagimmicks.transform.Transformer}
 * which can transform one type of objects into other ones (of same or different
 * type) and an extension interface {@link net.sf.javagimmicks.transform.BidiTransformer}
 * which can additionally do an opposite transformation.
 * <p>
 * The two interfaces {@link net.sf.javagimmicks.transform.Transforming} and
 * {@link net.sf.javagimmicks.transform.BidiTransforming} act as a sort of marker
 * interfaces for objects that can carry a {@link net.sf.javagimmicks.transform.Transformer}
 * or {@link net.sf.javagimmicks.transform.BidiTransformer}.
 * <p>
 * {@link net.sf.javagimmicks.transform.Transformers} finally contains a small tool-set of
 * static helper methods around the named interfaces.
 * 
 * @author Michael Scholz
 */
package net.sf.javagimmicks.transform;