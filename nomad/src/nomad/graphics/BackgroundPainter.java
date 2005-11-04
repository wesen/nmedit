package nomad.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class BackgroundPainter {

	private Component comp = null;
	private Image tile = null;
	private int iw = 0;
	private int ih = 0;
	
	public BackgroundPainter(Component comp, Image tile) {
		this.comp = comp;
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
	
	public Image getTile() {
		return tile;
	}

	public void paintBackground(Graphics g) {
		paintBackground(g, comp.getBackground());
	}

	public void paintBackground(Graphics g, Color alternativeBackground) {
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
