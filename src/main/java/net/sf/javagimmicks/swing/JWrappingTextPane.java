package net.sf.javagimmicks.swing;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class JWrappingTextPane extends JTextPane
{
    private static final long serialVersionUID = -5932297203397634342L;

    private boolean _wrappingEnabled = false;
    
    public JWrappingTextPane()
    {
        super();
    }

    public JWrappingTextPane(StyledDocument doc)
    {
        super(doc);
    }
    
    public boolean isWrappingEnabled()
    {
        return _wrappingEnabled;
    }

    public void setWrappingEnabled(boolean wrappingEnabled)
    {
        _wrappingEnabled = wrappingEnabled;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return _wrappingEnabled ? (super.getScrollableTracksViewportWidth()) : false;
    }
    
    public JScrollPane buildScrollPane()
    {
       JScrollPane result = new JScrollPane(this);
       result.getViewport().setBackground(this.getBackground());
       
       return result;
    }
}
