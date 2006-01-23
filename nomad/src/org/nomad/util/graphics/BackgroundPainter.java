package org.nomad.util.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * A class that fills an area with an image. 
 * 
 * @author Christian Schneider
 */
public class BackgroundPainter {

	// the fill
	private Image tile = null;
	
	// image size
	private int iw = 0;
	private int ih = 0;

	/**
	 * Creates a BackgroundPainter object for a Component
	 * @param comp the component that should be filled 
	 * @param tile the image used to fill the background
	 */
	public BackgroundPainter(Image tile) {
		this.tile = tile;
		
		if (tile!=null) {
			// makes the image loaded
			ImageIcon icon = new ImageIcon(tile);
			iw = icon.getIconWidth();
			ih = icon.getIconHeight();
			if (iw<=0||ih<=0)
				this.tile = null;
		}
	}
	
	/**
	 * Returns the fill image
	 * @return the fill image
	 */
	public Image getTile() {
		return tile;
	}

	/**
	 * Fills the background with the image. If the image is null
	 * the background will be painted using the components background
	 * color.
	 * 
	 * The clip bounds of the graphics object are checked so that
	 * only the necessary parts are filled.
	 * 
	 * @param g the graphics context
	 */
	public void paintBackground(Graphics g, Component comp) {
		paintBackground(g, comp, comp.getBackground());
	}

	/**
	 * Fills the background with the image. If the image is null
	 * the background will be painted using the alternativeBackground
	 * color.
	 * 
	 * The clip bounds of the graphics object are checked so that
	 * only the necessary parts are filled.
	 * 
	 * @param g the graphics context
	 * @param alternativeBackground alternative fill color
	 */
	public void paintBackground(Graphics g, Component comp, Color alternativeBackground) {
		int tw = comp.getWidth();
		int th = comp.getHeight();

		Rectangle clip = g.getClipBounds();
		if (tile!=null) {
			// align clip bounds
			int x = clip.x;
			int y = clip.y;
			int r = x+clip.width;
			int b = y+clip.height;
			
			if (clip!=null) {
				int deltax = clip.x % iw;
				int deltay = clip.y % ih;

				x -= deltax;
				r += deltax;
				y -= deltay;
				b += deltay;
			}

			int nully = y;

			while (x<r) {
				while (y<b) {
					g.drawImage(tile, x, y, comp);
					y+=ih;
				}
				y=nully;
				x+=iw;
			}

		} else {
			g.setColor(alternativeBackground);
			if (clip==null) // repaint all
				g.fillRect(0, 0, tw, th);
			else
				g.fillRect(clip.x, clip.y, clip.width, clip.height);
		}
	}
	
}
