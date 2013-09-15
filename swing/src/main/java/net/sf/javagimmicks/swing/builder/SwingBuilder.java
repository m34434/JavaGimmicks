package net.sf.javagimmicks.swing.builder;

import javax.swing.JPanel;

public class SwingBuilder extends PanelBuilder<SwingBuilder>
{
   public static void main(String[] args)
   {
      new SwingBuilder();
   }
   
   public SwingBuilder()
   {
      super(null, new JPanel());
   }

   
}