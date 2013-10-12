package net.sf.javagimmicks.swing;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A {@link ComboBoxEditor} implemenation that enables a given {@link JComboBox}
 * with auto-complete features.
 */
public class AutoComboBoxEditor extends BasicComboBoxEditor
{
   /**
    * Creates and registers an {@link AutoComboBoxEditor} on a given
    * {@link JComboBox} with the given settings.
    * 
    * @param comboBox
    *           the {@link JComboBox} where to register a new
    *           {@link AutoComboBoxEditor}
    * @param markOnFocus
    *           if the text content of the {@link JComboBox} should be marked
    *           upon focus gain (see
    *           {@link AutoComboBoxEditor#setMarkOnFocus(boolean)})
    * @param caseSensitive
    *           if suggestions should be done case-sensitive or not (see
    *           {@link AutoComboBoxEditor#setCaseSensitive(boolean)})
    * @param strict
    *           if only suggestions are accepted by the {@link JComboBox} (see
    *           {@link AutoComboBoxEditor#setStrict(boolean)})
    */
   public static void install(final JComboBox comboBox, final boolean markOnFocus, final boolean caseSensitive,
         final boolean strict)
   {
      final AutoComboBoxEditor editor = new AutoComboBoxEditor(comboBox);
      editor.setCaseSensitive(caseSensitive);
      editor.setStrict(strict);
      editor.setMarkOnFocus(markOnFocus);

      comboBox.setEditor(editor);
      comboBox.setEditable(true);
   }

   /**
    * Creates and registers an {@link AutoComboBoxEditor} on a given
    * {@link JComboBox} with the given settings in strict mode (see
    * {@link AutoComboBoxEditor#setStrict(boolean)}).
    * 
    * @param comboBox
    *           the {@link JComboBox} where to register a new
    *           {@link AutoComboBoxEditor}
    * @param markOnFocus
    *           if the text content of the {@link JComboBox} should be marked
    *           upon focus gain (see
    *           {@link AutoComboBoxEditor#setMarkOnFocus(boolean)})
    * @param caseSensitive
    *           if suggestions should be done case-sensitive or not (see
    *           {@link AutoComboBoxEditor#setCaseSensitive(boolean)})
    */
   public static void install(final JComboBox comboBox, final boolean markOnFocus, final boolean caseSensitive)
   {
      install(comboBox, markOnFocus, caseSensitive, true);
   }

   /**
    * Creates and registers an {@link AutoComboBoxEditor} on a given
    * {@link JComboBox} with the given settings in non-case-sensitive mode (see
    * {@link AutoComboBoxEditor#setCaseSensitive(boolean)}) and strict mode (see
    * {@link AutoComboBoxEditor#setStrict(boolean)}).
    * 
    * @param comboBox
    *           the {@link JComboBox} where to register a new
    *           {@link AutoComboBoxEditor}
    * @param markOnFocus
    *           if the text content of the {@link JComboBox} should be marked
    *           upon focus gain (see
    *           {@link AutoComboBoxEditor#setMarkOnFocus(boolean)})
    */
   public static void install(final JComboBox comboBox, final boolean markOnFocus)
   {
      install(comboBox, markOnFocus, false);
   }

   /**
    * Creates and registers an {@link AutoComboBoxEditor} on a given
    * {@link JComboBox} without mark-on-focus (see
    * {@link AutoComboBoxEditor#setMarkOnFocus(boolean)}, in non-case-sensitive
    * mode (see {@link AutoComboBoxEditor#setCaseSensitive(boolean)}) and strict
    * mode (see {@link AutoComboBoxEditor#setStrict(boolean)}).
    */
   public static void install(final JComboBox comboBox)
   {
      install(comboBox, false);
   }

   private final JComboBox _comboBox;

   private boolean _caseSensitive = false;
   private boolean _strict = true;
   private MarkOnFocusListener _markOnFocusListener;

   /**
    * Creates a new instance for the given {@link JComboBox}
    * 
    * @param comboBox
    *           the {@link JComboBox} for which auto-complete should be enabled
    */
   public AutoComboBoxEditor(final JComboBox comboBox)
   {
      _comboBox = comboBox;
      editor = new AutoTextField();
   }

   /**
    * Returns if suggestions are done case-sensitive or not.
    * 
    * @return if suggestions are done case-sensitive or not
    */
   public boolean isCaseSensitive()
   {
      return _caseSensitive;
   }

   /**
    * Returns if only values from the model of the {@link JComboBox} can be
    * entered.
    * 
    * @return if only values from the model of the {@link JComboBox} can be
    *         entered
    */
   public boolean isStrict()
   {
      return _strict;
   }

