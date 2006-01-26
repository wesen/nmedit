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
 * Created on Jan 25, 2006
 */
package org.nomad.editor.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.NomadComponent;

public class WorkspacePanel extends JPanel implements ContainerListener, ComponentListener {

	private NomadComponent observed = null;
	private ComponentLocationObserver clo = new ComponentLocationObserver();
	private InvalidComponentAction ica = new InvalidComponentAction();
	private int grid = 5;

	public WorkspacePanel() {
		addContainerListener(this);
		addComponentListener(this);
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createLoweredBevelBorder());
		addMouseListener(ica);
		setBackground(Color.WHITE);
	}

	public void setObserved(Component component) {
		clo.setObserved(null);
		if (component instanceof NomadComponent) {
			this.observed = (NomadComponent) component;
			alignObserved();
			clo.setObserved(observed);
		}
	}
	
	public NomadComponent getObserved() {
		return observed;
	}
	
	public void alignObserved() {
		if (observed!=null) {
			observed.setLocation(
					(getWidth()-observed.getWidth())/2,
					(getHeight()-observed.getHeight())/2
			);
		}
		repaint();
	}
	
	public void componentResized(ComponentEvent event) 	{ alignObserved(); }
	public void componentAdded(ContainerEvent event) 	{ setObserved(event.getChild()); }
	public void componentRemoved(ContainerEvent event) 	{ setObserved(null); }
	public void componentMoved(ComponentEvent event) 	{ }
	public void componentShown(ComponentEvent event) 	{ }
	public void componentHidden(ComponentEvent event) 	{ }

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; 
			
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (observed != null) {
			
			int ox = observed.getX();
			int oy = observed.getY();
			
			int dgrid = grid*2; 
			
			Color[] cLine = new Color[] {
				Color.decode("#DFE1D0"),
				Color.LIGHT_GRAY // dark
			};
			g2.setColor(cLine[0]);
			for (int x=(ox%dgrid); x<getWidth(); x+=dgrid*2) {
				g2.drawLine(x, 0, x, getHeight());
			}
			g2.setColor(cLine[1]);
			for (int x=(ox%dgrid)+dgrid; x<getWidth(); x+=dgrid*2) {
				g2.drawLine(x, 0, x, getHeight());
			}
			g2.setColor(cLine[1]);
			for (int y=(oy%dgrid); y<getHeight(); y+=dgrid*2) {
				g2.drawLine(0, y, getWidth(), y);
			}
			g2.setColor(cLine[0]);
			for (int y=(oy%dgrid)+dgrid; y<getHeight(); y+=dgrid*2) {
				g2.drawLine(0, y, getWidth(), y);
			}
			
			/*
			Stroke stroke = g2.getStroke();
			g2.setStroke(new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 2 }, 0 ));
			
			g.setColor(Color.BLUE);
			g.drawRect(observed.getX()-1, observed.getY()-1,
					observed.getWidth()+1, observed.getHeight()+1);
			
			g2.setStroke(stroke);
			*/
			
			g2.setColor(NomadClassicColors.alpha(Color.RED, 160));
			for (Component c : clo.invalid) {
				g2.fill(absoluteBounds(c));
			}
			
		}
	}

	public Rectangle absoluteBounds(Component c) {
		Point l = absolutLocation(c);
		Dimension s = c.getSize();
		return new Rectangle(l.x-1, l.y-1, s.width+2, s.height+2);
	}

	public Point absolutLocation(Component c) {
		return new Point(c.getX()+c.getParent().getX(), c.getY()+c.getParent().getY());
	}
	
	private class ComponentLocationObserver implements ContainerListener, ComponentListener {

		private NomadComponent observed = null;
		private ArrayList<Component> invalid = new ArrayList<Component>();
		
		public void setObserved(NomadComponent c) {
			
			invalid.clear();
			
			if (observed != null) {
				observed.removeContainerListener(this);
				for (Component child : observed.getComponents() )
					child.removeComponentListener(this);
			}
			
			observed = c;
			
			if (observed != null) {
				observed.addContainerListener(this);
				for (Component child : observed.getComponents() ) {
					child.addComponentListener(this);
					if (hasInvalidLocation(child)) {
						invalid.add(child);
					}
				}
			}
		}
		
		public boolean hasInvalidLocation(Component c) {
			if (c.getX()<0 || c.getY() <0) return true;
			int r = c.getX()+c.getWidth();
			int b = c.getY()+c.getHeight();
			
			Component p = c.getParent();
			if (r>=p.getWidth() || b>p.getHeight()) return true;
			return false;
		}
		
		public void componentAdded(ContainerEvent event) {
			event.getChild().addComponentListener(this);
		}

		public void componentRemoved(ContainerEvent event) {
			event.getChild().removeComponentListener(this);
		}

		public void update(Component c) {
			if (hasInvalidLocation(c) && (!invalid.contains(c)) ) {
				invalid.add(c);
				repaint();
			} else if ((!hasInvalidLocation(c)) && invalid.contains(c)) {
				invalid.remove(c);
				repaint();
			} else {
				repaint();
			}
		}
		
		public void componentResized(ComponentEvent event) {
			update( event.getComponent() );
		}

		public void componentMoved(ComponentEvent event) {
			update( event.getComponent() );
		}

		public void componentShown(ComponentEvent event) { }

		public void componentHidden(ComponentEvent arg0) { }
		
	}
	
	private class InvalidComponentAction implements MouseListener {

		public void mouseClicked(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent event) {
			for (Component c: clo.invalid) {
				
				if (absoluteBounds(c).contains(event.getPoint())) {

					boolean isset = false;
					
					if (c.getX()<0) {
						c.setLocation(new Point(0, c.getY()));
						isset = true;
					}
					if (c.getY()<0) {
						c.setLocation(new Point(c.getX(), 0));
						isset = true;
					}
					
					if (!isset) {
						if (c.getX()+c.getWidth()>c.getParent().getWidth())
							c.setLocation(new Point(
									Math.max(0, c.getParent().getWidth()-c.getWidth()), 
									c.getY()));
						if (c.getY()+c.getHeight()>c.getParent().getHeight())
							c.setLocation(new Point(
									c.getX(), Math.max(0, c.getParent().getHeight()-c.getHeight())));
					}
					
					
					break;
				}
			}
		}

		public void mouseReleased(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
