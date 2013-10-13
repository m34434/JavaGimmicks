package net.sf.javagimmicks.swing.builder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * A builder for setting up any {@link JComponent} with a fluent API.
 * 
 * @param <Component>
 *           the type of {@link JComponent} to setup
 * @param <Parent>
 *           the type of the parent {@link PanelBuilder} that created this
 *           {@link ComponentBuilder}
 */
public class ComponentBuilder<Component extends JComponent, Parent extends PanelBuilder<?>>
{
   private final Parent _parentBuilder;
   private final Component _component;

   ComponentBuilder(final Parent parentBuilder, final Component component)
   {
      _parentBuilder = parentBuilder;
      _component = component;
   }

   /**
    * Finishes setting up the current {@link JComponent} and moves back to the
    * parent {@link PanelBuilder}.
    * 
    * @return the parent {@link PanelBuilder} that created this
    *         {@link ComponentBuilder}
    */
   public Parent finish()
   {
      return _parentBuilder;
   }

   /**
    * Returns the currently built {@link JComponent}.
    * 
    * @return the currently built {@link JComponent}.
    */
   public Component get()
   {
      return _component;
   }

   /**
    * Applies a {@link Border} to the currently built {@link JComponent}.
    * 
    * @param border
    *           the {@link Border} to apply
    * @return the current builder instance
    * @see JComponent#setBorder(Border)
    */
   public ComponentBuilder<Component, Parent> border(final Border border)
   {
      get().setBorder(border);

      return this;
   }

   /**
    * Applies a {@link Font} to the currently built {@link JComponent}.
    * 
    * @param font
    *           the {@link Font} to apply
    * @return the current builder instance
    * @see JComponent#setFont(Font)
    */
   public ComponentBuilder<Component, Parent> font(final Font font)
   {
      get().setFont(font);

      return this;
   }

   /**
    * Enables or disables the currently built {@link JComponent}.
    * 
    * @param enabled
    *           if the currently built {@link JComponent} should be enabled or
    *           disabled
    * @return the current builder instance
    * @see JComponent#setEnabled(boolean)
    */
   public ComponentBuilder<Component, Parent> enabled(final boolean enabled)
   {
      get().setEnabled(enabled);

      return this;
   }

   /**
    * Applies a given background {@link Color} to the currently built
    * {@link JComponent}.
    * 
    * @param color
    *           the background {@link Color} to apply
    * @return the current builder instance
    * @see JComponent#setBackground(Color)
    */
   public ComponentBuilder<Component, Parent> background(final Color color)
   {
      get().setBackground(color);

      return this;
   }

   /**
    * Applies a given foreground {@link Color} to the currently built
    * {@link JComponent}.
    * 
    * @param color
    *           the foreground {@link Color} to apply
    * @return the current builder instance
    * @see JComponent#setForeground(Color)
    */
   public ComponentBuilder<Component, Parent> foreground(final Color color)
   {
      get().setForeground(color);

      return this;
   }

   /**
    * Add a given {@link FocusListener} to the currently built
    * {@link JComponent}.
    * 
    * @param focusListener
    *           the {@link FocusListener} to add
    * @return the current builder instance
    * @see JComponent#addFocusListener(FocusListener)
    */
   public ComponentBuilder<Component, Parent> focusListener(final FocusListener focusListener)
   {
      get().addFocusListener(focusListener);

      return this;
   }

   /**
    * Add a given {@link KeyListener} to the currently built {@link JComponent}.
    * 
    * @param keyListener
    *           the {@link KeyListener} to add
    * @return the current builder instance
    * @see JComponent#addKeyListener(KeyListener)
    */
   public ComponentBuilder<Component, Parent> keyListener(final KeyListener keyListener)
   {
      get().addKeyListener(keyListener);

      return this;
   }

   /**
    * Add a given {@link MouseListener} to the currently built
    * {@link JComponent}.
    * 
    * @param mouseListener
    *           the {@link MouseListener} to add
    * @return the current builder instance
    * @see JComponent#addMouseListener(MouseListener)
    */
   public ComponentBuilder<Component, Parent> mouseListener(final MouseListener mouseListener)
   {
      get().addMouseListener(mouseListener);

      return this;
   }

   /**
    * Add a given {@link PropertyChangeListener} to the currently built
    * {@link JComponent}.
    * 
    * @param propertyChangeListener
    *           the {@link PropertyChangeListener} to add
    * @return the current builder instance
    * @see JComponent#addPropertyChangeListener(PropertyChangeListener)
    */
   public ComponentBuilder<Component, Parent> propertyChangeListener(final PropertyChangeListener propertyChangeListener)
   {
      get().addPropertyChangeListener(propertyChangeListener);

      return this;
   }

   protected Parent getParent()
   {
      return _parentBuilder;
   }
}