package nomad.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * A horizontal gradient background renderer.
 * 
 * @author Christian Schneider
 */
public class HGradientBackgroundRenderer extends BackgroundRenderer {

	// light position, calculated by width*light
	private double light = 1.0/4.0;
	
	// light radius, calculated by width*radius
	private double radius = 3.0/4.0;
	
	// light color
	private Color lightColor = Color.WHITE;
	
	// background color
	private Color backgroundColor = Color.GRAY;
	
	/**
	 * A horizontal gradient background renderer.
	 */	
	public HGradientBackgroundRenderer() {
		super();
	}

	/**
	 * Sets the light position. The value must be in the interval [0,1].
	 * The pixel position is calculated by width*light.
	 * @param light the new value
	 */
	public void setLight(double light) {
		this.light = Math.max(Math.min(light, 1.0), 0);
		update();
	}

	/**
	 * Sets the radius. The value must be in the interval [0,1].
	 * The pixel size is calculated by width*radius.
	 * @param radius the new value
	 */
	public void setRadius(double radius) {
		this.radius = Math.max(Math.min(radius, 1.0), 0);
		update();
	}
	
	/**
	 * Sets the light color
	 * @param c the new color
	 */
	public void setLightColor(Color c) {
		this.lightColor = c;
		update();
	}
	
	/**
	 * Sets the background color
	 * @param c the new color
	 */
	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
		update();
	}
	
	protected void checkRender(Component comp) {
		// overwrite because we only want to check width, not height
		if ((isUpdateRequired() || (this.getSize().width!=comp.getWidth()))&&comp.getWidth()>0&&comp.getHeight()>0) {
			render(comp);
			clearUpdateFlag();
		}
	}

	protected Image renderImage(Component comp) {
		int[] pixels = createGradientTable(comp.getWidth(), (int)(comp.getWidth()*light), (int)(comp.getWidth()*radius));

		// create image
		return createImage(comp, new Dimension(comp.getWidth(), 1), pixels);
	}

	/**
	 * Calculates the pixels. Note that size is the width. Since we create a image with one pixel height.
	 * 
	 * @param size width of the image
	 * @param pxlight light position in pixels
	 * @param pxradius radius in pixels
	 * @return array containing the pixel values
	 */
	protected int[] createGradientTable(int size, int pxlight, int pxradius) {
		int[] pixels = new int[size];

		int clbr = backgroundColor.getRed();
		int clbg = backgroundColor.getGreen();
		int clbb = backgroundColor.getBlue();

		int cllr = lightColor.getRed();
		int cllg = lightColor.getGreen();
		int cllb = lightColor.getBlue();
		
		int cr, cg, cb;
		
		int clbackground = 0xFF000000
    	|((clbr << 16)&0x00FF0000)
    	|((clbg	<<  8)&0x0000FF00)
    	|( clbb  	  &0x000000FF);

		int l = Math.max(0, pxlight-pxradius);
		int r = Math.min(size-1, pxlight+pxradius);
		
		for (int i=0;i<l;i++) // outside light, left
			pixels[i] = clbackground;

		for (int i=r+1;i<size;i++) // outside light, right
			pixels[i] = clbackground;

		double factor;
		double factorN;
		for (int i=l;i<=r;i++) { // inside light
			factor =
				1.0 -
				( (double)Math.abs(pxlight-i) // distance d from light source: 0<=d<=pxradius
				 /(double)pxradius                   // divided by radius
				);

			// negative factor
			factorN= 1.0-factor;
			
			// set pixel
			
			cr = (int) (factor*cllr + factorN*clbr); 
			cg = (int) (factor*cllg + factorN*clbg);
			cb = (int) (factor*cllb + factorN*clbb);
			
			
			pixels[i] =    	0xFF000000
	    		|((cr<<16)&0x00FF0000)
	    		|((cg<< 8)&0x0000FF00)
	    		|( cb  	  &0x000000FF);
		}
		
		return pixels;
	}

	public void drawTo(Component comp, Graphics g) {
		// update if necessary
		checkRender(comp);
		
		// get image
		Image buffer = getBuffer();

		int top = 0;
		int bottom = comp.getHeight();
		
		// make the updated area smaller if there are clip bounds
		Rectangle r = g.getClipBounds();
		if (r!=null) {
			if (r.y>0)
				top = r.y;

			if (r.y+r.height<bottom)
				bottom = r.y+r.height;
		}

		// draw 1 pixel height image row by row
		for (int y=top;y<=bottom;y++)
			g.drawImage(buffer, 0, y, comp);
	}
}
