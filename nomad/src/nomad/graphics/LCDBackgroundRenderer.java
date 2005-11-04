package nomad.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

public class LCDBackgroundRenderer extends BackgroundRenderer {

	private Color clDisplayColor = null; 
	private Color clDisplayLight = null;
	private double distance = 10.0f; // pixel 
	private double distancePoint = 30.0f; // pixel
	private int dAdditionalLightX = 20;
	private int dAdditionalLightY = 20;

	private final static int FACTOR = 10000; 
	private final static double FACTOR4D = 4*FACTOR; 
	private final static double FACTOR6D = 6*FACTOR; 
	
	public LCDBackgroundRenderer() {
		setDefaultColors();
	}
	
	// variable setup

	public void randomizeBehaviour(Dimension dim) {
		distancePoint = (Math.min(dim.width,dim.height)-5)*Math.random()+5.0;
		dAdditionalLightX = (int)(dim.width*Math.random());
		dAdditionalLightY = (int)(dim.height*Math.random());
	}

	public void setDefaultColors() {
		setDisplayColor(Color.decode("#2721CE"));
		setLightColor(Color.decode("#367AF9"));
	}
	
	public void setDisplayColor(Color c) {
		clDisplayColor = c;
	}
	
	public void setLightColor(Color c) {
		clDisplayLight = c;
	}
	
	public void setDistance(float d) {
		distance = d;
	}

	protected Image renderImage(Component comp, Dimension dim) {
		// we don't use the super-implementation

		// calculate the componenents size
	    int size = dim.width * dim.height; 

	    // round (ceil) to pixel raster
	    int cdistance = (int) Math.ceil(distance);
	    
	    // *** factors
	    int[] xtable = new int[dim.width];
	    for (int i=0;i<xtable.length-cdistance;i++)
	    	
	    	if (i<cdistance) {
	    		// linear
	    		xtable[i] = (int)(((double)(cdistance-i)/(double)cdistance)*FACTOR);
	    		xtable[xtable.length-1-i] = xtable[i];
	    	} else {
	    		// nothing
	    		xtable[i] = 0;
	    	}
	    
	    int[] ytable = new int[dim.height];
	    for (int i=0;i<ytable.length-cdistance;i++)
	    	
	    	if (i<cdistance) {
	    		// linear
	    		ytable[i] = (int)(((float)(cdistance-i)/(float)cdistance)*FACTOR);
	    		ytable[ytable.length-1-i] = ytable[i];
	    	} else {
	    		// nothing
	    		ytable[i] = 0;
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
	    		factorInt = xtable[x] + ytable[y];
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
