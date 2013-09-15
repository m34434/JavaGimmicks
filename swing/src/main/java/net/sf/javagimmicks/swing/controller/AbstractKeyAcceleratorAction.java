package net.sf.javagimmicks.swing.controller;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public abstract class AbstractKeyAcceleratorAction extends AbstractAction
{
   private static final long serialVersionUID = -6955793613094829797L;

   public AbstractKeyAcceleratorAction(KeyStroke acceleratorKey)
    {
        super();
        setAcceleratorKey(acceleratorKey);
    }

    public AbstractKeyAcceleratorAction(String name, Icon icon, KeyStroke acceleratorKey)
    {
        super(name, icon);
        setAcceleratorKey(acceleratorKey);
    }

    public AbstractKeyAcceleratorAction(String name, KeyStroke acceleratorKey)
    {
        super(name);
        setAcceleratorKey(acceleratorKey);
    }

    public AbstractKeyAcceleratorAction(int keyCode, int modifiers)
    {
        super();
        setAcceleratorKey(keyCode, modifiers);
    }

    public AbstractKeyAcceleratorAction(String name, Icon icon, int keyCode, int modifiers)
    {
        super(name, icon);
        setAcceleratorKey(keyCode, modifiers);
    }

    public AbstractKeyAcceleratorAction(String name, int keyCode, int modifiers)
    {
        super(name);
        setAcceleratorKey(keyCode, modifiers);
    }
    
    public AbstractKeyAcceleratorAction(int keyCode)
    {
        super();
        setAcceleratorKey(keyCode);
    }

    public AbstractKeyAcceleratorAction(String name, Icon icon, int keyCode)
    {
        super(name, icon);
        setAcceleratorKey(keyCode);
    }

    public AbstractKeyAcceleratorAction(String name, int keyCode)
    {
        super(name);
        setAcceleratorKey(keyCode);
    }
    
    private void setAcceleratorKey(int keyCode, int modifiers)
    {
        setAcceleratorKey(KeyStroke.getKeyStroke(keyCode, modifiers));
    }
    
    private void setAcceleratorKey(int keyCode)
    {
        setAcceleratorKey(KeyStroke.getKeyStroke(keyCode, 0));
    }
    
    private void setAcceleratorKey(KeyStroke acceleratorKey)
    {
        putValue(ACCELERATOR_KEY, acceleratorKey);
    }
}