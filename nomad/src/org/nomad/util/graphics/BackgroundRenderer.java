package org.nomad.util.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;

/**
 * A class used to draw the background of a component.
 * This class can be used to implement calculation
 * expensive paint operations that need to be available
 * for different components. (But use only one BackgroundRenderer
 * per component)
 * 
 * @author Christian Schneider
 */
public abstract class BackgroundRenderer {

	// the background
	private Image bimage = null;
	
	// the current background size
	private Dimension dim = new Dimension(0,0);
	
	// true:the background must be updated
	private boolean forceRender = true;

	/**
	 * Creates a class for rendering a background image.
	 */
	public BackgroundRenderer() {
		super();
	}
	
	/**
	 * Call this if the background image must be updated.
	 */
	public void update() {
		forceRender = true;
	}
	
	/**
	 * Resets the update flag. Use this if the background image is
	 * up to date.
	 */
	protected void clearUpdateFlag() {
		forceRender = false;
	}
	
	/**
	 * Returns true if the background must be updated.
	 * @return true if the background must be updated.
	 */
	protected boolean isUpdateRequired() {
		return forceRender;
	}

	/**
	 * Renders the image for the given component and given size
	 * @param comp
	 */
	protected void render(Component comp) {
		Dimension dim = comp.getSize();
		bimage = renderImage(comp); // setup
		this.dim = dim;
		clearUpdateFlag();
	}

	/**
	 * This operation must check if the background must be updated
	 * and call <code>render(component)</code> if this case is true.
	 * 
	 * The <code>isUpdateRequired()</code> must be checked.
	 *  
	 * @param comp The component.
	 */
	protected void checkRender(Component comp) {
		if ((isUpdateRequired()||!this.dim.equals(comp.getSize()))&&comp.getWidth()>0&&comp.getHeight()>0)
			render(comp);
	}
	
	/**
	 * Returns true if the background image has transparency.
	 * @return true if the background image has transparency.
	 */
	public boolean hasAlpha() {
		return false;
	}

	/**
	 * Renders the image using createImage.
	 * @param comp the component
	 * @return the image
	 */
	protected Image renderImage(Component comp) {
		// create image
		return createImage(comp, comp.getSize(), hasAlpha());
	}

	/**
	 * Creates a BufferedImage with given size and alpha support if hasAlpha is true.
	 * If possible the image will be compatible to the graphics context of the given component.
	 * @param comp the component 
	 * @param dim image size
	 * @param hasAlpha true if the image should support alpha values
	 * @return the image
	 */
	protected BufferedImage createImage(Component comp, Dimension dim, boolean hasAlpha) {
		Graphics g = comp.getGraphics();
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			BufferedImage img = g2.getDeviceConfiguration().createCompatibleImage(dim.width, dim.height,
				hasAlpha?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB
			);

			g.dispose();
			
			return img;
		} else {
			return new BufferedImage(dim.width, dim.height,
				hasAlpha?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);
		}
	}

	/**
	 * Creates a image with given pixels.
	 * If possible the image will be compatible to the graphics context of the given component.
	 * @param comp the component
	 * @param dim the image size
	 * @param pixels the image pixels
	 * @return the image
	 */
	protected Image createImage(Component comp, Dimension dim, int [] pixels) {
	    return comp.createImage(
	      new MemoryImageSource(
	        dim.width, dim.height,
	        ColorModel.getRGBdefault(),
	        pixels, 0, dim.width
	      )
	    );
	}

	/**
	 * Returns the current size of the image.
	 * 
	 * @return the current size of the image.
	 */
	public Dimension getSize() {
		return dim;
	}
	
	/**
	 * Returns the current background image
	 * @return the current background image
	 */
	protected Image getBuffer() {
		return bimage;
	}

	/**
	 * Draws the background image to the given graphics context.
	 * comp is the component that must be repainted.
	 * 
	 * If the components size has changed or the update flag is set 
	 * the background image will be rendered before.
	 * 
	 * @param comp the component that must be repainted
	 * @param g the graphics context
	 */
	public void drawTo(Component comp, Graphics g) {
		checkRender(comp);

		if (bimage!=null)
			g.drawImage(bimage, 0, 0, comp);
	}

}
