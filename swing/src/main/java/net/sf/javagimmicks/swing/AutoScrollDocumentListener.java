package net.sf.javagimmicks.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * A {@link DocumentListener} that can be applied to a given
 * {@link JTextComponent} and enables auto scroll-down there upon changes to
 * it's internal {@link Document} by setting the caret to the last position.
 */
public class AutoScrollDocumentListener implements DocumentListener
{
   /**
    * Creates a new instance and installs it within the given
    * {@link JTextComponent}.
    * 
    * @param textComponent
    *           the {@link JTextComponent} where to install a new
    *           {@link AutoScrollDocumentListener}
    */
   public static void install(final JTextComponent textComponent)
   {
      textComponent.getDocument().addDocumentListener(
            new AutoScrollDocumentListener(textComponent));
   }

   private final JTextComponent _textComponent;

   /**
    * Creates a new instance for the given {@link JTextComponent} but without
    * registering itself there (this can be achieved with
    * {@link #install(JTextComponent)}).
    * 
    * @param textComponent
    *           the {@link JTextComponent} for which to create an instance
    */
   public AutoScrollDocumentListener(final JTextComponent textComponent)
   {
      _textComponent = textComponent;
   }

   @Override
   public void changedUpdate(final DocumentEvent e)
   {}

   @Override
   public void insertUpdate(final DocumentEvent e)
   {
      setCaret(e);
   }

   @Override
   public void removeUpdate(final DocumentEvent e)
   {
      setCaret(e);
   }

   private void setCaret(final DocumentEvent e)
   {
      _textComponent.setCaretPosition(e.getDocument().getLength());
   }
}
