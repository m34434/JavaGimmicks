package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A simple visitor-style implementation for traversing {@link File}s within the
 * file system.
 */
public class FileTraverser
{
   private final File _root;

   private TypeFilter _typeFilter = TypeFilter.FILE;
   private FilenameFilter _filenameFilter;
   private boolean _recursive = true;

   /**
    * Creates a new instance for the given root folder.
    * 
    * @param root
    *           the root folder where to start traversing (must be a non-null
    *           {@link File} pointing to a directory)
    * @throws IllegalArgumentException
    *            if the given root is <code>null</code> or not a directory
    */
   public FileTraverser(final File root)
   {
      if (root == null)
      {
         throw new IllegalArgumentException("Root folder is null!");
      }

      if (!root.isDirectory())
      {
         throw new IllegalArgumentException("Given file is not a directory!");
      }

      _root = root;
   }

   /**
    * Starts a new run using the given {@link FileVisitor} to report visited
    * {@link File}s.
    * 
    * @param visitor
    *           the {@link FileVisitor} where to report visited {@link File}s
    */
   public void run(final FileVisitor visitor)
   {
      if (visitor == null)
      {
         return;
      }

      scan(_root, visitor);
   }

   /**
    * Returns the current {@link TypeFilter} which determines what types of
    * {@link File}s should be visited (files and/or folders).
    * 
    * @return the current {@link TypeFilter}
    */
   public TypeFilter getTypeFilter()
   {
      return _typeFilter;
   }

   /**
    * Sets a new {@link TypeFilter}.
    * 
    * @param typeFilter
    *           the new {@link TypeFilter} to set
    * @throws IllegalArgumentException
    *            if the given {@link TypeFilter} is <code>null</code>
    * @see FileTraverser#getTypeFilter()
    */
   public void setTypeFilter(final TypeFilter typeFilter)
   {
      if (typeFilter == null)
      {
         throw new IllegalArgumentException("TypeFilter may not be null!");
      }

      this._typeFilter = typeFilter;
   }

   /**
    * Returns the current {@link FilenameFilter} which determines what files
    * and/or folders should be visited.
    * <p>
    * <b>Note:</b> folders not included by this {@link FilenameFilter} will be
    * skipped completely by {@link #run(FileVisitor)} calls (not visited and not
    * recursed into if {@link #isRecursive()} is set).
    * 
    * @return the current {@link FilenameFilter}
    */
   public FilenameFilter getFilenameFilter()
   {
      return _filenameFilter;
   }

   public void setFilenameFilter(final FilenameFilter filenameFilter)
   {
      this._filenameFilter = filenameFilter;
   }

   public boolean isRecursive()
   {
      return _recursive;
   }

   public void setRecursive(final boolean recursive)
   {
      this._recursive = recursive;
   }

   private void scan(final File directory, final FileVisitor visitor)
   {
      final File[] children = directory.listFiles(_filenameFilter);

      for (final File child : children)
      {
         if (child.isFile() && _typeFilter.acceptsFiles())
         {
            visitor.visit(child);
         }
         else if (child.isDirectory() && _typeFilter.acceptsFolders())
         {
            visitor.visit(child);
         }
      }

      for (final File child : children)
      {
         if (child.isDirectory())
         {
            if (_typeFilter.acceptsFolders())
            {
               visitor.visit(child);
            }

            if (_recursive)
            {
               scan(child, visitor);
            }
         }
      }

      if (_typeFilter.acceptsFiles())
      {
         for (final File child : children)
         {
            if (child.isFile())
            {
               visitor.visit(child);
            }
         }
      }
   }

   public static interface FileVisitor
   {
      void visit(File file);
   }

   public static enum TypeFilter
   {
      FILE(true, false), FOLDER(false, true), BOTH(true, true);

      private final boolean _acceptsFiles;
      private final boolean _acceptsFolders;

      private TypeFilter(final boolean acceptsFiles, final boolean acceptsFolders)
      {
         _acceptsFiles = acceptsFiles;
         _acceptsFolders = acceptsFolders;
      }

      public boolean acceptsFiles()
      {
         return _acceptsFiles;
      }

      public boolean acceptsFolders()
      {
         return _acceptsFolders;
      }
   };
}
