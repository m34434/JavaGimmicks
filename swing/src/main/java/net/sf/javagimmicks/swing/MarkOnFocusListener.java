package net.sf.javagimmicks.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/**
 * A {@link FocusListener} that can be installed on a {@link JTextComponent}
 * which marks the whole text content upon focus gain.
 */
public class MarkOnFocusListener implements FocusListener
{
   /**
    * Installs a new {@link MarkOnFocusListener} on a given
    * {@link JTextComponent}.
    * 
    * @param textComponent
    *           the {@link JTextComponent} where to install a new
    *           {@link MarkOnFocusListener}
    */
   public static void install(final JTextComponent textComponent)
   {
      textComponent.addFocusListener(new MarkOnFocusListener(textComponent));
   }

   private final JTextComponent _textComponent;

   /**
    * Creates a new instance for the given {@link JTextComponent}.
    * 
    * @param textComponent
    *           the {@link JTextComponent} where to mark the text content upon
    *           focus gain
    */
   public MarkOnFocusListener(final JTextComponent textComponent)
   {
      _textComponent = textComponent;
   }

   @Override
   public void focusGained(final FocusEvent e)
   {
      final int length = _textComponent.getDocument().getLength();

      if (length > 0)
      {
         _textComponent.setSelectionStart(0);
         _textComponent.setSelectionEnd(length);
      }
   }

   @Override
   public void focusLost(final FocusEvent e)
   {}
}
