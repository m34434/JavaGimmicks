package net.sf.javagimmicks.swing.model;

import java.awt.Color;

import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * A specialized {@link OutputStreamDocumentAdapter} that applies a given
 * {@link Color} to the up-streamed content in the wrapped {@link Document}.
 */
public class ColoredOutputStreamDocumentAdapter extends OutputStreamDocumentAdapter
{
   private final SimpleAttributeSet _attributes;

   /**
    * Creates a new instance for the given {@link Document} and {@link Color}.
    * 
    * @param document
    *           the {@link Document} where to output up-streamed content
    * @param color
    *           the {@link Color} to apply to the content
    */
   public ColoredOutputStreamDocumentAdapter(final Document document, final Color color)
   {
      super(document);

      _attributes = new SimpleAttributeSet();
      StyleConstants.setForeground(_attributes, color);
   }

   @Override
   protected void applyText(final Document document, final String text) throws Exception
   {
      document.insertString(document.getLength(), text, _attributes);
   }
}
