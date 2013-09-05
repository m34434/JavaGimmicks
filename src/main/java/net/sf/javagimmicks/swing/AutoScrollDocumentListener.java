package net.sf.javagimmicks.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class AutoScrollDocumentListener implements DocumentListener
{
   public static void install(JTextComponent textComponent)
   {
      textComponent.getDocument().addDocumentListener(
            new AutoScrollDocumentListener(textComponent));
   }

   private final JTextComponent _textComponent;

   public AutoScrollDocumentListener(JTextComponent textComponent)
   {
      _textComponent = textComponent;
   }

   public void changedUpdate(DocumentEvent e)
   {
   }

   public void insertUpdate(DocumentEvent e)
   {
      setCaret(e);
   }

   public void removeUpdate(DocumentEvent e)
   {
      setCaret(e);
   }

   private void setCaret(DocumentEvent e)
   {
      _textComponent.setCaretPosition(e.getDocument().getLength());
   }
}
