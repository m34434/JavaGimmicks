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

public class JMenuBuilder
{
    private final Stack<JMenu> _subMenuStack = new Stack<JMenu>();
    private final List<JMenuItem> _topLevelItems = new ArrayList<JMenuItem>();
    
    public JMenuBuilder menu(String menuName)
    {
        JMenu menu = new JMenu(menuName);
        
        add(menu);
        
        _subMenuStack.push(menu);
        
        return this;
    }
    
    public JMenuBuilder item(Action action)
    {
        JMenuItem item = new JMenuItem(action);
        
        add(item);
        
        return this;
    }
    
    public JMenuBuilder item(String name, final ActionListener listener)
    {
        return item(new AbstractAction(name)
        {
            private static final long serialVersionUID = -1845717164976373485L;

            public void actionPerformed(ActionEvent e)
            {
                listener.actionPerformed(e);
            }
        });
    }
    
    public JMenuBuilder item(String name, final ActionListener listener, int keyCode)
    {
        return item(new AbstractKeyAcceleratorAction(name, keyCode)
        {
            private static final long serialVersionUID = -1845717164976373484L;

            public void actionPerformed(ActionEvent e)
            {
                listener.actionPerformed(e);
            }
        });
    }
    
    public JMenuBuilder item(String same, final ActionListener listener, int keyCode, int modifiers)
    {
        return item(new AbstractKeyAcceleratorAction(same, keyCode, modifiers)
        {
            private static final long serialVersionUID = -1845717164976373483L;

            public void actionPerformed(ActionEvent e)
            {
                listener.actionPerformed(e);
            }
        });
    }
    
    public JMenuBuilder separator()
    {
        if(_subMenuStack.isEmpty())
        {
            throw new IllegalStateException("Cannot add separator on the top level!");
        }
        
        _subMenuStack.peek().addSeparator();
        
        return this;
    }
    
    public JMenuBuilder parent()
    {
        if(_subMenuStack.isEmpty())
        {
            throw new IllegalStateException("Already on the top level!");
        }
        
        _subMenuStack.pop();
        
        return this;
    }
    
    public JMenu bulidMenu(String name)
    {
        JMenu result = new JMenu(name);
        
        for(JMenuItem item : _topLevelItems)
        {
            result.add(item);
        }
        
        return result;
    }
    
    public JMenuBar buildMenuBar()
    {
        JMenuBar result = new JMenuBar();
        
        for(JMenuItem item : _topLevelItems)
        {
            if(!(item instanceof JMenu))
            {
                throw new IllegalStateException("Top level contains items!");
            }
            
            result.add((JMenu)item);
        }
        
        return result;
    }
    
    public JPopupMenu buildPopupMenu(String sLabel)
    {
       JPopupMenu result = new JPopupMenu(sLabel);
       
       for(JMenuItem item : _topLevelItems)
       {
          result.add(item);
       }
       
       return result;
    }
    
    private void add(JMenuItem item)
    {
        if(_subMenuStack.isEmpty())
        {
            _topLevelItems.add(item);
        }
        else
        {
            JMenu parentMenu = _subMenuStack.peek();
            parentMenu.add(item);
        }
    }
}
