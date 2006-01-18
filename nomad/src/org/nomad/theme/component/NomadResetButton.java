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
 * Created on Jan 8, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Christian Schneider
 */
public class NomadResetButton extends NomadControl {

	private Metrics m = new Metrics();
	private final static Color defaultBackground = Color.decode("#61A387");
	private final static Color defaultForeground = Color.decode("#74E25D");
	private final static Color clOutline = new Color(0, 0, 0, 0.4f);
	private boolean upsidedown = true;
	private boolean rememberState = false;
	private final Color clHighlight = new Color(245, 245, 220, 180);
	
	public NomadResetButton() {
		setOpaque(false);
		setDynamicOverlay(true);
		setSize(9,6);
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		setPreferredSize(getSize());
		
		setBackground(defaultBackground);
		setForeground(defaultForeground);
		
		setDefaultValue(new Integer(30));
		
		ChangeListener updater = new ChangeListener(){
			public void stateChanged(ChangeEvent event) {
				testIfStateChanged();
			}};


		addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent event) {
					requestFocus();
				}});
		
		setFocusable(true);
		
		addValueChangeListener(updater);
		addValueOptionChangeListener(updater);
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				if (SwingUtilities.isLeftMouseButton(event)) {
					m.update();
					Integer def = getDefaultValue();
					if (def!=null && m.polygonFill.getBounds().contains(event.getPoint())) {
						setValue(def.intValue());
					}
				}
			}});
	}
	
	protected void testIfStateChanged() {
		if (inDefaultState()!=rememberState) {
			rememberState=!rememberState; // update memory
			repaint();
		}
	}

	protected boolean inDefaultState() {
		Integer def = getDefaultValue();
		return (def!=null && def.intValue()==getValue());
	}
	
	public class Metrics {
		int w = 0;
		int h = 0;

		Polygon polygonFill = null;
		Polygon polygonDraw = null;
		
		public void update() {
			if (w==getWidth()&&h==getHeight()&&polygonFill!=null&&polygonDraw!=null)
				return;
			
			w = getWidth(); h = getHeight();
			int s = (1-w%2); // is 0(uneven) or 1(even) , shift so that middle is not even
			int thypotenuse = 0;
			int theight = 0;
			int middle = w/2-s;

			// hypotenuse c, hight h : condition : c = 2*h-1
			theight = h;
			thypotenuse = w-s;
			if (thypotenuse!=2*h-1) {
				theight = h;
				thypotenuse = 2*theight-1;
			}

			int left  = middle - (int) Math.floor ( thypotenuse/2.0d );
			int right = middle + (int) Math.floor ( thypotenuse/2.0d );
			int top   = h/2 - (int) Math.floor ( theight/2.0d );
			int bottom= h/2 + (int) Math.floor ( theight/2.0d );

			if (upsidedown) {
				polygonFill = new Polygon();
				polygonFill.addPoint(left, top+1);
				polygonFill.addPoint(middle, bottom+1);
				polygonFill.addPoint(right+1, top+1);
				
				polygonDraw = new Polygon();
				polygonDraw.addPoint(left, top+1);
				polygonDraw.addPoint(middle, bottom-1);
				polygonDraw.addPoint(right, top+1);
			} else {
				polygonFill = new Polygon();
				polygonFill.addPoint(left, bottom-1);
				polygonFill.addPoint(middle, top+1);
				polygonFill.addPoint(right, bottom-1);
				
				polygonDraw = new Polygon();
				polygonDraw.addPoint(left, bottom-1);
				polygonDraw.addPoint(middle, top+1);
				polygonDraw.addPoint(right, bottom-1);
			}
		}
	}

	protected void configureGraphics(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	}
	
	public void paintDecoration(Graphics2D g2) {
		configureGraphics(g2);
		m.update();
		g2.setColor(getBackground());
		g2.fill(m.polygonFill);
		g2.setColor(clOutline);
		g2.draw(m.polygonDraw);
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		if (inDefaultState()) {
			configureGraphics(g2);
			m.update();
			
			if (hasFocus()) {
				// we highlight the buttons outline
				g2.setColor(clHighlight);
				// outer outline
				g2.draw(m.polygonDraw);
			}
			
			Color f = getForeground();
			g2.setColor(new Color(f.getRed(), f.getGreen(), f.getBlue(), 165));
			g2.fill(m.polygonFill);
		}
	}
	
}
