/**
 * Provides Java 8 enhanced composite implementations for the most
 * important Collection API types. A composite collection
 * is one that wraps around any number of other collections
 * but providing them behind a single instance interface.
 * <p>
 * Instances cannot be directly instantiated, instead
 * {@link net.sf.javagimmicks.collections8.composite.CompositeUtils}
 * can be used to wrap a collection of the respective type
 * around any number of the same type (using a kind of <i>decorator</i>
 * pattern)
 * <p>
 * <a name="enumeration"></a>
 * <h3>Operations on a composite {@link java.util.Enumeration}</h3>
 * <table border="2" summary="Operations on a composite Enumeration">
 *    <thead>
 *       <tr>
 *          <th>Operation</th>
 *          <th>Supported</th>
 *       </tr>
 *    </thead>
 *    <tbody>
 *       <tr>
 *          <td>hasMoreElements()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>nextElement()</td>
 *          <td>supported</td>
 *       </tr>
 *    </tbody>
 * </table>
 * 
 * <a name="iterator"></a>
 * <h3>Operations on a composite {@link java.util.Iterator}</h3>
 * <table border="2" summary="Operations on a composite Iterator">
 *    <thead>
 *       <tr>
 *          <th>Operation</th>
 *          <th>Supported</th>
 *       </tr>
 *    </thead>
 *    <tbody>
 *       <tr>
 *          <td>hasNext()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>next()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>remove()</td>
 *          <td>supported</td>
 *       </tr>
 *    </tbody>
 * </table>
 * 
 * <a name="collection"></a>
 * <h3>Operations on a composite {@link java.util.Collection}</h3>
 * <table border="2" summary="Operations on a composite Collection">
 *    <thead>
 *       <tr>
 *          <th>Operation</th>
 *          <th>Supported</th>
 *       </tr>
 *    </thead>
 *    <tbody>
 *       <tr>
 *          <td>add()</td>
 *          <td>not supported</td>
 *       </tr>
 *       <tr>
 *          <td>addAll()</td>
 *          <td>not supported</td>
 *       </tr>
 *       <tr>
 *          <td>clear()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>contains()</td>
 *          <td>supported; needs {@code equals()} method on elements</td>
 *       </tr>
 *       <tr>
 *          <td>containsAll()</td>
 *          <td>supported; needs {@code equals()} method on elements</td>
 *       </tr>
 *       <tr>
 *          <td>isEmpty()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>iterator()</td>
 *          <td>supported; See <a href="#iterator">{@code Iterator} operations</a></td>
 *       </tr>
 *       <tr>
 *          <td>remove()</td>
 *          <td>supported; needs {@code equals()} method on elements</td>
 *       </tr>
 *       <tr>
 *          <td>removeAll()</td>
 *          <td>supported; needs {@code equals()} method on elements</td>
 *       </tr>
 *       <tr>
 *          <td>retainAll()</td>
 *          <td>supported; needs {@code equals()} method on elements</td>
 *       </tr>
 *       <tr>
 *          <td>size()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>toArray()</td>
 *          <td>supported</td>
 *       </tr>
 *    </tbody>
 * </table>
 * 
 * <a name="listiterator"></a>
 * <h3>Operations on a transformed {@link java.util.ListIterator}</h3>
 * <table border="2" summary="Operations on a transformed ListIterator">
 *    <thead>
 *       <tr>
 *          <th>Operation</th>
 *          <th>Supported</th>
 *       </tr>
 *    </thead>
 *    <tbody>
 *       <tr>
 *          <td>{@link java.util.Iterator} operations</td>
 *          <td>See table for <a href="#iterator">{@code Iterator} operations</a></td>
 *       </tr>
 *       <tr>
 *          <td>add()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>hasPrevious()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>nextIndex()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>previous()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>previousIndex()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>set()</td>
 *          <td>supported</td>
 *       </tr>
 *    </tbody>
 * </table>
 * 
 * <a name="list"></a>
 * <h3>Operations on a composite {@link java.util.List}</h3>
 * <table border="2" summary="Operations on a composite List">
 *    <thead>
 *       <tr>
 *          <th>Operation</th>
 *          <th>Supported</th>
 *       </tr>
 *    </thead>
 *    <tbody>
 *       <tr>
 *          <td>add()</td>
 *          <td>not supported</td>
 *       </tr>
 *       <tr>
 *          <td>addAll()</td>
 *          <td>not supported</td>
 *       </tr>
 *       <tr>
 *          <td>clear()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>contains()</td>
 *          <td>supported; needs {@code equals()} method</td>
 *       </tr>
 *       <tr>
 *          <td>containsAll()</td>
 *          <td>supported; needs {@code equals()} method</td>
 *       </tr>
 *       <tr>
 *          <td>isEmpty()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>iterator()</td>
 *          <td>supported; See <a href="#iterator">{@code Iterator} operations</a></td>
 *       </tr>
 *       <tr>
 *          <td>remove()</td>
 *          <td>supported; needs {@code equals()} method</td>
 *       </tr>
 *       <tr>
 *          <td>removeAll()</td>
 *          <td>supported; needs {@code equals()} method</td>
 *       </tr>
 *       <tr>
 *          <td>retainAll()</td>
 *          <td>supported; needs {@code equals()} method</td>
 *       </tr>
 *       <tr>
 *          <td>size()</td>
 *          <td>supported</td>
 *       </tr>
 *       <tr>
 *          <td>toArray()</td>
 *          <td>supported</td>
 *       </tr>
 *    </tbody>
 * </table>
 * @author Michael Scholz
 */
package net.sf.javagimmicks.collections8.composite;