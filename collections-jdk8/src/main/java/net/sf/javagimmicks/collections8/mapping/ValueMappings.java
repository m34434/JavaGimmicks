package net.sf.javagimmicks.collections8.mapping;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections8.mapping.ValueMappings.Mapping;
import net.sf.javagimmicks.collections8.transformer.TransformerUtils;

/**
 * Represents a n:m relation between two sets of elements where each mapping can
 * carry an additional value (in contradiction to {@link Mappings}) providing a
 * backed {@link Map} view of each "side" of the relation as well as many
 * analytic and modification operations.
 * <p>
 * <b>Example:</b> Assume you have a set of persons - e.g. <i>Alice</i>,
 * <i>Bob</i> and <i>Charles</i> as well as a set of hobbies - e.g.
 * <i>Astrology</i>, <i>Biking</i> and <i>Chess</i> and you want to model any
 * associations between persons and hobbies where each association should tell
 * how much the person likes the hobby (e.g. <i>Little</i> or <i>Much</i>), this
 * class is just the right thing for you:
 * <p>
 * The code below shows how to create an appropriate instance and fill it with
 * some mappings:
 * 
 * <pre>
 * final ValueMappings&lt;Person, Hobby, Likes&gt; m = DualMapValueMappings.&lt;Person, Hobby, Likes&gt; createTreeTreeInstance();
 * 
 * // Alice has hobbies Biking and Chess
 * m.put(Person.Alice, Hobby.Biking, Likes.Little);
 * m.put(Person.Alice, Hobby.Chess, Likes.Much);
 * 
 * // Bob has hobbies Astrology and Chess
 * m.put(Person.Bob, Hobby.Astrology, Likes.Much);
 * m.put(Person.Bob, Hobby.Chess, Likes.Little);
 * 
 * // Charles has hobbies Astrology and Biking
 * m.put(Person.Charles, Hobby.Astrology, Likes.Little);
 * m.put(Person.Charles, Hobby.Biking, Likes.Much);
 * </pre>
 * 
 * Now you can get a "left view" (showing the hobbies for each person and how
 * much the person likes the hobby) and a "right view" (showing the persons for
 * each hobby and how much the person likes the hobby) from the {@link Mappings}
 * object:
 * 
 * <pre>
 * {@code
 * System.out.println(m.getLeftView());
 * // Prints {Alice={Biking=Little, Chess=Much}, Bob={Astrology=Much, Chess=Little}, Charles={Astrology=Little, Biking=Much}}
 * 
 * System.out.println(m.getRightView());
 * // Prints {Astrology={Bob=Much, Charles=Little}, Biking={Alice=Little, Charles=Much}, Chess={Alice=Much, Bob=Little}}
 * }
 * </pre>
 * <p>
 * For more things that you can do with {@link ValueMappings}, please consult
 * the method descriptions.
 * <p>
 * If you don't want to assign a value for each mapping but simply want to
 * associate left and right elements without additional information,
 * {@link Mappings} is the right choice for you.
 * 
 * @see Mappings
 * @param <L>
 *           the type of left keys of the {@link ValueMappings}
 * @param <R>
 *           the type of right keys of the {@link ValueMappings}
 * @param <E>
 *           the type of values associated with the mappings
 */
public interface ValueMappings<L, R, E> extends Iterable<Mapping<L, R, E>>
{
   /**
    * Adds a new or replaces an existing mapping (or association) between a
    * given left key and right key with the given value.
    * 
    * @param left
    *           the left key of the new mapping
    * @param right
    *           the right key of the new mapping
    * @param value
    *           the value to associate with the new or existing mapping
    * @return the previous value associated with the mapping or {@code null} of
    *         the mapping is new
    */
   E put(L left, R right, E value);

   /**
    * Bulk-adds a bunch of left key-value associations for a single given right
    * key.
    * 
    * @param right
    *           the right key to add a bunch of left key-value associations for
    * @param map
    *           a {@link Map} containing the left keys to add for the given
    *           right key as well as the values to associate with the respective
    *           new mappings
    */
   default void putAllForRightKey(final R right, final Map<? extends L, ? extends E> map)
   {
      map.entrySet().forEach(leftEntry -> put(leftEntry.getKey(), right, leftEntry.getValue()));
   }

   /**
    * Bulk-adds a bunch of right key-value associations for a single given left
    * key.
    * 
    * @param left
    *           the left key to add a bunch of right key-value associations for
    * @param map
    *           a {@link Map} containing the right keys to add for the given
    *           left key as well as the values to associate with the respective
    *           new mappings
    */
   default void putAllForLeftKey(final L left, final Map<? extends R, ? extends E> map)
   {
      map.entrySet().forEach(rightEntry -> put(left, rightEntry.getKey(), rightEntry.getValue()));
   }

   /**
    * Removes a given mapping specified by left and right key from this
    * instance.
    * 
    * @param left
    *           the left key of the mapping to remove
    * @param right
    *           the right key of the mapping to remove
    * @return the value that was associated with the removed mapping or
    *         {@code null} if no such mapping was contained
    */
   default E remove(final L left, final R right)
   {
      final Map<R, E> mappedValuesLeft = getAllForLeftKey(left);

      return mappedValuesLeft != null ? mappedValuesLeft.remove(right) : null;
   }

   /**
    * Completely removes a given right key together with all it's mappings from
    * this instance.
    * 
    * @param right
    *           the right key to remove
    * @return the {@link Map} containing the left keys that were mapped to the
    *         given right key as well as the associated values
    */
   default Map<L, E> removeRightKey(final R right)
   {
      return getRightView().remove(right);
   }

