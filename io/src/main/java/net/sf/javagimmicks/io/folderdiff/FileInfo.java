package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.List;

import net.sf.javagimmicks.io.FileUtils;

/**
 * A simple {@link File} wrapper that holds additional information relevant for
 * folder comparison.
 */
public class FileInfo
{
   /**
    * The origin (source or target) of the scanned {@link File}.
    */
   public static enum Origin
   {
      /**
       * The scanned {@link File} is from the source folder.
       */
      Source,

      /**
       * The scanned {@link File} is from the target folder.
       */
      Target
   }

   private final File _file;
   private long _checkSum;
   private final PathInfo _pathInfo;
   private final Origin _origin;

   FileInfo(final File file, final int skipFragments, final Origin origin)
   {
      _file = file;

      _pathInfo = new PathInfo(file, skipFragments);

      _origin = origin;
   }

   /**
    * Returns the {@link List} of file path fragments relative to the compare
    * root folder. Serves as a shortcut to {@link PathInfo#getPathFragments()}.
    * 
    * @return the {@link List} of file path fragments relative to the compare
    *         root folder
    * @see PathInfo#getPathFragments()
    */
   public List<String> getPathFragments()
   {
      return _pathInfo.getPathFragments();
   }

   /**
    * Returns if the underlying {@link File} is a directory.
    * 
    * @return if the underlying {@link File} is a directory
    */
   public boolean isDirectory()
   {
      return _file.isDirectory();
   }

   /**
    * Returns the size (byte count) of the underlying {@link File}.
    * 
    * @return the size (byte count) of the underlying {@link File}
    */
   public long getSize()
   {
      return _file.length();
   }

   /**
    * Returns the change date of the underlying {@link File}.
    * 
    * @return the change date of the underlying {@link File}
    */
   public long getLastModified()
   {
      return _file.lastModified();
   }

   /**
    * Returns the checksum of the underlying {@link File} using
    * {@link FileUtils#getChecksum(File)} for checksum calculation.
    * 
    * @return the checksum of the underlying {@link File}
    * @see FileUtils#getChecksum(File)
    */
   public long getChecksum()
   {
      if (isDirectory())
      {
         return 0L;
      }

      if (_checkSum == 0L)
      {
         _checkSum = FileUtils.getChecksum(_file);
      }

      return _checkSum;
   }

   /**
    * Returns the underlying {@link File}
    * 
    * @return
    */
   public File getOriginalFile()
   {
      return _file;
   }

   /**
    * Returns the {@link PathInfo} for the underlying {@link File}.
    * 
    * @return the {@link PathInfo} for the underlying {@link File}
    */
   public PathInfo getPathInfo()
   {
      return _pathInfo;
   }

   /**
    * Returns the {@link Origin} of the underlying {@link File}.
    * 
    * @return the {@link Origin} of the underlying {@link File}
    */
   public Origin getOrigin()
   {
      return _origin;
   }

   /**
    * Returns if the underlying {@link File}'s {@link #getOrigin() Origin} is
    * {@link Origin#Source}.
    * 
    * @return if the underlying {@link File}'s {@link #getOrigin() Origin} is
    *         {@link Origin#Source}
    */
   public boolean isSource()
   {
      return getOrigin() == Origin.Source;
   }

   /**
    * Returns if the underlying {@link File}'s {@link #getOrigin() Origin} is
    * {@link Origin#Target}.
    * 
    * @return if the underlying {@link File}'s {@link #getOrigin() Origin} is
    *         {@link Origin#Target}
    */
   public boolean isTarget()
   {
      return getOrigin() == Origin.Target;
   }

   @Override
   public boolean equals(final Object o)
   {
      if (this == o)
      {
         return true;
      }

      if (!(o instanceof FileInfo))
      {
         return false;
      }

      return _pathInfo.equals(((FileInfo) o)._pathInfo);
   }
}