   /**
    * Returns if the text content of the {@link JComboBox} is marked on focus
    * gain.
    * 
    * @return if the text content of the {@link JComboBox} is marked on focus
    *         gain
    */
   public boolean isMarkOnFocus()
   {
      return _markOnFocusListener != null;
   }

   /**
    * Set if suggestions are done case-sensitive or not.
    * 
    * @param caseSensitive
    *           if suggestions are done case-sensitive or not
    */
   public void setCaseSensitive(final boolean caseSensitive)
   {
      _caseSensitive = caseSensitive;
   }

   /**
    * Sets if only values from the model of the {@link JComboBox} can be
    * entered.
    * 
    * @param strict
    *           if only values from the model of the {@link JComboBox} can be
    *           entered
    */
   public void setStrict(final boolean strict)
   {
      _strict = strict;
   }

   /**
    * Sets if the text content of the {@link JComboBox} is marked on focus gain.
    * 
    * @param markOnFocus
    *           if the text content of the {@link JComboBox} is marked on focus
    *           gain
    */
   public void setMarkOnFocus(final boolean markOnFocus)
   {
      if (markOnFocus == isMarkOnFocus())
      {
         return;
      }

      if (markOnFocus)
      {
         _markOnFocusListener = new MarkOnFocusListener(editor);
         editor.addFocusListener(_markOnFocusListener);
      }
      else
      {
         editor.removeFocusListener(_markOnFocusListener);
         _markOnFocusListener = null;
      }
   }

   private ComboBoxModel getModel()
   {
      return _comboBox.getModel();
   }

   private class AutoTextField extends JTextField
   {
      private static final long serialVersionUID = -2404637927639461570L;

      public AutoTextField()
      {
         setDocument(new AutoDocument());
         setBorder(null);

         if (_strict && getModel().getSize() > 0)
         {
            setText(getModel().getElementAt(0).toString());
         }
      }

      @Override
      public void replaceSelection(final String sText)
      {
         final AutoDocument oAutoDocument = (AutoDocument) getDocument();
         if (oAutoDocument != null)
         {
            try
            {
               final int iStart = Math.min(getCaret().getDot(), getCaret().getMark());
               final int iEnd = Math.max(getCaret().getDot(), getCaret().getMark());
               oAutoDocument.replace(iStart, iEnd - iStart, sText, null);
            }
            catch (final Exception exception)
            {
            }
         }
      }

      private String getMatch(final String sText)
      {
         for (int i = 0; i < getModel().getSize(); i++)
         {
            final String sListEntry = getModel().getElementAt(i).toString();
            if (sListEntry != null)
            {
               if (!_caseSensitive &&
                     sListEntry.toLowerCase().startsWith(sText.toLowerCase()))
               {
                  return sListEntry;
               }

               if (_caseSensitive && sListEntry.startsWith(sText))
               {
                  return sListEntry;
               }
            }
         }

         return null;
      }

      private class AutoDocument extends PlainDocument
      {
         private static final long serialVersionUID = -7796598731233922182L;

         @Override
         public void replace(final int iOffset, final int iLength, final String sText,
               final AttributeSet oAttributeSet) throws BadLocationException
         {
            super.remove(iOffset, iLength);
            insertString(iOffset, sText, oAttributeSet);
         }

         @Override
         public void insertString(final int iOffset, final String sText,
               final AttributeSet oAttributeSet) throws BadLocationException
         {
            if (sText == null || "".equals(sText))
               return;

            final String sStart = getText(0, iOffset);
            String sMatch = getMatch(sStart + sText);

            int iLength = (iOffset + sText.length()) - 1;
            if (_strict && sMatch == null)
            {
               sMatch = getMatch(sStart);
               iLength--;
            }
            else if (!_strict && sMatch == null)
            {
               super.insertString(iOffset, sText, oAttributeSet);
               return;
            }

            if (sMatch != null)
            {
               getModel().setSelectedItem(sMatch);
            }

            super.remove(0, getLength());
            super.insertString(0, sMatch, oAttributeSet);

            setSelectionStart(iLength + 1);
            setSelectionEnd(getLength());
         }

         @Override
         public void remove(final int iOffset, final int iLength)
               throws BadLocationException
         {
            int iStart = getSelectionStart();
            if (iStart > 0)
            {
               iStart--;
            }

            final String sMatch = getMatch(getText(0, iStart));
            if (!_strict && sMatch == null)
            {
               super.remove(iOffset, iLength);
            }
            else
            {
               super.remove(0, getLength());
               super.insertString(0, sMatch, null);
            }

            if (sMatch != null)
            {
               getModel().setSelectedItem(sMatch);
            }

            try
            {
               setSelectionStart(iStart);
               setSelectionEnd(getLength());
            }
            catch (final Exception exception)
            {
            }
         }
      }
   }
}
