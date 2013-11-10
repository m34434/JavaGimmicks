package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.sf.javagimmicks.io.FileTraverser;
import net.sf.javagimmicks.io.FileTraverser.TypeFilter;
import net.sf.javagimmicks.io.FileTraverser.FileVisitor;
import net.sf.javagimmicks.io.FileUtils;
import net.sf.javagimmicks.io.folderdiff.FileInfo.Origin;

class FileScanner
{
   private final Origin _origin;

   private final FolderDiffBuilder _builder;

   private final FilenameFilter _filter;
   private final File _rootFile;
   private final boolean _recursive;

   public FileScanner(final Origin origin, final FolderDiffBuilder builder, final File rootFile,
         final FilenameFilter filter,
         final boolean recursive)
   {
      if (rootFile == null)
      {
         throw new IllegalArgumentException("Root file may not be null!");
      }

      _origin = origin;

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

         final FileVisitor scanVisitor = new FileVisitor()
         {
            @Override
            public void visit(final File file)
            {
               if (file.isDirectory())
               {
                  _builder.fireEvent(new FolderDiffEvent(_builder, file));
               }
               result.add(new FileInfo(file, skipSegments, _origin));
            }
         };

         final FileTraverser traverser = new FileTraverser(_rootFile);
         traverser.setFilenameFilter(_filter);
         traverser.setTypeFilter(TypeFilter.BOTH);
         traverser.setRecursive(_recursive);

         traverser.run(scanVisitor);
      }

      return result;
   }
}
