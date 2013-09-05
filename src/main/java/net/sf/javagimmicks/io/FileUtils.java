package net.sf.javagimmicks.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class FileUtils
{
   public static List<String> getPathSegments(File file)
   {
      List<String> segements = Arrays.asList(file.getAbsoluteFile().toURI().getPath().split("/"));

      return segements.subList(1, segements.size());
   }

   public static long getChecksum(File file)
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

         byte[] tempBuf = new byte[128];

         while (cis.read(tempBuf) >= 0);

         return cis.getChecksum().getValue();
      }
      catch (IOException e)
      {
         return 0L;
      }
      finally
      {
         if(cis != null)
         {
            try
            {
               cis.close();
            }
            catch (IOException e) {}
         }
      }
   }
}
