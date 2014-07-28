package net.sf.javagimmicks.collections.mapping;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections.mapping.Mappings.Mapping;

/**
 * Represents a n:m relation between two sets of elements providing a backed
 * {@link Map} view of each "side" of the relation as well as many analytic and
 * modification operations.
 * <p>
 * <b>Example:</b> Assume you have a set of persons - e.g. <i>Alice</i>,
 * <i>Bob</i> and <i>Charles</i> as well as a set of hobbies - e.g.
 * <i>Astrology</i>, <i>Biking</i> and <i>Chess</i> and you want to model any
 * associations between persons and hobbies, this class is just the right thing
 * for you:
 * <p>
 * The code below shows how to create an appropriate instance and fill it with
 * some mappings:
 * 
 * <pre>
 * Mappings&lt;Person, Hobby&gt; m = DualMapMappings.&lt;Person, Hobby&gt; createTreeTreeInstance();
 * 
 * // Alice has hobbies Biking and Chess
 * m.put(Person.Alice, Hobby.Biking);
 * m.put(Person.Alice, Hobby.Chess);
 * 
 * // Bob has hobbies Astrology and Chess
 * m.put(Person.Bob, Hobby.Astrology);
 * m.put(Person.Bob, Hobby.Chess);
 * 
 * // Charles has hobbies Astrology and Biking
 * m.put(Person.Charles, Hobby.Astrology);
 * m.put(Person.Charles, Hobby.Biking);
 * </pre>
 * 
 * Now you can get a "left view" (showing the hobbies for each person) and a
 * "right view" (showing the persons for each hobby) from the {@link Mappings}
 * object:
 * 
 * <pre>
 * {@code
 * System.out.println(m.getLeftView());
 * // Prints {Alice=[Biking, Chess], Bob=[Astrology, Chess], Charles=[Astrology, Biking]}
 * 
 * System.out.println(m.getRightView());
 * // Prints {Astrology=[Bob, Charles], Biking=[Alice, Charles], Chess=[Alice, Bob]}
 * }
 * </pre>
 * <p>
 * For more things that you can do with {@link Mappings}, please consult the
 * method descriptions.
 * <p>
 * If additionally you want to assign a value for each mapping,
 * {@link ValueMappings} is the right choice for you
 * 
 * @see ValueMappings
 * @param <L>
 *           the type of left keys of the {@link Mappings}
 * @param <R>
 *           the type of right keys of the {@link Mappings}
 */
public interface Mappings<L, R> extends Iterable<Mapping<L, R>>
{
   /**
    * Adds a new mapping (or association) between a given left key and right
    * key.
    * 
    * @param left
    *           the left key of the new mapping
    * @param right
    *           the right key of the new mapping
    * @return if the {@link Mappings} was changed during this operation (i.e.
    *         the mapping was new)
    */
   boolean put(L left, R right);

   /**
    * Bulk-adds a bunch of left keys for a single given right key.
    * 
    * @param right
    *           the right key to add a bunch of left keys for
    * @param c
    *           a {@link Collection} of left keys to add for the given right key
    * @return if the {@link Mappings} was changed during this operation
    */
   boolean putAllForRightKey(R right, Collection<? extends L> c);

   /**
    * Bulk-adds a bunch of right keys for a single given left key.
    * 
    * @param left
    *           the left key to add a bunch of right keys for
    * @param c
    *           a {@link Collection} of right keys to add for the given left key
    * @return if the {@link Mappings} was changed during this operation
    */
   boolean putAllForLeftKey(L left, Collection<? extends R> c);

   /**
    * Removes a given mapping specified by left and right key from this
    * instance.
    * 
    * @param left
    *           the left key of the mapping to remove
    * @param right
    *           the right key of the mapping to remove
    * @return if the {@link Mappings} was changed during this operation
    */
   boolean remove(L left, R right);

   /**
    * Completely removes a given right key together with all it's mappings from
    * this instance.
    * 
    * @param right
    *           the right key to remove
    * @return the {@link Set} of left keys that were associated with the given
    *         right key
    */
   Set<L> removeRightKey(R right);

