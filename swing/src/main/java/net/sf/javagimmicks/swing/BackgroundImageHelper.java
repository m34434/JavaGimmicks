package net.sf.javagimmicks.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

public class BackgroundImageHelper
{
    private boolean _enlarge = false;
    private int _scaleHint = Image.SCALE_DEFAULT;
    private boolean _keepRatio = true;

    private final Image _image;
    private final int _imageWidth;
    private final int _imageHeight;
    private final double _imageRatio;

    private Image _scaledImage;
    private int _scaledWith;
    private int _scaledHeight;
    private boolean _drawScaled = false;

    private int _lastWidth;
    private int _lastHeight;

    public BackgroundImageHelper(Image image)
    {
        _image = image;
        _imageWidth = image.getWidth(null);
        _imageHeight = image.getHeight(null);
        _imageRatio = ((double) _imageWidth) / _imageHeight;
    }

    public boolean isEnlarge()
    {
        return _enlarge;
    }

    public void setEnlarge(boolean enlarge)
    {
        _enlarge = enlarge;
    }

    public int getScaleHint()
    {
        return _scaleHint;
    }

    public void setScaleHint(int scaleHint)
    {
        _scaleHint = scaleHint;
    }

    public boolean isKeepRatio()
    {
        return _keepRatio;
    }

    public void setKeepRatio(boolean keepRatio)
    {
        _keepRatio = keepRatio;
    }

    public void paintComponent(Component component, Graphics g)
    {
        final int width = component.getWidth();
        final int height = component.getHeight();

        // Skip, if nothing is to draw
        if(width == 0 || height == 0)
        {
            return;
        }

        recalc(width, height);

        if(_drawScaled)
        {
            final int x = (_lastWidth - _scaledWith) / 2;
            final int y = (_lastHeight - _scaledHeight) / 2;

            g.drawImage(_scaledImage, x, y, _scaledWith, _scaledHeight, component);
        }
        else
        {
            final int x = (_lastWidth - _imageWidth) / 2;
            final int y = (_lastHeight - _imageHeight) / 2;

            g.drawImage(_image, x, y, _imageWidth, _imageHeight, component);
        }
    }

    private void recalc(int newWidth, int newHeight)
    {
        if(newWidth == _lastWidth && newHeight == _lastHeight)
        {
            return;
        }
        
        _lastWidth = newWidth;
        _lastHeight = newHeight;
        
        // Calculate the ratio of the desktop size
        final double currentRatio = ((double) _lastWidth) / _lastHeight;

        // Compare the ratios
        int newScaledWith;
        int newScaledHeight;

        if(_keepRatio)
        {
            if(currentRatio >= _imageRatio)
            {
                newScaledWith = (int) (_lastHeight * _imageRatio);
                newScaledHeight = _lastHeight;
            }
            else
            {
                newScaledHeight = (int) (_lastWidth / _imageRatio);
                newScaledWith = _lastWidth;
            }
        }
        else
        {
            newScaledHeight = _lastHeight;
            newScaledWith = _lastWidth;
        }

        newScaledHeight = _enlarge ? newScaledHeight : Math.min(newScaledHeight, _imageHeight);
        newScaledWith = _enlarge ? newScaledWith : Math.min(newScaledWith, _imageWidth);

        if(newScaledHeight == _imageHeight && newScaledWith == _imageWidth)
        {
            _drawScaled = false;
        }
        else
        {
            if(newScaledHeight != _scaledHeight || newScaledWith != _scaledWith)
            {
                _scaledHeight = newScaledHeight;
                _scaledWith = newScaledWith;

                _scaledImage = _image.getScaledInstance(_scaledWith, _scaledHeight, _scaleHint);
            }
            
            _drawScaled = true;
        }
    }
}
