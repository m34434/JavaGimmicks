package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.Collection;
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
 */
public interface Mappings<L, R> extends Iterable<Mapping<L, R>>
{
   public boolean put(L left, R right);

   public boolean putLeftValuesFor(R right, Collection<? extends L> c);

   public boolean putRightValuesFor(L left, Collection<? extends R> c);

   public boolean remove(L left, R right);

   public Set<L> removeRightKey(R right);

   public Set<R> removeLeftKey(L left);

   public void clear();

   public boolean contains(L left, R right);

   public boolean containsLeftKey(L left);

   public boolean containsRightKey(R right);

   public int size();

   public boolean isEmpty();

   public Mappings<R, L> getInverseMappings();

   public Set<Mapping<L, R>> getMappingSet();

   public Map<L, Set<R>> getLeftView();

   public Map<R, Set<L>> getRightView();

   public Set<L> getLeftValuesFor(R right);

   public Set<R> getRightValuesFor(L left);

   public interface Mapping<L, R> extends Serializable
   {
      public L getLeftKey();

      public R getRightKey();

      public Mapping<R, L> getInverseMapping();
   }
}
