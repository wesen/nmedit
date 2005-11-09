package nomad.misc;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;

import javax.swing.JComponent;

/**
 * A component using it's own double buffering mechanism.
 * The builtin double buffer is disabled by default.
 * The class is intended to use for custom components that
 * have a time expensive paint operation but do not change
 * their appearance frequently. 
 * 
 * Instead of <code>paintComponent</code> the <code>paintBuffer</code> method 
 * must be used to draw the component.
 * 
 * The <code>isOpaque()</code> is used to determine wether to use
 * a Buffer that supports transparency or not.
 * 
 * The component is opaque by default.
 * 
 * The advantage of using a custom double buffering is that we do
 * not need to use <code>VolatileImage</code> as double buffer
 * as swing components do. The <code>VolatileImage</code> may
 * loose it's content anytime and requires more repaints.
 * 
 * @author Christian Schneider
 * @see java.awt.image.VolatileImage
 */
public class JPaintComponent extends JComponent {

	// width of the current buffer
	private int bufferw = -1;
	
	// height of the current buffer
	private int bufferh = -1;
	
	// the current buffer
	private Image buffer = null;
	
	// can be used to disable the custom buffering
	// the property must not be changed at runtime
	private static boolean directRendering = false;

	// used to indicate wether the buffer must be repainted or not 
	private boolean mustRepaintBuffer = false;
	
	/**
	 * Creates a instance using it's own double buffering.
	 */
	public JPaintComponent() {
		// opaque by default
		setOpaque(true);
		// disable builtin double buffering if direct rendering is disabled
		setDoubleBuffered(directRendering);
	}

	/**
	 * Paints the component to the buffer. Overwrite this
	 * method instead of <code>paintComponent()</code> to
	 * implement custom rendering.
	 * 
	 * @param g the graphics object
	 */
	protected void paintBuffer(Graphics g) {
		// paint buffer
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void setOpaque(boolean enable) {
		mustRepaintBuffer = true;
		super.setOpaque(enable);
	}

	public void repaint() {
		mustRepaintBuffer = true;
		super.repaint();
	} 

	/**
	 * Do not overwrite <code>paintComponent()</code> but
	 * instead overwrite <code>paintBuffer()</code>
	 */
    public void paintComponent(Graphics g) {
    	if (directRendering) {
    		// just draw to the current context
    		paintBuffer(g);
    	} else {
    		// custom buffering
	    	
    		// get current component size
	    	int w = getWidth();
	    	int h = getHeight();
	    	
	    	// if size has changed or the buffer is null we must recreate the buffer
	    	if (bufferw!=w || bufferh!=h || buffer==null) {
				bufferw = w;   // remember current size
				bufferh = h;
				buffer = null;

				if (bufferw>0 && bufferh>0) { // only create image if it has a valid size
					if (g instanceof Graphics2D) {
						buffer = ((Graphics2D)g).getDeviceConfiguration()
							.createCompatibleImage(bufferw, bufferh,
								isOpaque()
									? Transparency.OPAQUE
								    : Transparency.TRANSLUCENT
						);
					} else {
						buffer = createImage(bufferw, bufferh);
					}
					// if the buffer is not null, we have to repaint it
					mustRepaintBuffer |= buffer != null;
				}
	    	}
	
			if (buffer!=null) { // the buffer exists and is valid
				
				if (mustRepaintBuffer) {
		        	mustRepaintBuffer = false;
		        	Graphics bgraphics = buffer.getGraphics().create();
		        	if (!isOpaque()) {
		        		// erase buffer with transparent pixels	        	
			        	if (bgraphics instanceof Graphics2D) {
			        		Graphics2D g2 = (Graphics2D) bgraphics.create();
			        		g2.setComposite(AlphaComposite.Clear);
			        		g2.fill(new Rectangle(0,0,bufferw, bufferh));
			        		// free resources, better now than later
			        		g2.dispose();
			        	}
		        	}
		        	
		        	// draw to the buffer
		        	paintBuffer(bgraphics);
		        	
		        	// free resources, better now than later
		        	bgraphics.dispose();
				}

				// flip the buffer to the screen
	    		g.drawImage(buffer, 0, 0, this);

			} else {

				// no buffer there, so we draw direct to the graphics context
	    		paintBuffer(g);
			}
	    }
    }

}
