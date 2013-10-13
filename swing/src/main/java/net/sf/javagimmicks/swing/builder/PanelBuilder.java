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

/**
 * A builder for setting up any {@link JPanel} with a fluent API.
 * 
 * @param <Parent>
 *           the type of the parent {@link PanelBuilder} that created this
 *           {@link PanelBuilder}
 */
public class PanelBuilder<Parent extends PanelBuilder<?>> extends ComponentBuilder<JPanel, Parent>
{
   PanelBuilder(final Parent parentBuilder, final JPanel panel)
   {
      super(parentBuilder, panel);
   }

   /**
    * Adds a new child {@link JPanel} within the currently built {@link JPanel}
    * and returns the {@link PanelBuilder} for it.
    * 
    * @return the {@link PanelBuilder} for the created child {@link JPanel}
    */
   public PanelBuilder<PanelBuilder<Parent>> panel()
   {
      return new PanelBuilder<PanelBuilder<Parent>>(this, new JPanel());
   }

   /**
    * Adds a new child {@link JPanel} within the currently built {@link JPanel}
    * and returns the {@link PanelBuilder} for it.
    * 
    * @param layoutManager
    *           the {@link LayoutManager} to apply in the created child
    *           {@link JPanel}
    * @return the {@link PanelBuilder} for the created child {@link JPanel}
    */
   public PanelBuilder<PanelBuilder<Parent>> panel(final LayoutManager layoutManager)
   {
      return new PanelBuilder<PanelBuilder<Parent>>(this, new JPanel(layoutManager));
   }

   /**
    * Applies the given {@link LayoutManager} to the currently built
    * {@link JPanel}.
    * 
    * @param layoutManager
    *           the {@link LayoutManager} to apply
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    */
   public PanelBuilder<Parent> layout(final LayoutManager layoutManager)
   {
      get().setLayout(layoutManager);

      return this;
   }

   /**
    * Applies a new {@link BorderLayout} to the currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BorderLayout#BorderLayout()
    */
   public PanelBuilder<Parent> borderLayout()
   {
      return layout(new BorderLayout());
   }

   /**
    * Applies a new {@link BorderLayout} to the currently built {@link JPanel}.
    * 
    * @param hgap
    *           the horizontal gap for the new {@link BorderLayout}
    * @param vgap
    *           the vertical gap for the new {@link BorderLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BorderLayout#BorderLayout(int, int)
    */
   public PanelBuilder<Parent> borderLayout(final int hgap, final int vgap)
   {
      return layout(new BorderLayout(hgap, vgap));
   }

   /**
    * Applies a new {@link BoxLayout} to the currently built {@link JPanel}.
    * 
    * @param axis
    *           the axis settings to apply on the new {@link BoxLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BoxLayout#BoxLayout(java.awt.Container, int)
    */
   public PanelBuilder<Parent> boxLayout(final int axis)
   {
      return layout(new BoxLayout(get(), axis));
   }

   /**
    * Applies a new {@link BoxLayout} in {@link BoxLayout#X_AXIS} mode to the
    * currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BoxLayout#BoxLayout(java.awt.Container, int)
    * @see BoxLayout#X_AXIS
    */
   public PanelBuilder<Parent> boxLayoutX()
   {
      return layout(new BoxLayout(get(), BoxLayout.X_AXIS));
   }

   /**
    * Applies a new {@link BoxLayout} in {@link BoxLayout#Y_AXIS} mode to the
    * currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BoxLayout#BoxLayout(java.awt.Container, int)
    * @see BoxLayout#Y_AXIS
    */
   public PanelBuilder<Parent> boxLayoutY()
   {
      return layout(new BoxLayout(get(), BoxLayout.Y_AXIS));
   }

   /**
    * Applies a new {@link BoxLayout} in {@link BoxLayout#LINE_AXIS} mode to the
    * currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BoxLayout#BoxLayout(java.awt.Container, int)
    * @see BoxLayout#LINE_AXIS
    */
   public PanelBuilder<Parent> boxLayoutLine()
   {
      return layout(new BoxLayout(get(), BoxLayout.LINE_AXIS));
   }

   /**
    * Applies a new {@link BoxLayout} in {@link BoxLayout#PAGE_AXIS} mode to the
    * currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see BoxLayout#BoxLayout(java.awt.Container, int)
    * @see BoxLayout#PAGE_AXIS
    */
   public PanelBuilder<Parent> boxLayoutPage()
   {
      return layout(new BoxLayout(get(), BoxLayout.PAGE_AXIS));
   }

   /**
    * Applies a new {@link FlowLayout} to the currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see FlowLayout#FlowLayout()
    */
   public PanelBuilder<Parent> flowLayout()
   {
      return layout(new FlowLayout());
   }

