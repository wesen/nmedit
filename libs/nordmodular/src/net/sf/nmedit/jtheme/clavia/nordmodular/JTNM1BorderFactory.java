/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jan 9, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * @author Christian Schneider
 */
public class JTNM1BorderFactory {

	private static final Border NordEditor311LoweredBorder = new NordEditor311Border(false,1);
	private static final Border NordEditor311RaisedBorder = new NordEditor311Border(true,1); 
	private static final Border NordEditor311GroupBoxBorder = new NordEditor311GroupboxBorder();

	public static Border createNordEditor311Border() {
		return createNordEditor311LoweredBorder();
	}

	public static Border createNordEditor311RaisedBorder() 
	{
		return NordEditor311RaisedBorder;
	}

	public static Border createNordEditor311LoweredBorder() {
		return NordEditor311LoweredBorder;
	}

	public static Border createNordEditor311RaisedBorder(int size) {
		return new NordEditor311Border(true,size);
	}

	public static Border createNordEditor311LoweredBorder(int size) {
		return new NordEditor311Border(false,size);
	}

	public static Border createNordEditor311Border(boolean raised, int size) {
		return new NordEditor311Border(raised,size);
	}

	public static Border createNordEditor311GroupBoxBorder(){
		return NordEditor311GroupBoxBorder;
	}
	
	private static class NordEditor311Border implements Border {

		public final static Color clLight = Color.decode("#EDEEEF");
		public final static Color clShadow = Color.decode("#929292");
		public Color clupper;
		public Color cllower;
		public int size;
		
		public NordEditor311Border(boolean raised, int size) {
			this.size=Math.max(1,size);
			if (raised) {
				clupper = clLight;
				cllower = clShadow;
			} else {
				clupper = clShadow;
				cllower = clLight;
			}
		}
		
		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			w--; h--;
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(x,y);
			g2.setStroke(new BasicStroke(size));
			
			g2.setColor(clupper);
			g2.drawLine(size,0,w-size-1,0);
			g2.drawLine(0,x+size,0,h-size-1);
			
			g2.setColor(cllower);
			g2.drawLine(w,h-size,w,size);
			g2.drawLine(w-size,h,size,h);
			
			g2.dispose();
		}

		public Insets getBorderInsets(Component component) {
			return new Insets(size,size,size,size);
		}

		public boolean isBorderOpaque() {
			return false;
		}
		
	}

	private static class NordEditor311ButtonBorder implements Border {

		public final static Color clTop1Up = Color.decode("#b3b3b1"); //"#A3A3A3"
		public final static Color clTop2Up = Color.decode("#DBDBDB");
		public final static Color clTop3Up = Color.decode("#C1C1C3");
		public final static Color clBot1Up = Color.decode("#757575");
		public final static Color clBot2Up = Color.decode("#424242");
		public final static Color clBot3Up = Color.decode("#8D8D8B");
		
		public Color clTop1; 
		public Color clTop2;
		public Color clTop3;
		public Color clBot1;
		public Color clBot2;
		public Color clBot3;
		
		public Color clupper;
		public Color cllower;
		public int size=3;
		
		public NordEditor311ButtonBorder(boolean raised) {
			if (raised) {
				clTop1=clTop1Up;
				clTop2=clTop2Up;
				clTop3=clTop3Up;

				clBot1=clBot1Up;
				clBot2=clBot2Up;
				clBot3=clBot3Up;
			} else {
				clBot1=clBot3Up;
				clBot2=clTop3Up;
				clBot3=NomadClassicColors.BUTTON_BACKGROUND;

				clTop1=clTop1Up;
				clTop2=clBot2Up;
				clTop3=clBot1Up;
			}
		}
		
		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			Graphics2D g2 = (Graphics2D) g;//.create();
			
			int tmpw = w; int tmph=h; int tmpx=x; int tmpy=y;
			
			w--;h--;
			g2.setColor(clTop1);
			g2.drawLine(x+1,y,x+w,y); // hrz
			g2.drawLine(x,y+1,x,y+h); // vrt
			g2.setColor(clTop2);
			x++; y++;w-=2;h-=2;
			g2.drawLine(x,y,x+w,y); // hrz
			g2.drawLine(x,y,x,y+h); // vrt
            /*
			g2.setColor(clTop3);
			x++; y++;w-=2;h-=2;
			g2.drawLine(x,y,x+w,y); // hrz
			g2.drawLine(x,y,x,y+h); // vrt*/
			
			
			w=tmpw;h=tmph;x=tmpx;y=tmpy;
			w--;h--;
			g2.setColor(clBot1);
			g2.drawLine(x,y+h,x+w,y+h); // hrz
			g2.drawLine(x+w,y,x+w,y+h); // vrt
			g2.setColor(clBot2);
			x++; y++;w-=2;h-=2;
			g2.drawLine(x,y+h,x+w,y+h); // hrz
			g2.drawLine(x+w,y,x+w,y+h); // vrt
            /*
			g2.setColor(clBot3);
			x++; y++;w-=2;h-=2;
			g2.drawLine(x,y+h,x+w,y+h); // hrz
			g2.drawLine(x+w,y,x+w,y+h); // vrt
            */
			//g2.dispose();
		}

		public Insets getBorderInsets(Component component) {
            //return new Insets(3,3,3,3);
            return new Insets(2,2,2,2);
		}

		public boolean isBorderOpaque() {
			return false;
		}
		
	}
	
	private static class NordEditor311GroupboxBorder implements Border {

		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			g.setColor(NomadClassicColors.GROUPBOX_BORDER);
			g.drawLine(x,y+2,x+2,y); // corner top,left
			g.drawLine(x+w-1-2,y,x+w-1,y+2); // corner top,right
			g.drawLine(x,y+h-1-2,x+2,y+h-1); // corner bottom,left
			g.drawLine(x+w-1-2,y+h-1,x+w-1,y+h-1-2); // corner bottom,right

			g.drawLine(x,y+2,x,y+h-1-2);//left
			g.drawLine(x+w-1,y+2,x+w-1,y+h-1-2);//right
			g.drawLine(x+2,y,x+w-1-2,y);//top
			g.drawLine(x+2,y+h-1,x+w-1-2,y+h-1);//bottom
		}

		public Insets getBorderInsets(Component comp) {
			return new Insets(3,3,3,3);
		}

		public boolean isBorderOpaque() {
			return false;
		}
		
	}


	public static Border createNordEditor311RaisedButtonBorder() {
		return new NordEditor311ButtonBorder(true);
	}

	public static Border createNordEditor311LoweredButtonBorder() {
		return new NordEditor311ButtonBorder(false);
	}
	
}
