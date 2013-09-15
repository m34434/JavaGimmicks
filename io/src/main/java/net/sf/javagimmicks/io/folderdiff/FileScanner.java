package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;

class FileScanner
{
   private final FilenameFilter _filter;
   private final File _rootFile;
   private final boolean _recursive;
   
   private FolderDiffListener _listener;
   
   public FileScanner(File rootFile, FilenameFilter filter, boolean recursive)
   {
      if(rootFile == null)
      {
         throw new IllegalArgumentException("Root file may not be null!");
      }
      
      _rootFile = rootFile;
      _filter = filter;
      _recursive = recursive;
   }
   
   public void setFolderDiffListener(FolderDiffListener listener)
   {
      _listener = listener;
   }
   
   public List<FileInfo> scan()
   {
      ArrayList<FileInfo> result = new ArrayList<FileInfo>();
      
      if(_rootFile.isDirectory())
      {
         int skipSegments = FileUtils.getPathSegments(_rootFile).size();
         
         scanInternal(result, _rootFile, skipSegments);
      }

      return result;
   }
   
   private void scanInternal(List<FileInfo> result, File currentDir, int skipSegments)
   {
      if(_listener != null)
      {
         _listener.folderScanned(currentDir);
      }
      
      File[] children = currentDir.listFiles(_filter);
      
      for(File child : children)
      {
         if(child.isDirectory())
         {
            result.add(new FileInfo(child, skipSegments));
            
            if(_recursive)
            {
               scanInternal(result, child, skipSegments);
            }
         }
      }
      
      for(File childFile : children)
      {
         if(!childFile.isDirectory())
         {
            result.add(new FileInfo(childFile, skipSegments));
         }
      }
   }
}
