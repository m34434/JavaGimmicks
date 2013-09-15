package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;

public class FileInfo
{
   private final File _file;
   private long _checkSum;
   private final PathInfo _pathInfo;

   FileInfo(File file, int skipFragments)
   {
      _file = file;

      _pathInfo = new PathInfo(file, skipFragments);
   }
   
   public List<String> getPathFragments()
   {
      return _pathInfo.getPathFragments();
   }

   public boolean isDirectory()
   {
      return _file.isDirectory();
   }
   
   public long getSize()
   {
      return _file.length();
   }

   public long getLastModified()
   {
      return _file.lastModified();
   }
   
   public long getChecksum()
   {
      if(isDirectory())
      {
         return 0L;
      }
      
      if(_checkSum == 0L)
      {
         _checkSum = FileUtils.getChecksum(_file);
      }
      
      return _checkSum;
   }
   
   public File getOriginalFile()
   {
      return _file;
   }
   
   public PathInfo getPathInfo()
   {
      return _pathInfo;
   }
   
   public boolean equals(Object o)
   {
      if(this == o)
      {
         return true;
      }
      
      if(!(o instanceof FileInfo))
      {
         return false;
      }
      
      return _pathInfo.equals(((FileInfo)o)._pathInfo);
   }
}
