package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections.mapping.ValueMappings.Mapping;

/**
 * Represents a n:m relation between two sets of elements where each mapping can
 * carry an additional value (in contradiction to {@link Mappings})providing a
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
 * System.out.println(m.getLeftMap());
 * // Prints {Alice={Biking=Little, Chess=Much}, Bob={Astrology=Much, Chess=Little}, Charles={Astrology=Little, Biking=Much}}
 * 
 * System.out.println(m.getRightMap());
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
 */
public interface ValueMappings<L, R, E> extends Iterable<Mapping<L, R, E>>
{
   public E put(L left, R right, E value);

   public void putLeft(R right, Map<? extends L, ? extends E> c);

   public void putRight(L left, Map<? extends R, ? extends E> c);

   public E get(L left, R right);

   public E remove(L left, R right);

   public Map<L, E> removeLeft(R right);

   public Map<R, E> removeRight(L left);

   public void clear();

   public boolean containsMapping(L left, R right);

   public boolean containsLeft(L left);

   public boolean containsRight(R right);

   public int size();

   public boolean isEmpty();

   public ValueMappings<R, L, E> getInverseMappings();

   public Set<Mapping<L, R, E>> getMappingSet();

   public Collection<E> getValues();

   public Map<L, Map<R, E>> getLeftOuterMap();

   public Map<R, Map<L, E>> getRightOuterMap();

   public Map<L, E> getLeftInnerMap(R right);

   public Map<R, E> getRightInnerMap(L left);

   public interface Mapping<L, R, E> extends Serializable
   {
      public L getLeft();

      public R getRight();

      public E getValue();

      public Mapping<R, L, E> getInverseMapping();
   }
}
