package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * A {@link FileFilter} implementation that accepts {@link File}s based on a
 * given {@link FilenameFilter}. The {@link FilenameFilter} is applied to files
 * and/or folder depending on the given {@link Mode}.
 */
public class FilenameFilterFileFilterAdapter implements FileFilter
{
   private final FilenameFilter _filter;
   private final Mode _mode;

   /**
    * Creates a new instance for the given {@link FilenameFilter} and
    * {@link Mode}.
    * 
    * @param filter
    *           the {@link FilenameFilter} to use internally (may not be
    *           <code>null</code>)
    * @param mode
    *           the {@link Mode} which determines where to apply the
    *           {@link FilenameFilter} - i.e. files and/or folders (may not be
    *           <code>null</code>)
    * @throws IllegalArgumentException
    *            if one of the parameters is <code>null</code>
    */
   public FilenameFilterFileFilterAdapter(final FilenameFilter filter, final Mode mode)
   {
      if (filter == null)
      {
         throw new IllegalArgumentException("Predicate is null!");
      }

      if (mode == null)
      {
         throw new IllegalArgumentException("Mode is null!");
      }

      this._filter = filter;
      this._mode = mode;
   }

   /**
    * Returns the internal {@link FilenameFilter}.
    * 
    * @return the internal {@link FilenameFilter}
    */
   public FilenameFilter getFilenameFlter()
   {
      return _filter;
   }

   /**
    * Returns the internal {@link Mode} which determines to which {@link File}
    * types to apply the internal {@link FilenameFilter}.
    * 
    * @return the internal {@link Mode}
    */
   public Mode getMode()
   {
      return _mode;
   }

   @Override
   public boolean accept(final File file)
   {
      if (file.isDirectory())
      {
         return !_mode._applyToFolders || _filter.accept(file.getParentFile(), file.getName());
      }

      if (file.isFile())
      {
         return !_mode._applyToFiles || _filter.accept(file.getParentFile(), file.getName());
      }

      return false;
   }

   /**
    * Determines if the {@link FilenameFilter} should be applied to files and/or
    * folders.
    */
   public static enum Mode
   {
      /**
       * Apply the {@link FilenameFilter} only to files.
       */
      FILE(true, false),

      /**
       * Apply the {@link FilenameFilter} only to folders.
       */
      FOLDER(false, true),

      /**
       * Apply the {@link FilenameFilter} to files and folders.
       */
      BOTH(true, true);

      private final boolean _applyToFiles;
      private final boolean _applyToFolders;

      private Mode(final boolean applyToFiles, final boolean applyToFolders)
      {
         _applyToFiles = applyToFiles;
         _applyToFolders = applyToFolders;
      }
   }

}