   /**
    * Completely removes a given left key together with all it's mappings from
    * this instance.
    * 
    * @param left
    *           the left key to remove
    * @return the {@link Map} containing the right keys that were mapped to with
    *         the given left key as well as the associated values
    */
   default Map<R, E> removeLeftKey(final L left)
   {
      return getLeftView().remove(left);
   }

   /**
    * Removes all mappings from this instance
    */
   default void clear()
   {
      getLeftView().clear();
   }

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
   default boolean containsMapping(final L left, final R right)
   {
      final Map<R, E> rightInnerMap = getAllForLeftKey(left);
      return rightInnerMap != null && rightInnerMap.containsKey(right);
   }

   /**
    * Check if any mappings are contained in this instance for a given left key.
    * 
    * @param left
    *           the left key to check for any existing mappings
    * @return if there is at least one mapping contained for the given left key
    */
   default boolean containsLeftKey(final L left)
   {
      return getLeftView().containsKey(left);
   }

   /**
    * Check if any mappings are contained in this instance for a given right
    * key.
    * 
    * @param right
    *           the right key to check for any existing mappings
    * @return if there is at least one mapping contained for the given right key
    */
   default boolean containsRightKey(final R right)
   {
      return getRightView().containsKey(right);
   }

   /**
    * Returns the number of mappings contained within the current instance.
    * 
    * @return the number of mappings contained within the current instance
    */
   default int size()
   {
      return getMappingSet().size();
   }

   /**
    * Checks if the current instance contains no mappings.
    * 
    * @return if the current instance contains no mappings
    */
   boolean isEmpty();

   /**
    * Retrieve the value associated with the mapping specified by left and right
    * key.
    * 
    * @param left
    *           the left key of the mapping whose value should be retrieved
    * @param right
    *           the right key of the mapping whose value should be retrieved
    * @return the associated value or {@code null} if no such mapping was
    *         contained
    */
   default E get(final L left, final R right)
   {
      final Map<R, E> rightInnerMap = getAllForLeftKey(left);

      return rightInnerMap != null ? rightInnerMap.get(right) : null;
   }

   /**
    * Returns all mappings contained within this instance as a {@link Set} of
    * {@link Mapping} instances.
    * 
    * @return all mappings contained within this instance
    */
   Set<Mapping<L, R, E>> getMappingSet();

   /**
    * Returns a {@link Collection} of the values of all mappings contained
    * within this instance.
    * 
    * @return a {@link Collection} of the values of all mappings
    */
   default Collection<E> getValues()
   {
      return TransformerUtils.decorate(getMappingSet(), source -> source.getValue());
   }

   /**
    * Return the "left view" of this instance - that is a {@link Map} that
    * contains on the key side the left mapping keys and on the value side
    * another {@link Map} which has on the key side the right mapping keys and
    * on the value side the associated values.
    * <p>
    * Have a look at the example with the {@link ValueMappings class
    * documentation} to get a better understanding.
    * 
    * @return the left view of this instance as {@link Map}
    */
   Map<L, Map<R, E>> getLeftView();

   /**
    * Return the "right view" of this instance - that is a {@link Map} that
    * contains on the key side the right mapping keys and on the value side
    * another {@link Map} which has on the key side the left mapping keys and on
    * the value side the associated values.
    * <p>
    * Have a look at the example with the {@link ValueMappings class
    * documentation} to get a better understanding.
    * 
    * @return the right view of this instance as {@link Map}
    */
   Map<R, Map<L, E>> getRightView();

   /**
    * Returns a {@link Map} of all left keys that are mapped to a given right
    * key and the associated values.
    * 
    * @param right
    *           the right key to get all mapped left keys for
    * @return the {@link Map} of left keys mapped to the given right key
    */
   default Map<L, E> getAllForRightKey(final R right)
   {
      return getRightView().get(right);
   }

   /**
    * Returns a {@link Map} of all right keys that are mapped to a given left
    * key and the associated values.
    * 
    * @param left
    *           the left key to get all mapped right keys for
    * @return the {@link Map} of right keys mapped to the given left key
    */
   default Map<R, E> getAllForLeftKey(final L left)
   {
      return getLeftView().get(left);
   }

   /**
    * Returns an inverted view of this instance (left and right keys are
    * exchanged).
    * 
    * @return an inverted view of this instance
    */
   ValueMappings<R, L, E> invert();

   /**
    * Returns an {@link Iterator} of all contained {@link Mapping}s.
    * 
    * @see #getMappingSet()
    */
   @Override
   default Iterator<Mapping<L, R, E>> iterator()
   {
      return getMappingSet().iterator();
   }

   /**
    * Represents a single left-to-right mapping contained within a
    * {@link ValueMappings} object.
    * 
    * @param <L>
    *           the type of left keys of the {@link Mapping}
    * @param <R>
    *           the type of right keys of the {@link Mapping}
    * @param <E>
    *           the type of values associated with the mappings
    */
   interface Mapping<L, R, E>
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
       * Returns the value associated with the mapping.
       * 
       * @return the value associated with the mapping
       */
      E getValue();

      /**
       * Returns an inverted view of this instance (with exchanged left and
       * right key).
       * 
       * @return an inverted view of this instance
       */
      default Mapping<R, L, E> invert()
      {
         return new Mapping<R, L, E>()
         {
            @Override
            public Mapping<L, R, E> invert()
            {
               return Mapping.this;
            }

            @Override
            public R getLeftKey()
            {
               return Mapping.this.getRightKey();
            }

            @Override
            public L getRightKey()
            {
               return Mapping.this.getLeftKey();
            }

            @Override
            public E getValue()
            {
               return Mapping.this.getValue();
            }
         };
      }
   }
}
