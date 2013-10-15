package net.sf.javagimmicks.swing.builder;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * An extension of {@link ComponentBuilder} for configuring {@link JButton}s.
 */
public class ButtonBuilder<P extends PanelBuilder<?>> extends ComponentBuilder<JButton, P>
{
   ButtonBuilder(final P parentBuilder, final JButton component)
   {
      super(parentBuilder, component);
   }

   /**
    * Applies the given text to the internally built {@link JButton}.
    * 
    * @param text
    *           the text to apply
    * @return the current builder instance
    */
   public ButtonBuilder<P> text(final String text)
   {
      get().setText(text);

      return this;
   }

   /**
    * Add the given {@link ActionListener} to the internally built
    * {@link JButton}.
    * 
    * @param listener
    *           the {@link ActionListener} to add
    * @return the current builder instance
    */
   public ButtonBuilder<P> actionListener(final ActionListener listener)
   {
      get().addActionListener(listener);

      return this;
   }
}