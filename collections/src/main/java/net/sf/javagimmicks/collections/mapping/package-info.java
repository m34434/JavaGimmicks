/**
 * This API introduces two new data-structures {@link net.sf.javagimmicks.collections.mapping.Mappings}
 * and {@link net.sf.javagimmicks.collections.mapping.ValueMappings} that allow to model n:m relations
 * and get information for each "side" via well-known {@link java.util.Map} API.
 * <p>
 * <b>Example:</b>
 * Assume you have a set of persons - e.g. <i>Alice</i>, <i>Bob</i> and <i>Charles</i> as well as a set of
 * hobbies - e.g. <i>Astrology</i>, <i>Biking</i> and <i>Chess</i> and you want to model any associations
 * between persons and hobbies, a {@link net.sf.javagimmicks.collections.mapping.Mappings} object is just
 * the right thing for you:
 * <pre>
 * {@code
 * Mappings<Person, Hobby> m = DualMapMappings.<Person, Hobby>createTreeTreeInstance();
 * 
 * // Alice has hobbies Biking and Chess
 * m.put(Person.Alice, Hobby.Biking);
 * m.put(Person.Alice, Hobby.Chess);

 * // Bob has hobbies Astrology and Chess
 * m.put(Person.Bob, Hobby.Astrology);
 * m.put(Person.Bob, Hobby.Chess);
 * 
 * // Charles has hobbies Astrology and Biking
 * m.put(Person.Charles, Hobby.Astrology);
 * m.put(Person.Charles, Hobby.Biking);
 * }
 * </pre>
 * Now you can get a "left view" (showing the hobbies for each person) and a "right view" (showing the
 * persons for each hobby) from the {@link net.sf.javagimmicks.collections.mapping.Mappings} object:
 * <pre>
 * {@code
 * System.out.println(m.getLeftMap());
 * // Prints {Alice=[Biking, Chess], Bob=[Astrology, Chess], Charles=[Astrology, Biking]}
 * 
 * System.out.println(m.getRightMap());
 * // Prints {Astrology=[Bob, Charles], Biking=[Alice, Charles], Chess=[Alice, Bob]}
 * }
 * </pre>
 * <p>
 * If additionally you want to assign a value for each such combination or mapping,
 * {@link net.sf.javagimmicks.collections.mapping.ValueMappings} is the right choice for you
 */
package net.sf.javagimmicks.collections.mapping;