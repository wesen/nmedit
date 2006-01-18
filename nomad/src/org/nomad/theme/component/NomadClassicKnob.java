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
 * Created on Jan 6, 2006
 */
package org.nomad.theme.component;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

import org.nomad.theme.component.model.NomadClassicKnobGraphics;
import org.nomad.theme.component.model.NomadClassicKnobGraphics.ControlCachedGraphics;


/**
 * @author Christian Schneider
 */
public class NomadClassicKnob extends NomadControl {

	//private final Color clDefaultKnobFill = Color.decode("#9B9B9B");
	//private final Color clDefaultKnobOutline = Color.BLACK;

	private ControlCachedGraphics controlGraphics = null;
	private final static ClassicKeyListener classicKeyListener
	  = new ClassicKeyListener();
	private final static ClassicMouseListener classicMouseListener
	  = new ClassicMouseListener();
	private final static ClassicMouseMotionListener classicMouseMotionListener
	  = new ClassicMouseMotionListener();
	
	public NomadClassicKnob() {
		addMouseListener(classicMouseListener);
		addKeyListener(classicKeyListener);
		addMouseMotionListener(classicMouseMotionListener);

		setOpaque(false);
		setFocusable(true);
		setDynamicOverlay(true);
		Dimension d = new Dimension(24,24);
		setPreferredSize(d);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		//deleteOnScreenBuffer();
		
		getAccessibleProperties().rewriteDefaults();
	}
	
	boolean morphEnabled = true;

	public void paintDecoration(Graphics2D g2) {
		controlGraphics = NomadClassicKnobGraphics.obtainGraphicsCache(this, controlGraphics);
		controlGraphics.paintDecorationCache(this, g2);
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		controlGraphics = NomadClassicKnobGraphics.obtainGraphicsCache(this, controlGraphics);
		controlGraphics.paintDynamicOverlay(this, g2);
	}
	
	protected void finalize() throws Throwable {
		if (controlGraphics!=null) {
			controlGraphics.dispose();
		}
		super.finalize();
	}
	
	private static class ClassicKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent event) {
			
			if (event.getSource() instanceof NomadControl) {
				NomadControl control = (NomadControl) event.getSource();
				
				boolean ctrl_pressed = (event.getModifiers() & (KeyEvent.CTRL_DOWN_MASK|KeyEvent.CTRL_MASK))!=0;
				switch(event.getKeyCode()) {
					case KeyEvent.VK_UP: 
						if (ctrl_pressed) control.incMorph(); else control.incValue(); break;
					case KeyEvent.VK_DOWN:
						if (ctrl_pressed) control.decMorph(); else control.decValue(); break;
					case KeyEvent.VK_DELETE:
						if (ctrl_pressed) control.setMorphValue(null); break;
				}
			}
		}
	}

	private static class ClassicMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			if (event.getSource() instanceof NomadControl) {
				((NomadControl)event.getSource()).requestFocus();
			}
		}
	}

	private static class ClassicMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent event) {
			if (event.getSource() instanceof NomadClassicKnob) {
				NomadClassicKnob control = (NomadClassicKnob) event.getSource();
				
				if (SwingUtilities.isLeftMouseButton(event)) {
					control.controlGraphics = NomadClassicKnobGraphics
						.obtainGraphicsCache(control, control.controlGraphics);
					control.setValue(	
						control.getMinValue()
						+ (int) (control.controlGraphics.getMetrics().calcRangeFactor(event.getPoint())
						* control.getRange())	
					);
				}				
			}
		}
	}
}
