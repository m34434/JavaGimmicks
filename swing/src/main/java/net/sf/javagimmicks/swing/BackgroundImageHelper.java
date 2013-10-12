package net.sf.javagimmicks.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

/**
 * A helper class that can be used by an other {@link Component} that helps to
 * render a background {@link Image} using a configurable resize/scaling
 * behavior.
 */
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

   /**
    * Creates a new instance for the given {@link Image}
    * 
    * @param image
    *           the {@link Image} that should be rendered
    */
   public BackgroundImageHelper(final Image image)
   {
      _image = image;
      _imageWidth = image.getWidth(null);
      _imageHeight = image.getHeight(null);
      _imageRatio = ((double) _imageWidth) / _imageHeight;
   }

   /**
    * Returns if the background {@link Image} should be enlarged if the client
    * {@link Component}'s size is bigger.
    * 
    * @return if the background {@link Image} should be enlarged if the client
    *         {@link Component}'s size is bigger
    */
   public boolean isEnlarge()
   {
      return _enlarge;
   }

   /**
    * Sets if the background {@link Image} should be enlarged if the client
    * {@link Component}'s size is bigger.
    * 
    * @param enlarge
    *           if the background {@link Image} should be enlarged if the client
    *           {@link Component}'s size is bigger
    */
   public void setEnlarge(final boolean enlarge)
   {
      _enlarge = enlarge;
   }

   /**
    * Returns the scale hint that is internally used for resizing/scaling (see
    * {@link Image#getScaledInstance(int, int, int)}.
    * 
    * @return the scale hint that is internally used for resizing/scaling
    */
   public int getScaleHint()
   {
      return _scaleHint;
   }

   /**
    * Sets the scale hint that is internally used for resizing/scaling (see
    * {@link Image#getScaledInstance(int, int, int)}.
    * 
    * @param scaleHint
    *           the scale hint that is internally used for resizing/scaling
    */
   public void setScaleHint(final int scaleHint)
   {
      _scaleHint = scaleHint;
   }

   /**
    * Returns if the width/height ratio should be kept upon resizing/scaling.
    * 
    * @return if the width/height ratio should be kept upon resizing/scaling
    */
   public boolean isKeepRatio()
   {
      return _keepRatio;
   }

   /**
    * Sets if the width/height ratio should be kept upon resizing/scaling.
    * 
    * @param keepRatio
    *           if the width/height ratio should be kept upon resizing/scaling
    */
   public void setKeepRatio(final boolean keepRatio)
   {
      _keepRatio = keepRatio;
   }

   /**
    * Should be called by the client {@link Component} to render the background
    * {@link Image} with the internal settings on the given {@link Graphics}.
    * 
    * @param component
    *           the {@link Component} which want's to render the background
    *           {@link Image}
    * @param g
    *           the {@link Graphics} where to paint the {@link Component}
    * @see Component#paint(Graphics)
    */
   public void paintComponent(final Component component, final Graphics g)
   {
      final int width = component.getWidth();
      final int height = component.getHeight();

      // Skip, if nothing is to draw
      if (width == 0 || height == 0)
      {
         return;
      }

      recalc(width, height);

      if (_drawScaled)
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

   private void recalc(final int newWidth, final int newHeight)
   {
      if (newWidth == _lastWidth && newHeight == _lastHeight)
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

      if (_keepRatio)
      {
         if (currentRatio >= _imageRatio)
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

      if (newScaledHeight == _imageHeight && newScaledWith == _imageWidth)
      {
         _drawScaled = false;
      }
      else
      {
         if (newScaledHeight != _scaledHeight || newScaledWith != _scaledWith)
         {
            _scaledHeight = newScaledHeight;
            _scaledWith = newScaledWith;

            _scaledImage = _image.getScaledInstance(_scaledWith, _scaledHeight, _scaleHint);
         }

         _drawScaled = true;
      }
   }
}
