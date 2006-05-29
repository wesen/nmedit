package net.sf.nmedit.nomad.util.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

/**
 * Background render implementation that creates a LCD like background.
 * 
 * @author Christian Schneider
 */
public class LCDBackgroundRenderer extends BackgroundRenderer {

	// display color
	private Color clDisplayColor = null;
	
	// light color
	private Color clDisplayLight = null;
	
	// distance in pixel
	private double distance = 10.0; 
	
	// distance in pixel
	private double distancePoint = 30.0; 
	
	// direct light position
	private int dAdditionalLightX = 20;
	private int dAdditionalLightY = 20;

	// used for fixed point arithmetics
	private final static int FACTOR = 10000; 
	private final static double FACTOR4D = 4*FACTOR; 
	private final static double FACTOR6D = 6*FACTOR; 

	public final static Color DEFAULT_DISPLAY_COLOR =  Color.decode("#2721CE");
	public final static Color DEFAULT_LIGHT_COLOR =  Color.decode("#367AF9");
	
	/**
	 * A LCD like background renderer.
	 */
	public LCDBackgroundRenderer() {
		setDefaultColors();
	}

	/**
	 * Creates random settings. 
	 * @param dim the size of the area
	 */
	public void randomizeBehaviour(Dimension dim) {
		distancePoint = (Math.min(dim.width,dim.height)-5)*Math.random()+5.0;
		dAdditionalLightX = (int)(dim.width*Math.random());
		dAdditionalLightY = (int)(dim.height*Math.random());
	}

	/**
	 * Resets the display color and light color to their defaults.
	 */
	public void setDefaultColors() {
		setDisplayColor(DEFAULT_DISPLAY_COLOR);
		setLightColor(DEFAULT_LIGHT_COLOR);
	}
	
	/**
	 * Sets the display color property
	 * @param c the new color
	 */
	public void setDisplayColor(Color c) {
		clDisplayColor = c;
		update();
	}
	
	/**
	 * Sets the light color property
	 * @param c the new color
	 */
	public void setLightColor(Color c) {
		clDisplayLight = c;
		update();
	}
	
	/**
	 * Sets the distance for the border glow in pixel.
	 * @param d the distance
	 */
	public void setBorderGlowSize(float d) {
		distance = d;
		update();
	}

	/**
	 * Renders the lcd background.
	 */
	protected Image renderImage(Component comp) {
		Dimension dim = comp.getSize();
		
		// calculate the componenents size
	    int size = dim.width * dim.height; 

	    // round (ceil) to pixel raster
	    int cdistance = (int) Math.ceil(distance);
	    
	    // *** factors
	    int[] xtableBorder = new int[dim.width];
	    for (int i=0;i<xtableBorder.length-cdistance;i++) {
	    	
	    	if (i<cdistance) {
	    		// linear
	    		xtableBorder[i] = (int)(((double)(cdistance-i)/(double)cdistance)*FACTOR);
	    		xtableBorder[xtableBorder.length-1-i] = xtableBorder[i];
	    	} else {
	    		// nothing
	    		xtableBorder[i] = 0;
	    	}
	
	    }
	    
	    int[] ytableBorder = new int[dim.height];
	    for (int i=0;i<ytableBorder.length-cdistance;i++) {
	    	
	    	if (i<cdistance) {
	    		// linear
	    		ytableBorder[i] = (int)(((float)(cdistance-i)/(float)cdistance)*FACTOR);
	    		ytableBorder[ytableBorder.length-1-i] = ytableBorder[i];
	    	} else {
	    		// nothing
	    		ytableBorder[i] = 0;
	    	}

	    }
	    
	    // display
	    int cdr = clDisplayColor.getRed();
	    int cdg = clDisplayColor.getGreen();
	    int cdb = clDisplayColor.getBlue();
	    int displayColorInt =   0xFF000000
        	|(((int) cdr << 16)&0x00FF0000)
        	|(((int) cdg <<  8)&0x0000FF00)
        	|( (int) cdb       &0x000000FF);

	    // light source
	    int clr = clDisplayLight.getRed();
	    int clg = clDisplayLight.getGreen();
	    int clb = clDisplayLight.getBlue();
	    
	    // current pixel
	    int cr = 0;
	    int cg = 0;
	    int cb = 0;

	    int[] pixels = new int[size];

	    //int right  = dim.width	-cdistance;
	    //int bottom = dim.height	-cdistance;
	    
	    int factorInt= 0;
	    double factor = 0.0f;
	    double factorN = 0.0f;
	    int index = 0;
	    
	    for (int y=0;y<dim.height;y++)
	    {
	    	for (int x=0;x<dim.width;x++)
	    	{
	    		factorInt = xtableBorder[x] + ytableBorder[y];

	    		factorInt+=
	    			Math.min(FACTOR4D,
	    				FACTOR*(
	    					Math.min(Math.abs(x-dAdditionalLightX), FACTOR)/distancePoint
	    				   +Math.min(Math.abs(y-dAdditionalLightY), FACTOR)/distancePoint
	    				)
	    			);
	    		
	    		if (factorInt==0) {
	    			
	    			// inner rect -> only display color
	    			pixels[index] = displayColorInt;
	    		} else {
	    			// factor = factorInt / (2*factor)
	    			factor = (double) Math.log(1.0+((double) factorInt) / FACTOR6D);
	    			
	    			// negative factor
	    			factorN= 1.0f-factor;
	    			
	    			// set pixel
	    			
	    			cr = (int) (factor*clr + factorN*cdr); 
	    			cg = (int) (factor*clg + factorN*cdg);
	    			cb = (int) (factor*clb + factorN*cdb);
	    			
	    	        pixels[index] =0xFF000000
	    	          |((cr << 16)&0x00FF0000)
	    	          |((cg <<  8)&0x0000FF00)
	    	          |( cb       &0x000000FF);
	    		}

	        	index ++;
	    	}
	    }

	    return createImage(comp, dim, pixels);
	}
	
}
