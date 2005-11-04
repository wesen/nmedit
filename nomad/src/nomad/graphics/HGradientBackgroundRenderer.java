package nomad.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class HGradientBackgroundRenderer extends BackgroundRenderer {

	private double light = 1.0/4.0;
	private double radius = 3.0/4.0;
	private Color lightColor = Color.WHITE;
	private Color backgroundColor = Color.GRAY;
	
	public HGradientBackgroundRenderer() {
		super();
	}

	public void setLight(double light) {
		this.light = Math.max(Math.min(light, 1.0), 0);
	}

	public void setRadius(double radius) {
		this.radius = Math.max(Math.min(radius, 1.0), 0);
	}
	
	public void setLightColor(Color c) {
		this.lightColor = c;
	}
	
	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}
	
	protected void checkRender(Component comp, Dimension dim) {
		// overwrite because we only want to check width, not height
		if ((isRenderingForced() || (this.getSize().width!=dim.width))&&dim.width>0&&dim.height>0) {
			render(comp, dim);
			hasRendered();
		}
	}

	protected Image renderImage(Component comp, Dimension dim) {
		int[] pixels = createGradientTable(dim.width, (int)(dim.width*light), (int)(dim.width*radius));

		// create image
		return createImage(comp, new Dimension(dim.width, 1), pixels);
	}

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

	public void drawTo(Component comp, Dimension dim, Graphics g) {
		checkRender(comp, dim);
		
		// overwrite drawTo
		Image buffer = getBuffer();

		int top = 0;
		int bottom = dim.height-1;
		
		Rectangle r = g.getClipBounds();
		if (r!=null) {
			if (r.y>0)
				top = r.y;
			if (r.y+r.height<bottom)
				bottom = r.y+r.height;
		}
		
		for (int y=top;y<=bottom;y++)
			g.drawImage(buffer, 0, y, comp);
	}
}