   /**
    * Completely removes a given left key together with all it's mappings from
    * this instance.
    * 
    * @param left
    *           the left key to remove
    * @return the {@link Set} of right keys that were associated with the given
    *         left key
    */
   Set<R> removeLeftKey(L left);

   /**
    * Removes all mappings from this instance
    */
   void clear();

   /**
    * Checks if a given mapping specified by left and right key is contained in
    * the current instance.
    * 
    * @param left
    *           the left key of the mapping to remove
    * @param right
    *           the right key of the mapping to remove
    * @return if the specified mapping is contained in the current instance
    */
   boolean contains(L left, R right);

   /**
    * Check if any mappings are contained in this instance for a given left key.
    * 
    * @param left
    *           the left key to check for any existing mappings
    * @return if there is at least one mapping contained for the given left key
    */
   boolean containsLeftKey(L left);

   /**
    * Check if any mappings are contained in this instance for a given right
    * key.
    * 
    * @param right
    *           the right key to check for any existing mappings
    * @return if there is at least one mapping contained for the given right key
    */
   boolean containsRightKey(R right);

   /**
    * Returns the number of mappings contained within the current instance.
    * 
    * @return the number of mappings contained within the current instance
    */
   int size();

   /**
    * Checks if the current instance contains no mappings.
    * 
    * @return if the current instance contains no mappings
    */
   boolean isEmpty();

   /**
    * Returns all mappings contained within this instance as a {@link Set} of
    * {@link Mapping} instances.
    * 
    * @return all mappings contained within this instance
    */
   Set<Mapping<L, R>> getMappingSet();

   /**
    * Return the "left view" of this instance - that is a {@link Map} that
    * contains the left mapping keys on the key side and a {@link Set} of the
    * associated right mapping keys for the left mapping key on the value side.
    * 
    * @return the left view of this instance as {@link Map}
    */
   Map<L, Set<R>> getLeftView();

   /**
    * Return the "right view" of this instance - that is a {@link Map} that
    * contains the right mapping keys on the key side and a {@link Set} of the
    * associated left mapping keys for the right mapping key on the value side.
    * 
    * @return the right view of this instance as {@link Map}
    */
   Map<R, Set<L>> getRightView();

   /**
    * Returns all left keys that are mapped to a given right key as a
    * {@link Set}.
    * 
    * @param right
    *           the right key to get all mapped left keys for
    * @return the {@link Set} of left keys mapped to the given right key
    */
   Set<L> getAllForRightKey(R right);

   /**
    * Returns all right keys that are mapped to a given left key as a
    * {@link Set}.
    * 
    * @param left
    *           the left key to get all mapped right keys for
    * @return the {@link Set} of right keys mapped to the given left key
    */
   Set<R> getAllForLeftKey(L left);

   /**
    * Returns an inverted view of this instance (left and right keys are
    * exchanged).
    * 
    * @return an inverted view of this instance
    */
   Mappings<R, L> invert();

   /**
    * Returns an {@link Iterator} of all contained {@link Mapping}s.
    * 
    * @see #getMappingSet()
    */
   @Override
   Iterator<Mapping<L, R>> iterator();

   /**
    * Represents a single left-to-right mapping contained within a
    * {@link Mappings} object.
    * 
    * @param <L>
    *           the type of left keys of the {@link Mapping}
    * @param <R>
    *           the type of right keys of the {@link Mapping}
    */
   interface Mapping<L, R>
   {
      /**
       * Returns the left key of the mapping.
       * 
       * @return the left key of the mapping
       */
      L getLeftKey();

      /**
       * Returns the right key of the mapping.
       * 
       * @return the right key of the mapping
       */
      R getRightKey();

      /**
       * Returns an inverted view of this instance (with exchanged left and
       * right key).
       * 
       * @return an inverted view of this instance
       */
      Mapping<R, L> invert();
   }
}
