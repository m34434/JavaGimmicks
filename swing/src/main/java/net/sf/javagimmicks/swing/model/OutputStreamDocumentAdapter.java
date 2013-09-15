package net.sf.javagimmicks.swing.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.text.Document;

public class OutputStreamDocumentAdapter extends OutputStream
{
    protected final Document _document;
    private final ByteArrayOutputStream _buffer = new ByteArrayOutputStream();

    public OutputStreamDocumentAdapter(Document document)
    {
        _document = document;
    }

    @Override
    public void flush() throws IOException
    {
        try
        {
            applyText(_document, harvestBuffer());
        }
        catch(Exception e)
        {
            throw new IOException("Unable to write into internal Document: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException
    {
        flush();
    }

    @Override
    public void write(int b) throws IOException
    {
        _buffer.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        _buffer.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        _buffer.write(b);
    }
    
    protected void applyText(Document document, String text) throws Exception
    {
        _document.insertString(_document.getLength(), text, null);
    }
    
    private String harvestBuffer()
    {
        byte[] arrBytes = _buffer.toByteArray();
        _buffer.reset();
        
        return new String(arrBytes);
    }
}