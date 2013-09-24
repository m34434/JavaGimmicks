package net.sf.javagimmicks.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.javagimmicks.lang.Filter;

/**
 * A {@link Filter} for {@link CharSequence}s that internally uses any number of
 * include and exclude regular expression {@link Pattern}s for filtering.
 */
public class IncludeExcludePatternFilter implements Filter<CharSequence>
{
   /**
    * Creates a new instance for given {@link Collection}s of include and
    * exclude {@link Pattern}s.
    * 
    * @param includePatterns
    *           the {@link Collection} of include {@link Pattern}s
    * @param excludePatterns
    *           the {@link Collection} of exclude {@link Pattern}s
    * @return the new instance
    */
   public static IncludeExcludePatternFilter fromPatterns(final Collection<Pattern> includePatterns,
         final Collection<Pattern> excludePatterns)
   {
      return new IncludeExcludePatternFilter(
            new ArrayList<Pattern>(includePatterns),
            new ArrayList<Pattern>(excludePatterns));
   }

   /**
    * Creates a new instance for a given {@link Collection} of include
    * {@link Pattern}s.
    * 
    * @param includePatterns
    *           the {@link Collection} of include {@link Pattern}s
    * @return the new instance
    */
   public static IncludeExcludePatternFilter fromPatterns(final Collection<Pattern> includePatterns)
   {
      final Collection<Pattern> excludePatterns = Collections.emptySet();

      return fromPatterns(includePatterns, excludePatterns);
   }

   /**
    * Creates a new instance for given {@link Collection}s of include and
    * exclude patterns provided as regular expression {@link String}s.
    * 
    * @param includePatterns
    *           the {@link Collection} of include patterns
    * @param excludePatterns
    *           the {@link Collection} of exclude patterns
    * @return the new instance
    */
   public static IncludeExcludePatternFilter fromStringPatterns(final Collection<String> includePatterns,
         final Collection<String> excludePatterns)
   {
      return fromPatterns(bulkCompile(includePatterns), bulkCompile(excludePatterns));
   }

   /**
    * Creates a new instance for a given {@link Collection} of include patterns
    * provided as regular expression {@link String}s.
    * 
    * @param includePatterns
    *           the {@link Collection} of include patterns
    * @return the new instance
    */
   public static IncludeExcludePatternFilter fromStringPatterns(final Collection<String> includePatterns)
   {
      final Collection<String> excludePatterns = Collections.emptySet();

      return fromStringPatterns(includePatterns, excludePatterns);
   }

   private final List<Pattern> _includePatterns;
   private final List<Pattern> _excludePatterns;

   /**
    * Creates a new instance without any include or exclude {@link Pattern}s.
    */
   public IncludeExcludePatternFilter()
   {
      this(new ArrayList<Pattern>(), new ArrayList<Pattern>());
   }

   private IncludeExcludePatternFilter(final List<Pattern> includePatterns, final List<Pattern> excludePatterns)
   {
      _includePatterns = includePatterns;
      _excludePatterns = excludePatterns;
   }

   /**
    * Adds a number of include {@link Pattern}s to this instance.
    * 
    * @param patterns
    *           the {@link Pattern}s to add
    */
   public void addIncludePatterns(final Collection<Pattern> patterns)
   {
      _includePatterns.addAll(patterns);
   }

   /**
    * Adds a number of include {@link Pattern}s to this instance.
    * 
    * @param patterns
    *           the {@link Pattern}s to add
    */
   public void addIncludePatterns(final Pattern... patterns)
   {
      addIncludePatterns(Arrays.asList(patterns));
   }

   /**
    * Adds a number of include pattern {@link String}s to this instance.
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    */
   public void addIncludePatternStrings(final Collection<String> patterns)
   {
      _includePatterns.addAll(bulkCompile(patterns));
   }

   /**
    * Adds a number of include pattern {@link String}s to this instance.
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    */
   public void addIncludePatterns(final String... patterns)
   {
      addIncludePatternStrings(Arrays.asList(patterns));
   }

   /**
    * Adds a number of exclude {@link Pattern}s to this instance.
    * 
    * @param patterns
    *           the {@link Pattern}s to add
    */
   public void addExcludePatterns(final Collection<Pattern> patterns)
   {
      _excludePatterns.addAll(patterns);
   }

   /**
    * Adds a number of exclude {@link Pattern}s to this instance.
    * 
    * @param patterns
    *           the {@link Pattern}s to add
    */
   public void addExcludePatterns(final Pattern... patterns)
   {
      addExcludePatterns(Arrays.asList(patterns));
   }

   /**
    * Adds a number of exclude pattern {@link String}s to this instance.
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    */
   public void addExcludePatternStrings(final Collection<String> patterns)
   {
      _excludePatterns.addAll(bulkCompile(patterns));
   }

   /**
    * Adds a number of exclude pattern {@link String}s to this instance.
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    */
   public void addExcludePatterns(final String... patterns)
   {
      addExcludePatternStrings(Arrays.asList(patterns));
   }

   /**
    * Returns a read-only view of the contained include {@link Pattern}s.
    * 
    * @return the include {@link Pattern}s
    */
   public List<Pattern> getIncludePatterns()
   {
      return Collections.unmodifiableList(_includePatterns);
   }

   /**
    * Returns a read-only view of the contained exclude {@link Pattern}s.
    * 
    * @return the exclude {@link Pattern}s
    */
   public List<Pattern> getExcludePatterns()
   {
      return Collections.unmodifiableList(_excludePatterns);
   }

   @Override
   public boolean accepts(final CharSequence charSequence)
   {
      return (_includePatterns.isEmpty() || matchesAny(_includePatterns, charSequence))
            && !matchesAny(_excludePatterns, charSequence);
   }

   private static boolean matchesAny(final List<Pattern> patterns, final CharSequence charSequence)
   {
      for (final Pattern pattern : patterns)
      {
         if (pattern.matcher(charSequence).matches())
         {
            return true;
         }
      }

      return false;
   }

   private static Set<Pattern> bulkCompile(final Collection<String> patterns)
   {
      final Set<Pattern> result = new HashSet<Pattern>();
      for (final String pattern : patterns)
      {
         result.add(Pattern.compile(pattern));
      }

      return result;
   }
}
