package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;

public class PathInfo
{
   private final List<String> _pathFragments;
   private final boolean _directory;

   PathInfo(File file, int skipFragments)
   {
      List<String> allSegments = FileUtils.getPathSegments(file);
      _pathFragments = new ArrayList<String>(allSegments.subList(skipFragments, allSegments.size()));
      _directory = file.isDirectory();
   }
   
   public List<String> getPathFragments()
   {
      return Collections.unmodifiableList(_pathFragments);
   }
   
   public boolean isDirectory()
   {
      return _directory;
   }
   
   public File applyToFolder(File folder)
   {
      for(String pathFragment : _pathFragments)
      {
         folder = new File(folder, pathFragment);
      }
      
      return folder;
   }
   
   public String toString()
   {
      StringBuilder result = new StringBuilder();
      
      for(String pathSegment : _pathFragments)
      {
         result.append(PATH_SEP).append(pathSegment);
      }
      
      return result.toString();
   }

   @Override
   public boolean equals(Object o)
   {
      if(this == o)
      {
         return true;
      }
      
      if(!(o instanceof PathInfo))
      {
         return false;
      }
      
      PathInfo other = (PathInfo)o;
      
      return isDirectory() == other.isDirectory() && _pathFragments.equals(other._pathFragments);
   }
   
   private static final String PATH_SEP = File.separator;
}
