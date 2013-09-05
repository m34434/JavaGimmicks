package net.sf.javagimmicks.swing.builder;

import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ButtonBuilder<P extends PanelBuilder<?>> extends ComponentBuilder<JButton, P>
{
   ButtonBuilder(P parentBuilder, JButton component)
   {
      super(parentBuilder, component);
   }
   
   public ButtonBuilder<P> text(String text)
   {
      get().setText(text);
      
      return this;
   }
   
   public ButtonBuilder<P> actionListener(ActionListener listener)
   {
      get().addActionListener(listener);
      
      return this;
   }
}