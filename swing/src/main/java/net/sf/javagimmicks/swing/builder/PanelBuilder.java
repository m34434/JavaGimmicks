package net.sf.javagimmicks.swing.builder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PanelBuilder<P extends PanelBuilder<?>> extends ComponentBuilder<JPanel, P>
{
   PanelBuilder(P parentBuilder, JPanel panel)
   {
      super(parentBuilder, panel);
   }

   public PanelBuilder<PanelBuilder<P>> panel()
   {
      return new PanelBuilder<PanelBuilder<P>>(this, new JPanel());
   }

   public PanelBuilder<PanelBuilder<P>> panel(LayoutManager layoutManager)
   {
      return new PanelBuilder<PanelBuilder<P>>(this, new JPanel(layoutManager));
   }
   
   public PanelBuilder<P> layout(LayoutManager layoutManager)
   {
      get().setLayout(layoutManager);
      
      return this;
   }
   
   public PanelBuilder<P> borderLayout()
   {
      return layout(new BorderLayout());
   }
   
   public PanelBuilder<P> borderLayout(int hgap, int vgap)
   {
      return layout(new BorderLayout(hgap, vgap));
   }
   
   public PanelBuilder<P> boxLayout(int axis)
   {
      return layout(new BoxLayout(get(), axis));
   }
   
   public PanelBuilder<P> boxLayoutX()
   {
      return layout(new BoxLayout(get(), BoxLayout.X_AXIS));
   }
   
   public PanelBuilder<P> boxLayoutY()
   {
      return layout(new BoxLayout(get(), BoxLayout.Y_AXIS));
   }
   
   public PanelBuilder<P> boxLayoutLine()
   {
      return layout(new BoxLayout(get(), BoxLayout.LINE_AXIS));
   }
   
   public PanelBuilder<P> boxLayoutPage()
   {
      return layout(new BoxLayout(get(), BoxLayout.PAGE_AXIS));
   }
   
   public PanelBuilder<P> flowLayout()
   {
      return layout(new FlowLayout());
   }
   
   public PanelBuilder<P> flowLayout(int align)
   {
      return layout(new FlowLayout(align));
   }
   
   public PanelBuilder<P> flowLayout(int align, int hgap, int vgap)
   {
      return layout(new FlowLayout(align, hgap, vgap));
   }
   
   public PanelBuilder<P> gridBagLayout()
   {
      return layout(new GridBagLayout());
   }
   
   public PanelBuilder<P> gridLayout()
   {
      return layout(new GridLayout());
   }
   
   public PanelBuilder<P> gridLayout(int rows, int cols)
   {
      return layout(new GridLayout(rows, cols));
   }
   
   public PanelBuilder<P> gridLayout(int rows, int cols, int hgap, int vgap)
   {
      return layout(new GridLayout(rows, cols, hgap, vgap));
   }
   
   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<P>> component(C component)
   {
      get().add(component);

      return new ComponentBuilder<C, PanelBuilder<P>>(this, component);
   }

   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<P>> component(C component, Object constraints)
   {
      get().add(component, constraints);
      
      return new ComponentBuilder<C, PanelBuilder<P>>(this, component);
   }

   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<P>> component(C component, Object constraints, int index)
   {
      get().add(component, constraints, index);
      
      return new ComponentBuilder<C, PanelBuilder<P>>(this, component);
   }

   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<P>> component(C component, int index)
   {
      get().add(component, index);
      
      return new ComponentBuilder<C, PanelBuilder<P>>(this, component);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(String text)
   {
      JButton button = new JButton(text);
      get().add(button);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(Action action)
   {
      JButton button = new JButton(action);
      get().add(button);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<P>> button(String text, Object contraints)
   {
      JButton button = new JButton(text);
      get().add(button, contraints);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(Action action, Object contraints)
   {
      JButton button = new JButton(action);
      get().add(button, contraints);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(String text, Object contraints, int index)
   {
      JButton button = new JButton(text);
      get().add(button, contraints, index);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(Action action, Object contraints, int index)
   {
      JButton button = new JButton(action);
      get().add(button, contraints, index);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(String text, int index)
   {
      JButton button = new JButton(text);
      get().add(button, index);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
   
   public ButtonBuilder<PanelBuilder<P>> button(Action action, int index)
   {
      JButton button = new JButton(action);
      get().add(button, index);
      
      return new ButtonBuilder<PanelBuilder<P>>(this, button);
   }
}