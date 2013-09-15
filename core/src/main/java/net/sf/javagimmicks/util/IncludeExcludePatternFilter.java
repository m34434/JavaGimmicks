package net.sf.javagimmicks.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.javagimmicks.lang.Filter;

public class IncludeExcludePatternFilter implements Filter<CharSequence>
{
   public static IncludeExcludePatternFilter fromPatterns(Collection<Pattern> includePatterns, Collection<Pattern> excludePatterns)
   {
      return new IncludeExcludePatternFilter(
         new ArrayList<Pattern>(includePatterns),
         new ArrayList<Pattern>(excludePatterns));
   }
   
   public static IncludeExcludePatternFilter fromPatterns(Collection<Pattern> includePatterns)
   {
      Collection<Pattern> excludePatterns = Collections.emptySet();

      return fromPatterns(includePatterns, excludePatterns);
   }
   
   public static IncludeExcludePatternFilter fromStringPatterns(Collection<String> includePatterns, Collection<String> excludePatterns)
   {
      return fromPatterns(bulkCompile(includePatterns), bulkCompile(excludePatterns));
   }
   
   public static IncludeExcludePatternFilter fromStringPatterns(Collection<String> includePatterns)
   {
      Collection<String> excludePatterns = Collections.emptySet();

      return fromStringPatterns(includePatterns, excludePatterns);
   }
   
   private final List<Pattern> _includePatterns;
   private final List<Pattern> _excludePatterns;
   
   public IncludeExcludePatternFilter()
   {
      this(new ArrayList<Pattern>(), new ArrayList<Pattern>());
   }
   
   private IncludeExcludePatternFilter(List<Pattern> includePatterns, List<Pattern> excludePatterns)
   {
      _includePatterns = includePatterns;
      _excludePatterns = excludePatterns;
   }
   
   public void addIncludePattern(Pattern pattern)
   {
      _includePatterns.add(pattern);
   }
   
   public void addIncludePatterns(Collection<Pattern> patterns)
   {
      _includePatterns.addAll(patterns);
   }
   
   public void addIncludePatternString(String pattern)
   {
      _includePatterns.add(Pattern.compile(pattern));
   }
   
   public void addIncludePatternStrings(Collection<String> patterns)
   {
      _includePatterns.addAll(bulkCompile(patterns));
   }
   
   public void addExcludePattern(Pattern pattern)
   {
      _excludePatterns.add(pattern);
   }
   
   public void addExcludePatterns(Collection<Pattern> patterns)
   {
      _excludePatterns.addAll(patterns);
   }
   
   public void addExcludePatternString(String pattern)
   {
      _excludePatterns.add(Pattern.compile(pattern));
   }
   
   public void addExcludePatternStrings(Collection<String> patterns)
   {
      _excludePatterns.addAll(bulkCompile(patterns));
   }
   
   public List<Pattern> getIncludePatterns()
   {
      return _includePatterns;
   }

   public List<Pattern> getExcludePatterns()
   {
      return _excludePatterns;
   }

   public boolean accepts(CharSequence charSequence)
   {
      return 
         (_includePatterns.isEmpty() || matchesAny(_includePatterns, charSequence))
         && !matchesAny(_excludePatterns, charSequence);
   }
   
   private static boolean matchesAny(List<Pattern> patterns, CharSequence charSequence)
   {
      for(Pattern pattern : patterns)
      {
         if(pattern.matcher(charSequence).matches())
         {
            return true;
         }
      }
      
      return false;
   }
   
   private static Set<Pattern> bulkCompile(Collection<String> patterns)
   {
      final Set<Pattern> result = new HashSet<Pattern>();
      for(String pattern : patterns)
      {
         result.add(Pattern.compile(pattern));
      }
      
      return result;
   }
}
