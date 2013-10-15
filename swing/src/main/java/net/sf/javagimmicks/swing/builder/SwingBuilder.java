package net.sf.javagimmicks.swing.builder;

import javax.swing.JPanel;

/**
 * The root class for creating a new Swing UI - acutually a special
 * {@link PanelBuilder}.
 */
public class SwingBuilder extends PanelBuilder<SwingBuilder>
{
   /**
    * Creates a new instance with a new internal {@link JPanel}.
    */
   public SwingBuilder()
   {
      super(null, new JPanel());
   }
}