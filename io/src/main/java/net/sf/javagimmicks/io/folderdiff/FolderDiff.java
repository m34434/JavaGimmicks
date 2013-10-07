package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.SortedSet;

/**
 * A model class representing the results of a folder comparison.
 */
public class FolderDiff
{
   private final File _sourceFolder;
   private final File _targetFolder;

   private final SortedSet<PathInfo> _filesDifferent;
   private final SortedSet<PathInfo> _filesSourceOnly;
   private final SortedSet<PathInfo> _filesTargetOnly;
   private final SortedSet<PathInfo> _filesEqual;
   private final SortedSet<PathInfo> _filesAll;

   FolderDiff(final File sourceFolder, final File targetFolder,
         final SortedSet<PathInfo> all, final SortedSet<PathInfo> equal,
         final SortedSet<PathInfo> different, final SortedSet<PathInfo> sourceOnly,
         final SortedSet<PathInfo> targetOnly)
   {
      _sourceFolder = sourceFolder;
      _targetFolder = targetFolder;

      _filesDifferent = different;
      _filesSourceOnly = sourceOnly;
      _filesTargetOnly = targetOnly;
      _filesEqual = equal;
      _filesAll = all;
   }

   /**
    * Returns the source folder (as {@link File} object) that was used for the
    * comparison.
    * 
    * @return the source folder (as {@link File} object) that was used for the
    *         comparison
    */
   public File getSourceFolder()
   {
      return _sourceFolder;
   }

   /**
    * Returns the target folder (as {@link File} object) that was used for the
    * comparison.
    * 
    * @return the target folder (as {@link File} object) that was used for the
    *         comparison
    */
   public File getTargetFolder()
   {
      return _targetFolder;
   }

   /**
    * Returns the paths (as {@link PathInfo} objects) of all {@link File}s that
    * are different in the {@link #getSourceFolder() source folder} and
    * {@link #getTargetFolder() target folder}.
    * 
    * @return the {@link PathInfo}s of all different {@link File}s
    */
   public SortedSet<PathInfo> getDifferent()
   {
      return _filesDifferent;
   }

   /**
    * Returns the paths (as {@link PathInfo} objects) of all {@link File}s that
    * are only present in the {@link #getSourceFolder() source folder}.
    * 
    * @return the {@link PathInfo}s of all {@link File}s only present in the
    *         {@link #getSourceFolder() source folder}
    */
   public SortedSet<PathInfo> getSourceOnly()
   {
      return _filesSourceOnly;
   }

   /**
    * Returns the paths (as {@link PathInfo} objects) of all {@link File}s that
    * are only present in the {@link #getTargetFolder() target folder}.
    * 
    * @return the {@link PathInfo}s of all {@link File}s only present in the
    *         {@link #getTargetFolder() target folder}
    */
   public SortedSet<PathInfo> getTargetOnly()
   {
      return _filesTargetOnly;
   }

   /**
    * Returns the paths (as {@link PathInfo} objects) of all {@link File}s that
    * are equal in the {@link #getSourceFolder() source folder} and
    * {@link #getTargetFolder() target folder}.
    * 
    * @return the {@link PathInfo}s of all equal {@link File}s
    */
   public SortedSet<PathInfo> getEqual()
   {
      return _filesEqual;
   }

   /**
    * Returns the paths (as {@link PathInfo} objects) of all {@link File}s (from
    * {@link #getSourceFolder() source folder} and/or {@link #getTargetFolder()
    * target folder}) that were compared.
    * 
    * @return the {@link PathInfo}s of all compared {@link File}s
    */
   public SortedSet<PathInfo> getAll()
   {
      return _filesAll;
   }
}
