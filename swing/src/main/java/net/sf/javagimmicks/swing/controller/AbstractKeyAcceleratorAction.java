package net.sf.javagimmicks.swing.controller;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * An extension of {@link AbstractAction} that allows to specify a
 * {@link KeyStroke} on which it will be invoked.
 */
public abstract class AbstractKeyAcceleratorAction extends AbstractAction
{
   private static final long serialVersionUID = -6955793613094829797L;

   /**
    * Creates a new instance for the given accelerator {@link KeyStroke}.
    * 
    * @param acceleratorKey
    *           the {@link KeyStroke} on which this action will be invoked.
    */
   public AbstractKeyAcceleratorAction(final KeyStroke acceleratorKey)
   {
      super();
      setAcceleratorKey(acceleratorKey);
   }

   /**
    * Creates a new instance for the given accelerator {@link KeyStroke}, name
    * and {@link Icon}.
    * 
    * @param name
    *           the name of this action
    * @param icon
    *           the {@link Icon} of this action
    * @param acceleratorKey
    *           the {@link KeyStroke} on which this action will be invoked.
    * @see AbstractAction#AbstractAction(String, Icon)
    */
   public AbstractKeyAcceleratorAction(final String name, final Icon icon, final KeyStroke acceleratorKey)
   {
      super(name, icon);
      setAcceleratorKey(acceleratorKey);
   }

   /**
    * Creates a new instance for the given accelerator {@link KeyStroke} and
    * name.
    * 
    * @param name
    *           the name of this action
    * @param acceleratorKey
    *           the {@link KeyStroke} on which this action will be invoked.
    * @see AbstractAction#AbstractAction(String)
    */
   public AbstractKeyAcceleratorAction(final String name, final KeyStroke acceleratorKey)
   {
      super(name);
      setAcceleratorKey(acceleratorKey);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code and modifier code (see
    * {@link KeyStroke#getKeyStroke(int, int)}).
    * 
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    */
   public AbstractKeyAcceleratorAction(final int keyCode, final int modifiers)
   {
      super();
      setAcceleratorKey(keyCode, modifiers);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code and modifier code (see
    * {@link KeyStroke#getKeyStroke(int, int)}), name and {@link Icon}.
    * 
    * @param name
    *           the name of this action
    * @param icon
    *           the {@link Icon} of this action
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    * @see AbstractAction#AbstractAction(String, Icon)
    */
   public AbstractKeyAcceleratorAction(final String name, final Icon icon, final int keyCode, final int modifiers)
   {
      super(name, icon);
      setAcceleratorKey(keyCode, modifiers);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code and modifier code (see
    * {@link KeyStroke#getKeyStroke(int, int)}) and name.
    * 
    * @param name
    *           the name of this action
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    * @see AbstractAction#AbstractAction(String)
    */
   public AbstractKeyAcceleratorAction(final String name, final int keyCode, final int modifiers)
   {
      super(name);
      setAcceleratorKey(keyCode, modifiers);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code without modifiers (see
    * {@link KeyStroke#getKeyStroke(int, int)}).
    * 
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    */
   public AbstractKeyAcceleratorAction(final int keyCode)
   {
      super();
      setAcceleratorKey(keyCode);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code without modifiers (see
    * {@link KeyStroke#getKeyStroke(int, int)}), name and {@link Icon}.
    * 
    * @param name
    *           the name of this action
    * @param icon
    *           the {@link Icon} of this action
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    * @see AbstractAction#AbstractAction(String, Icon)
    */
   public AbstractKeyAcceleratorAction(final String name, final Icon icon, final int keyCode)
   {
      super(name, icon);
      setAcceleratorKey(keyCode);
   }

   /**
    * Creates a new instance for an accelerator {@link KeyStroke} which is
    * specified by a given key code without modifiers (see
    * {@link KeyStroke#getKeyStroke(int, int)}) and name.
    * 
    * @param name
    *           the name of this action
    * @param keyCode
    *           the key code for the {@link KeyStroke} to use as accelerator
    * @param modifiers
    *           the modifiers for the {@link KeyStroke} to use as accelerator
    * @see KeyStroke#getKeyStroke(int, int)
    * @see AbstractAction#AbstractAction(String)
    */
   public AbstractKeyAcceleratorAction(final String name, final int keyCode)
   {
      super(name);
      setAcceleratorKey(keyCode);
   }

   private void setAcceleratorKey(final int keyCode, final int modifiers)
   {
      setAcceleratorKey(KeyStroke.getKeyStroke(keyCode, modifiers));
   }

   private void setAcceleratorKey(final int keyCode)
   {
      setAcceleratorKey(KeyStroke.getKeyStroke(keyCode, 0));
   }

   private void setAcceleratorKey(final KeyStroke acceleratorKey)
   {
      putValue(ACCELERATOR_KEY, acceleratorKey);
   }
}