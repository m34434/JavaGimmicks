package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

import net.sf.javagimmicks.collections.transformer.Transformer;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.util.IncludeExcludePatternFilter;

class IncludeExcludeFilenameFilter implements FilenameFilter
{
   private final IncludeExcludePatternFilter _filter = new IncludeExcludePatternFilter();
   private static final Transformer<String, String> _patternTransformer = new Transformer<String, String>()
   {
      public String transform(String source)
      {
         return source.replaceAll("\\.", "\\.").replaceAll("\\*", ".*").replaceAll("\\?", ".");
      }
   };
   
   public IncludeExcludeFilenameFilter(Collection<String> includePatterns, Collection<String> excludePatterns)
   {
      addIncludePatterns(includePatterns);
      addExcludePatterns(excludePatterns);
   }
   
   public IncludeExcludeFilenameFilter(Collection<String> excludePatterns)
   {
      addExcludePatterns(excludePatterns);
   }
   
   public IncludeExcludeFilenameFilter(String includePattern, String excludePattern)
   {
      addIncludePattern(includePattern);
      addExcludePattern(excludePattern);
   }
   
   public IncludeExcludeFilenameFilter(String excludePattern)
   {
      addExcludePattern(excludePattern);
   }
   
   public IncludeExcludeFilenameFilter() {}

   public boolean accept(File dir, String name)
   {
      return _filter.accepts(name);
   }

   public void addIncludePattern(String pattern)
   {
      _filter.addIncludePatternString(_patternTransformer.transform(pattern));
   }
   
   public void addIncludePatterns(Collection<String> patterns)
   {
      _filter.addIncludePatternStrings(TransformerUtils.decorate(patterns, _patternTransformer));
   }

   public void addExcludePattern(String pattern)
   {
      _filter.addExcludePatternString(_patternTransformer.transform(pattern));
   }
   
   public void addExcludePatterns(Collection<String> patterns)
   {
      _filter.addExcludePatternStrings(TransformerUtils.decorate(patterns, _patternTransformer));
   }
}
