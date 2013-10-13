package net.sf.javagimmicks.swing.builder;

import javax.swing.JPanel;

public class SwingBuilder extends PanelBuilder<SwingBuilder>
{
   public SwingBuilder()
   {
      super(null, new JPanel());
   }
}