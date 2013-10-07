package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;

/**
 * A supporting model class that holds some path information about a compared
 * {@link File}.
 */
public class PathInfo
{
   private final List<String> _pathFragments;
   private final boolean _directory;

   PathInfo(final File file, final int skipFragments)
   {
      final List<String> allSegments = FileUtils.getPathSegments(file);
      _pathFragments = new ArrayList<String>(allSegments.subList(skipFragments, allSegments.size()));
      _directory = file.isDirectory();
   }

   /**
    * Returns the {@link List} of file path fragments of a compared {@link File}
    * relative to the compare root folder.
    * 
    * @return the {@link List} of file path fragments of a compared {@link File}
    *         relative to the compare root folder
    */
   public List<String> getPathFragments()
   {
      return Collections.unmodifiableList(_pathFragments);
   }

   /**
    * Returns if the path determined by this instance is a directory.
    * 
    * @return if the path determined by this instance is a directory
    */
   public boolean isDirectory()
   {
      return _directory;
   }

   /**
    * Extends a given {@link File folder path} by the fragments of this instance
    * (see {@link #getPathFragments()}.
    * 
    * @param folder
    *           the {@link File folder path} to extend
    * @return the given {@link File folder path} extended by the path fragments
    *         of this instance
    * @see #getPathFragments()
    */
   public File applyToFolder(File folder)
   {
      for (final String pathFragment : _pathFragments)
      {
         folder = new File(folder, pathFragment);
      }

      return folder;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();

      for (final String pathSegment : _pathFragments)
      {
         result.append(PATH_SEP).append(pathSegment);
      }

      return result.toString();
   }

   @Override
   public boolean equals(final Object o)
   {
      if (this == o)
      {
         return true;
      }

      if (!(o instanceof PathInfo))
      {
         return false;
      }

      final PathInfo other = (PathInfo) o;

      return isDirectory() == other.isDirectory() && _pathFragments.equals(other._pathFragments);
   }

   private static final String PATH_SEP = File.separator;
}
