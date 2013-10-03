package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 * Provides utility methods for basic file handling.
 */
public class FileUtils
{
   private FileUtils()
   {}

   /**
    * Splits the path information of a given {@link File} and returns the
    * resulting segments as a {@link List} of {@link String}s.
    * <p>
    * The following algorithm is used internally:
    * <ul>
    * <li>Convert the given {@link File} into the absolute form</li>
    * <li>Convert it into a {@link URI} path (which is OS independent)</li>
    * <li>Split the {@link URI} path using {@code /} as a separator</li>
    * </ul>
    * 
    * @param file
    *           the {@link File} for which to determine the path segments
    * @return the {@link List} of path segments
    */
   public static List<String> getPathSegments(final File file)
   {
      final List<String> segements = Arrays.asList(file.getAbsoluteFile().toURI().getPath().split("/"));

      return segements.subList(1, segements.size());
   }

   /**
    * Returns the {@link Adler32} checksum of the contents of a given
    * {@link File}.
    * 
    * @param file
    *           the {@link File} to calculate the checksum from
    * @return the resulting checksum as {@code long} value
    */
   public static long getChecksum(final File file)
   {
      if (file.isDirectory())
      {
         return 0L;
      }

      CheckedInputStream cis = null;
      try
      {
         cis = new CheckedInputStream(
               new FileInputStream(file), new Adler32());

         final byte[] tempBuf = new byte[128];

         while (cis.read(tempBuf) >= 0);

         return cis.getChecksum().getValue();
      }
      catch (final IOException e)
      {
         return 0L;
      }
      finally
      {
         if (cis != null)
         {
            try
            {
               cis.close();
            }
            catch (final IOException e)
            {
            }
         }
      }
   }
}
