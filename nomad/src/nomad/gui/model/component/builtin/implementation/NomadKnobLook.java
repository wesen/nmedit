/*  
 * Adapted DKnob.java by Joakim Eriksson
 */

// TODO -Combine with JModKnob.

package nomad.gui.model.component.builtin.implementation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * @author Ian Hoogebom
 * @hidden
 */
public class NomadKnobLook
{
    public final static Color DEFAULT_FOCUS_COLOR = new Color(0x8080ff);
    
	public final static float START = 225;
    public final static float LENGTH = 270;
    public final static float PI = (float) Math.PI;
    public final static float START_ANG = (START/360)*PI*2;
    public final static float LENGTH_ANG = (LENGTH/360)*PI*2;
    public final static float MULTIP = 180 / PI; 
    
    public final static Color DEFAULT_BOTTOM_COLOR = new Color(0xB0B0B0);
    public final static Color DEFAULT_MIDDLE_COLOR = new Color(0xD0D0D0);
    public final static Color DEFAULT_TOP_COLOR = new Color(0x707070);

    private Color BOTTOM_COLOR = DEFAULT_BOTTOM_COLOR;
    private Color MIDDLE_COLOR = DEFAULT_MIDDLE_COLOR;
    private Color TOP_COLOR = DEFAULT_TOP_COLOR;
    
    private Color outside_needle_color = Color.GRAY;
    private Color outside_circle_color = Color.GREEN;
    
    private boolean forceRepaintBackground = true;

	private BufferedImage background = null;
	private Dimension size = null;

    public NomadKnobLook(int type) {
		size = type == NomadKnob.LARGE ? NomadKnob.SIZE_LARGE : NomadKnob.SIZE_SMALL;
		background = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void setBottomColor(Color c) {
    	BOTTOM_COLOR = c;
    	changed();
    }
    
    public void setMiddleColor(Color c) {
    	MIDDLE_COLOR = c;
    	changed();
    }
    
    public void setTopColor(Color c) {
    	TOP_COLOR = c;
    	changed();
    }
    
    public void setOutsideNeedle(Color c) {
    	outside_needle_color = c;
    	//changed(); does not affect background
    }
    
    public void setOutsideCircle(Color c) {
    	outside_circle_color = c;
    	//changed(); does not affect background
    }
    
    private void changed() {
    	forceRepaintBackground = true;
    }
    
    public Color getBottomColor() {
    	return BOTTOM_COLOR;
    }
    
    public Color getMiddleColor() {
    	return MIDDLE_COLOR;
    }
    
    public Color getTopColor() {
    	return TOP_COLOR;
    }
    
    public Color getOutsideNeedle() {
    	return outside_needle_color;
    }
    
    public Color getOutsideCircle() {
    	return outside_circle_color;
    }
    
    public void setDragType(int type) {
    }

    public void paintTo(Graphics g, ImageObserver observer) {
    	if (forceRepaintBackground) {
    		forceRepaintBackground = false;
    		Graphics2D g2 = background.createGraphics();
    		g2.setComposite(AlphaComposite.Clear);
    		g2.fill(new Rectangle2D.Double(0,0, size.width-1, size.height-1));
    		// we must use a new graphics object
    		paintBackground(background.createGraphics());
    	}
		g.drawImage(background, 0, 0, observer);
    }

    // Paint the DKnob
    protected void paintBackground(Graphics2D g) {

    	g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    	g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    	
        int x = 0;
        int y = 0;
        
    	int width = size.width;
        int height = size.height;
        int offset = 2;
        
    	int size = Math.min(width, height) - (offset*2);

        // Circle top..?
        g.setColor(BOTTOM_COLOR);
        g.drawOval(offset+2, offset+2, size-6, size-6);

        // Min / Max markers
        g.setColor(Color.black);
        x = offset + size/2 + (int)((6+size/3) * Math.cos(START_ANG));
        y = offset + size/2 - (int)((6+size/3) * Math.sin(START_ANG));
        g.drawLine(offset + size/2, offset + size/2, x, y);
        
        x = offset + size/2 + (int)((6+size/3) * Math.cos(START_ANG - LENGTH_ANG));
        y = offset + size/2 - (int)((6+size/3) * Math.sin(START_ANG - LENGTH_ANG));
        g.drawLine(offset + size/2, offset + size/2, x, y);
        
        // Top
        g.setColor(MIDDLE_COLOR);
        g.fillOval(offset+3, offset+3, size-6, size-6);
        g.setColor(TOP_COLOR);
        g.fillOval(offset+4, offset+4, size-8, size-8);
    }
    
}

