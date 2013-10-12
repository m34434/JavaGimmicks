package net.sf.javagimmicks.swing;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 * An extension of {@link JTextPane} that can be configured to line-wrap the
 * contained text.
 */
public class JWrappingTextPane extends JTextPane
{
   private static final long serialVersionUID = -5932297203397634342L;

   private boolean _wrappingEnabled = false;

   /**
    * Creates a new instance.
    * 
    * @see JTextPane#JTextPane()
    */
   public JWrappingTextPane()
   {
      super();
   }

   /**
    * Creates a new instance for the given {@link StyledDocument}.
    * 
    * @param doc
    *           the {@link StyledDocument} that this instance should work on
    * @see JTextPane#JTextPane(StyledDocument)
    */
   public JWrappingTextPane(final StyledDocument doc)
   {
      super(doc);
   }

   /**
    * Returns if line-wrapping is enabled
    * 
    * @return if line-wrapping is enabled
    */
   public boolean isWrappingEnabled()
   {
      return _wrappingEnabled;
   }

   /**
    * Sets if line-wrapping is enabled
    * 
    * @param wrappingEnabled
    *           if line-wrapping is enabled
    */
   public void setWrappingEnabled(final boolean wrappingEnabled)
   {
      _wrappingEnabled = wrappingEnabled;
   }

   @Override
   public boolean getScrollableTracksViewportWidth()
   {
      return _wrappingEnabled ? (super.getScrollableTracksViewportWidth()) : false;
   }

   /**
    * Creates a new {@link JScrollPane} that contains this
    * {@link JWrappingTextPane} as component and additionally adjusts the
    * background color of the {@link JScrollPane} to that of this instance which
    * looks much better in {@link #setWrappingEnabled(boolean) wrapped} mode.
    * 
    * @return the resulting {@link JScrollPane}
    */
   public JScrollPane buildScrollPane()
   {
      final JScrollPane result = new JScrollPane(this);
      result.getViewport().setBackground(this.getBackground());

      return result;
   }
}
