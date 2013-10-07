package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;
import net.sf.javagimmicks.io.folderdiff.event.FolderDiffEvent;

class FileScanner
{
   private final FolderDiffBuilder _builder;

   private final FilenameFilter _filter;
   private final File _rootFile;
   private final boolean _recursive;

   public FileScanner(final FolderDiffBuilder builder, final File rootFile, final FilenameFilter filter,
         final boolean recursive)
   {
      if (rootFile == null)
      {
         throw new IllegalArgumentException("Root file may not be null!");
      }

      _builder = builder;

      _rootFile = rootFile;
      _filter = filter;
      _recursive = recursive;
   }

   public List<FileInfo> scan()
   {
      final ArrayList<FileInfo> result = new ArrayList<FileInfo>();

      if (_rootFile.isDirectory())
      {
         final int skipSegments = FileUtils.getPathSegments(_rootFile).size();

         scanInternal(result, _rootFile, skipSegments);
      }

      return result;
   }

   private void scanInternal(final List<FileInfo> result, final File currentDir, final int skipSegments)
   {
      _builder.fireEvent(new FolderDiffEvent(_builder, currentDir));

      final File[] children = currentDir.listFiles(_filter);

      for (final File child : children)
      {
         if (child.isDirectory())
         {
            result.add(new FileInfo(child, skipSegments));

            if (_recursive)
            {
               scanInternal(result, child, skipSegments);
            }
         }
      }

      for (final File childFile : children)
      {
         if (!childFile.isDirectory())
         {
            result.add(new FileInfo(childFile, skipSegments));
         }
      }
   }
}
