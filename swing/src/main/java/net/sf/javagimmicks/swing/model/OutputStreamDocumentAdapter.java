package net.sf.javagimmicks.swing.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.text.Document;

/**
 * A special {@link OutputStream} that forwards up-streamed content to a given
 * {@link Document}.
 */
public class OutputStreamDocumentAdapter extends OutputStream
{
   protected final Document _document;
   private final ByteArrayOutputStream _buffer = new ByteArrayOutputStream();

   /**
    * Creates a new instance for the given {@link Document}.
    * 
    * @param document
    *           the {@link Document} where to output up-streamed content.
    */
   public OutputStreamDocumentAdapter(final Document document)
   {
      _document = document;
   }

   @Override
   public void flush() throws IOException
   {
      try
      {
         applyText(_document, harvestBuffer());
      }
      catch (final Exception e)
      {
         throw new IOException("Unable to write into internal Document: " + e.getMessage());
      }
   }

   @Override
   public void close() throws IOException
   {
      flush();
   }

   @Override
   public void write(final int b) throws IOException
   {
      _buffer.write(b);
   }

   @Override
   public void write(final byte[] b, final int off, final int len) throws IOException
   {
      _buffer.write(b, off, len);
   }

   @Override
   public void write(final byte[] b) throws IOException
   {
      _buffer.write(b);
   }

   protected void applyText(final Document document, final String text) throws Exception
   {
      _document.insertString(_document.getLength(), text, null);
   }

   private String harvestBuffer()
   {
      final byte[] arrBytes = _buffer.toByteArray();
      _buffer.reset();

      return new String(arrBytes);
   }
}