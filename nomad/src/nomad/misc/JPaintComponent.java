package nomad.misc;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class JPaintComponent extends JComponent {

	private int bufferw = 0;
	private int bufferh = 0;
	private Image buffer = null;
	
	public JPaintComponent() {
		setDoubleBuffered(false); // we do buffering ourselves
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent event) {
				buffer = null;
				updateUI();
			}
		});
	}

	protected void paintBuffer(Graphics g) {
		// paint buffer
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private boolean mustRepaintBuffer = false;
	
	public void repaint() {
		mustRepaintBuffer = true;
		super.repaint();
	} 

    public void paintComponent(Graphics g) {
    	
    	int w = getWidth();
    	int h = getHeight();
    	
    	if (bufferw!=w || bufferh!=h || buffer==null) {

    		// create new buffer
			bufferw = w;
			bufferh = h;
			buffer = null;
			
			if (bufferw>0 && bufferh>0) {
				if (g instanceof Graphics2D) {
					buffer = ((Graphics2D)g).getDeviceConfiguration()
						.createCompatibleImage(bufferw, bufferh,
							isOpaque()?Transparency.OPAQUE:Transparency.TRANSLUCENT
							 // Create an image that supports arbitrary levels of transparency
					);
				} else {
					buffer = createImage(bufferw, bufferh);
				}
				mustRepaintBuffer |= buffer != null;
			}
    	}

		if (buffer!=null) {
			if (mustRepaintBuffer) {
	        	mustRepaintBuffer = false;
	        	Graphics bgraphics = buffer.getGraphics().create();
	        	if (!isOpaque()) {
	        		// erase buffer	        	
		        	if (bgraphics instanceof Graphics2D) {
		        		Graphics2D g2 = (Graphics2D) bgraphics.create();
		        		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		        		g2.setComposite(AlphaComposite.Clear);
		        		g2.fill(new Rectangle2D.Double(0,0, bufferw-1, bufferh-1));
		        	}
	        	}
	        	paintBuffer(bgraphics);
			}
    		g.drawImage(buffer, 0, 0, this);
		} else {
    		paintBuffer(g);
		}
    }
}
