package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

   /**
    * Unzips a given ZIP file {@link InputStream} into a given target
    * {@link File folder}.
    * <p>
    * The given target {@link File folder} must either exist (and must really be
    * a directory) or is must be producible by the calling application.
    * 
    * @param zipFile
    *           the ZIP file to unzip as {@link InputStream}
    * @param targetFolder
    *           the target {@link File folder} where to unzip the files
    * @throws IOException
    *            if any internal file operation fails
    * @throws {@link IllegalArgumentException} the given target {@link File
    *         folder} is not a directory
    */
   public static void unzip(final InputStream zipFile, final File targetFolder) throws IOException,
         IllegalArgumentException
   {
      if (targetFolder.exists())
      {
         if (!targetFolder.isDirectory())
         {
            throw new IllegalArgumentException(String.format(
                  "Given existing target folder '%1$s' is not a directory!", targetFolder));
         }
      }
      else
      {
         if (!targetFolder.mkdirs())
         {
            throw new IOException(String.format(
                  "Could not create target folder '%1$s'!", targetFolder));
         }
      }

      final ZipInputStream zis = new ZipInputStream(zipFile);
      final byte[] buffer = new byte[1024];

      try
      {
         for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry())
         {
            try
            {
               if (entry.isDirectory())
               {
                  new File(targetFolder, entry.getName()).mkdirs();
               }
               else
               {
                  final File targetFile = new File(targetFolder, entry.getName());
                  targetFile.getParentFile().mkdirs();

                  final FileOutputStream fos = new FileOutputStream(targetFile);
                  try
                  {
                     for (int len = zis.read(buffer); len > 0; len = zis.read(buffer))
                     {
                        fos.write(buffer, 0, len);
                     }
                  }
                  finally
                  {
                     fos.close();
                  }
               }
            }
            finally
            {
               zis.closeEntry();
            }
         }
      }
      finally
      {
         zis.close();
      }
   }

   /**
    * Unzips a given ZIP {@link File} into a given target {@link File folder}.
    * <p>
    * The given target {@link File folder} must either exist (and must really be
    * a directory) or is must be producible by the calling application.
    * 
    * @param zipFile
    *           the ZIP {@link File} to unzip
    * @param targetFolder
    *           the target {@link File folder} where to unzip the files
    * @throws IOException
    *            if any internal file operation fails
    * @throws {@link IllegalArgumentException} if the given ZIP {@link File} is
    *         not valid or the given target {@link File folder} is not a
    *         directory
    */
   public static void unzip(final File zipFile, final File targetFolder) throws IOException, IllegalArgumentException
   {
      if (zipFile == null || !zipFile.exists() || !zipFile.isFile())
      {
         throw new IllegalArgumentException(String.format(
               "Given ZIP file '%1$s' is null, does not exist or is not a file!", zipFile));
      }

      unzip(new FileInputStream(zipFile), targetFolder);
   }
}
