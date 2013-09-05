package net.sf.javagimmicks.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

public class MarkOnFocusListener implements FocusListener
{
   public static void install(JTextComponent textComponent)
   {
      textComponent.addFocusListener(new MarkOnFocusListener(textComponent));
   }

   private final JTextComponent _textComponent;

   public MarkOnFocusListener(JTextComponent textComponent)
   {
      _textComponent = textComponent;
   }

   public void focusGained(FocusEvent e)
   {
      int length = _textComponent.getDocument().getLength();

      if (length > 0)
      {
         _textComponent.setSelectionStart(0);
         _textComponent.setSelectionEnd(length);
      }
   }

   public void focusLost(FocusEvent e)
   {
   }
}
