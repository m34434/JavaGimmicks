package net.sf.javagimmicks.swing.builder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.border.Border;


public class ComponentBuilder<C extends JComponent, P extends PanelBuilder<?>>
{
   private final P _parentBuilder;
   private final C _component;
   
   ComponentBuilder(P parentBuilder, C component)
   {
      _parentBuilder = parentBuilder;
      _component = component;
   }
   
   public P finish()
   {
      return _parentBuilder;
   }
   
   public C get()
   {
      return _component;
   }

   public ComponentBuilder<C, P> border(Border border)
   {
      get().setBorder(border);
      
      return this;
   }
   
   public ComponentBuilder<C, P> font(Font font)
   {
      get().setFont(font);
      
      return this;
   }
   
   public ComponentBuilder<C, P> enabled(boolean enabled)
   {
      get().setEnabled(enabled);
      
      return this;
   }
   
   public ComponentBuilder<C, P> background(Color color)
   {
      get().setBackground(color);
      
      return this;
   }

   public ComponentBuilder<C, P> foreground(Color color)
   {
      get().setForeground(color);
      
      return this;
   }
   
   public ComponentBuilder<C, P> focusListener(FocusListener focusListener)
   {
      get().addFocusListener(focusListener);
      
      return this;
   }
   
   public ComponentBuilder<C, P> keyListener(KeyListener keyListener)
   {
      get().addKeyListener(keyListener);
      
      return this;
   }
   
   public ComponentBuilder<C, P> mouseListener(MouseListener mouseListener)
   {
      get().addMouseListener(mouseListener);
      
      return this;
   }
   
   public ComponentBuilder<C, P> propertyChangeListener(PropertyChangeListener propertyChangeListener)
   {
      get().addPropertyChangeListener(propertyChangeListener);
      
      return this;
   }
   
   protected P getParent()
   {
      return _parentBuilder;
   }
}