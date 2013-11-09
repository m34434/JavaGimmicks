package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FilenameFilter;

public class FileTraverser
{
   private final File _root;

   private TypeFilter _typeFilter = TypeFilter.FILE;
   private FilenameFilter _filenameFilter;
   private boolean _recursive = true;

   public FileTraverser(final File root)
   {
      if (!root.isDirectory())
      {
         throw new IllegalArgumentException("Given file is not a directory!");
      }

      _root = root;
   }

   public void run(final Visitor visitor)
   {
      if (visitor == null)
      {
         return;
      }

      scan(_root, visitor);
   }

   public TypeFilter getTypeFilter()
   {
      return _typeFilter;
   }

   public void setTypeFilter(final TypeFilter typeFilter)
   {
      if (typeFilter == null)
      {
         throw new IllegalArgumentException("TypeFilter may not be null!");
      }

      this._typeFilter = typeFilter;
   }

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

   private void scan(final File directory, final Visitor visitor)
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

   public static interface Visitor
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
