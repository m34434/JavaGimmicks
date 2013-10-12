package net.sf.javagimmicks.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.sf.javagimmicks.swing.controller.AbstractKeyAcceleratorAction;

/**
 * A builder providing a fluent API for creating {@link JMenu}s,
 * {@link JMenuBar}s or {@link JPopupMenu}s.
 */
public class JMenuBuilder
{
   private final Stack<JMenu> _subMenuStack = new Stack<JMenu>();
   private final List<JMenuItem> _topLevelItems = new ArrayList<JMenuItem>();

   /**
    * Creates a new instance.
    */
   public JMenuBuilder()
   {}

   /**
    * Creates a new sub-menu and switches this instance to build the sub-menu
    * until {@link #parent()} is called.
    * 
    * @param menuName
    *           the name of the new sub-menu
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder menu(final String menuName)
   {
      final JMenu menu = new JMenu(menuName);

      add(menu);

      _subMenuStack.push(menu);

      return this;
   }

   /**
    * Adds a new {@link Action} to the currently built menu.
    * 
    * @param action
    *           the {@link Action} to add in the currently built menu
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder item(final Action action)
   {
      final JMenuItem item = new JMenuItem(action);

      add(item);

      return this;
   }

   /**
    * Adds a new {@link Action} specified by the given name and
    * {@link ActionListener} to the currently built menu.
    * 
    * @param name
    *           the name of {@link Action} to add in the currently built menu
    * @param listener
    *           the {@link ActionListener} of the {@link Action} to add in the
    *           currently build menu
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder item(final String name, final ActionListener listener)
   {
      return item(new AbstractAction(name)
      {
         private static final long serialVersionUID = -1845717164976373485L;

         @Override
         public void actionPerformed(final ActionEvent e)
         {
            listener.actionPerformed(e);
         }
      });
   }

   /**
    * Adds a new {@link Action} specified by the given name,
    * {@link ActionListener} and accelerator key code to the currently built
    * menu.
    * 
    * @param name
    *           the name of {@link Action} to add in the currently built menu
    * @param listener
    *           the {@link ActionListener} of the {@link Action} to add in the
    *           currently build menu
    * @param keyCode
    *           the key code which will be registered as accelerator key on the
    *           created {@link Action}
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder item(final String name, final ActionListener listener, final int keyCode)
   {
      return item(new AbstractKeyAcceleratorAction(name, keyCode)
      {
         private static final long serialVersionUID = -1845717164976373484L;

         @Override
         public void actionPerformed(final ActionEvent e)
         {
            listener.actionPerformed(e);
         }
      });
   }

   /**
    * Adds a new {@link Action} specified by the given name,
    * {@link ActionListener} and accelerator key code (and modifiers) to the
    * currently built menu.
    * 
    * @param name
    *           the name of {@link Action} to add in the currently built menu
    * @param listener
    *           the {@link ActionListener} of the {@link Action} to add in the
    *           currently build menu
    * @param keyCode
    *           the key code which will be registered as accelerator key on the
    *           created {@link Action}
    * @param modifiers
    *           the modifiers for the registered accelerator key (see
    *           {@link AbstractKeyAcceleratorAction#AbstractKeyAcceleratorAction(String, int, int)}
    *           )
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder item(final String same, final ActionListener listener, final int keyCode, final int modifiers)
   {
      return item(new AbstractKeyAcceleratorAction(same, keyCode, modifiers)
      {
         private static final long serialVersionUID = -1845717164976373483L;

         @Override
         public void actionPerformed(final ActionEvent e)
         {
            listener.actionPerformed(e);
         }
      });
   }

   /**
    * Adds a separator to the currently built menu.
    * 
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder separator()
   {
      if (_subMenuStack.isEmpty())
      {
         throw new IllegalStateException("Cannot add separator on the top level!");
      }

      _subMenuStack.peek().addSeparator();

      return this;
   }

   /**
    * Switches back to the parent menu if currently a sub-menu is built (see
    * {@link #menu(String)}).
    * 
    * @return the current {@link JMenuBuilder}
    */
   public JMenuBuilder parent()
   {
      if (_subMenuStack.isEmpty())
      {
         throw new IllegalStateException("Already on the top level!");
      }

      _subMenuStack.pop();

      return this;
   }

   /**
    * Builds a {@link JMenu} with the given name for the menu structure
    * configured so far.
    * 
    * @param name
    *           the name of the new {@link JMenu}
    * @return the new {@link JMenu}
    */
   public JMenu bulidMenu(final String name)
   {
      final JMenu result = new JMenu(name);

      for (final JMenuItem item : _topLevelItems)
      {
         result.add(item);
      }

      return result;
   }

   /**
    * Builds a {@link JMenuBar} for the menu structure configured so far.
    * 
    * @return the new {@link JMenu}
    */
   public JMenuBar buildMenuBar()
   {
      final JMenuBar result = new JMenuBar();

      for (final JMenuItem item : _topLevelItems)
      {
         if (!(item instanceof JMenu))
         {
            throw new IllegalStateException("Top level contains items!");
         }

         result.add((JMenu) item);
      }

      return result;
   }

   /**
    * Builds a {@link JPopupMenu} with the given name for the menu structure
    * configured so far.
    * 
    * @param name
    *           the name of the new {@link JPopupMenu}
    * @return the new {@link JPopupMenu}
    */
   public JPopupMenu buildPopupMenu(final String name)
   {
      final JPopupMenu result = new JPopupMenu(name);

      for (final JMenuItem item : _topLevelItems)
      {
         result.add(item);
      }

      return result;
   }

   private void add(final JMenuItem item)
   {
      if (_subMenuStack.isEmpty())
      {
         _topLevelItems.add(item);
      }
      else
      {
         final JMenu parentMenu = _subMenuStack.peek();
         parentMenu.add(item);
      }
   }
}