   /**
    * Applies a new {@link FlowLayout} to the currently built {@link JPanel}.
    * 
    * @param align
    *           the alignment for the new {@link FlowLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see FlowLayout#FlowLayout(int)
    */
   public PanelBuilder<Parent> flowLayout(final int align)
   {
      return layout(new FlowLayout(align));
   }

   /**
    * Applies a new {@link FlowLayout} to the currently built {@link JPanel}.
    * 
    * @param align
    *           the alignment for the new {@link FlowLayout}
    * @param hgap
    *           the horizontal gap for the new {@link FlowLayout}
    * @param vgap
    *           the vertical gap for the new {@link FlowLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see FlowLayout#FlowLayout(int, int, int)
    */
   public PanelBuilder<Parent> flowLayout(final int align, final int hgap, final int vgap)
   {
      return layout(new FlowLayout(align, hgap, vgap));
   }

   /**
    * Applies a new {@link GridBagLayout} to the currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see GridBagLayout#GridBagLayout()
    */
   public PanelBuilder<Parent> gridBagLayout()
   {
      return layout(new GridBagLayout());
   }

   /**
    * Applies a new {@link GridLayout} to the currently built {@link JPanel}.
    * 
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see GridLayout#GridLayout()
    */
   public PanelBuilder<Parent> gridLayout()
   {
      return layout(new GridLayout());
   }

   /**
    * Applies a new {@link GridLayout} to the currently built {@link JPanel}.
    * 
    * @param rows
    *           the number of rows for the new {@link GridLayout}
    * @param cols
    *           the number of columns for the new {@link GridLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see GridLayout#GridLayout(int, int))
    */
   public PanelBuilder<Parent> gridLayout(final int rows, final int cols)
   {
      return layout(new GridLayout(rows, cols));
   }

   /**
    * Applies a new {@link GridLayout} to the currently built {@link JPanel}.
    * 
    * @param rows
    *           the number of rows for the new {@link GridLayout}
    * @param cols
    *           the number of columns for the new {@link GridLayout}
    * @param hgap
    *           the horizontal gap for the new {@link GridLayout}
    * @param vgap
    *           the vertical gap for the new {@link GridLayout}
    * @return the current builder instance
    * @see JPanel#setLayout(LayoutManager)
    * @see GridLayout#GridLayout(int, int))
    */
   public PanelBuilder<Parent> gridLayout(final int rows, final int cols, final int hgap, final int vgap)
   {
      return layout(new GridLayout(rows, cols, hgap, vgap));
   }

   /**
    * Adds a new given {@link JComponent} to the currently built {@link JPanel}
    * and returns a {@link ComponentBuilder} for it.
    * 
    * @param component
    *           the {@link JComponent} to add
    * @param <Component>
    *           the concrete type of the {@link JComponent} to add
    * @return the {@link ComponentBuilder} for the added {@link JComponent}
    */
   public <Component extends JComponent> ComponentBuilder<Component, PanelBuilder<Parent>> component(
         final Component component)
   {
      get().add(component);

      return new ComponentBuilder<Component, PanelBuilder<Parent>>(this, component);
   }

   public <Component extends JComponent> ComponentBuilder<Component, PanelBuilder<Parent>> component(
         final Component component,
         final Object constraints)
   {
      get().add(component, constraints);

      return new ComponentBuilder<Component, PanelBuilder<Parent>>(this, component);
   }

   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<Parent>> component(final C component,
         final Object constraints, final int index)
   {
      get().add(component, constraints, index);

      return new ComponentBuilder<C, PanelBuilder<Parent>>(this, component);
   }

   public <C extends JComponent> ComponentBuilder<C, PanelBuilder<Parent>> component(final C component, final int index)
   {
      get().add(component, index);

      return new ComponentBuilder<C, PanelBuilder<Parent>>(this, component);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final String text)
   {
      final JButton button = new JButton(text);
      get().add(button);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final Action action)
   {
      final JButton button = new JButton(action);
      get().add(button);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final String text, final Object contraints)
   {
      final JButton button = new JButton(text);
      get().add(button, contraints);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final Action action, final Object contraints)
   {
      final JButton button = new JButton(action);
      get().add(button, contraints);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final String text, final Object contraints, final int index)
   {
      final JButton button = new JButton(text);
      get().add(button, contraints, index);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final Action action, final Object contraints, final int index)
   {
      final JButton button = new JButton(action);
      get().add(button, contraints, index);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final String text, final int index)
   {
      final JButton button = new JButton(text);
      get().add(button, index);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }

   public ButtonBuilder<PanelBuilder<Parent>> button(final Action action, final int index)
   {
      final JButton button = new JButton(action);
      get().add(button, index);

      return new ButtonBuilder<PanelBuilder<Parent>>(this, button);
   }
}