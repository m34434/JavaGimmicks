package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.util.Function;
import net.sf.javagimmicks.util.IncludeExcludePatternFilter;

class IncludeExcludeFilenameFilter implements FilenameFilter
{
   private final IncludeExcludePatternFilter _filter = new IncludeExcludePatternFilter();
   private static final Function<String, String> _patternTransformer = new Function<String, String>()
   {
      @Override
      public String apply(final String source)
      {
         return source.replaceAll("\\.", "\\.").replaceAll("\\*", ".*").replaceAll("\\?", ".");
      }
   };

   public IncludeExcludeFilenameFilter(final Collection<String> includePatterns,
         final Collection<String> excludePatterns)
   {
      addIncludePatterns(includePatterns);
      addExcludePatterns(excludePatterns);
   }

   public IncludeExcludeFilenameFilter(final Collection<String> excludePatterns)
   {
      addExcludePatterns(excludePatterns);
   }

   public IncludeExcludeFilenameFilter(final String includePattern, final String excludePattern)
   {
      addIncludePattern(includePattern);
      addExcludePattern(excludePattern);
   }

   public IncludeExcludeFilenameFilter(final String excludePattern)
   {
      addExcludePattern(excludePattern);
   }

   public IncludeExcludeFilenameFilter()
   {}

   @Override
   public boolean accept(final File dir, final String name)
   {
      return _filter.accepts(name);
   }

   public void addIncludePattern(final String pattern)
   {
      _filter.addIncludePatterns(_patternTransformer.apply(pattern));
   }

   public void addIncludePatterns(final Collection<String> patterns)
   {
      _filter.addIncludePatternStrings(TransformerUtils.decorate(patterns, _patternTransformer));
   }

   public void addExcludePattern(final String pattern)
   {
      _filter.addExcludePatterns(_patternTransformer.apply(pattern));
   }

   public void addExcludePatterns(final Collection<String> patterns)
   {
      _filter.addExcludePatternStrings(TransformerUtils.decorate(patterns, _patternTransformer));
   }
}
