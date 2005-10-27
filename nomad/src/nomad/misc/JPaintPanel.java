package nomad.misc;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JPaintPanel extends JPanel {
	
    public void paint(final Graphics g) {
    	super.paint(g);
    	SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					doPainting(g);
				}
	    	}
    	);
    }
	
	protected void doPainting(Graphics g) {
		// do painting here
	}
}
