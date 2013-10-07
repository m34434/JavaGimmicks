/**
 * Contains a folder comparison engine that scans the contents of two folders
 * and find differences between them down to file level.
 * <p>
 * It is easily configurable, how files are compared by activating any of the
 * following modes:
 * <ul>
 *    <li>Change date comparison</li>
 *    <li>File size comparison</li>
 *    <li>File checksum comparison</li>
 * </ul>
 * <p>
 * The engine is heavily built on top of {@link net.sf.javagimmicks.collections.diff}
 * features (from <b>collections</b> module).
 */
package net.sf.javagimmicks.io.folderdiff;

