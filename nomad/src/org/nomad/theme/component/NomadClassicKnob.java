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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.nomad.theme.component.model.KnobMetrics;
import org.nomad.theme.component.model.KnobPaintManager;
import org.nomad.theme.component.model.KnobPainter;

/**
 * @author Christian Schneider
 */
public class NomadClassicKnob extends NomadControl {

	//private final Color clDefaultKnobFill = Color.decode("#9B9B9B");
	//private final Color clDefaultKnobOutline = Color.BLACK;

	private final static KnobPainter knobPainter = new KnobPainter();
	private final static KnobPaintManager paintManager = new KnobPaintManager();
	private KnobPaintManager.CachedKnobGraphics graphics = null;
	
	// private ControlCachedGraphics controlGraphics = null;
	
	public NomadClassicKnob() {
		setFocusable(true);
		setDynamicOverlay(true);
		Dimension d = new Dimension(24,24);
        enableEvents(KeyEvent.KEY_EVENT_MASK);
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        enableEvents(MouseEvent.MOUSE_MOTION_EVENT_MASK);
		setDefaultSize(d);
		setSize(d);
		setKnobPainter(knobPainter); // bad, 
	}

	protected static void setKnobPainter(KnobPainter painter) {
		paintManager.setKnobPainter(painter);
	}
	
	boolean morphEnabled = true;

	public void paintDecoration(Graphics2D g2) {
		(graphics = paintManager.obtainGraphics(this, graphics)).paintDecoration(g2);
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		(graphics = paintManager.obtainGraphics(this, graphics)).paintDynamicOverlay(this, g2);
	}
	
	protected void finalize() throws Throwable {
		if (graphics!=null) {
			graphics.unregister();
		}
		super.finalize();
	}

    protected void processKeyEvent(KeyEvent event)
    {
        if (event.getID() == KeyEvent.KEY_PRESSED)
        {
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
        super.processKeyEvent(event);
    }
    
    protected void processMouseEvent(MouseEvent event)
    {
        if (event.getID() == MouseEvent.MOUSE_PRESSED)
        {
            if (event.getSource() instanceof NomadControl) {
                ((NomadControl)event.getSource()).requestFocus();
            }
        }
        super.processMouseEvent(event);
    }
    
    protected void processMouseMotionEvent(MouseEvent event)
    {
        if (event.getID() == MouseEvent.MOUSE_DRAGGED)
        {
            if (event.getSource() instanceof NomadClassicKnob) {
                NomadClassicKnob control = (NomadClassicKnob) event.getSource();
                if (SwingUtilities.isLeftMouseButton(event)) {
                    KnobMetrics metrics = 
                        (control.graphics = NomadClassicKnob.paintManager.obtainGraphics(control, control.graphics)).getMetrics();
                    
                    control.setValue(   
                        control.getMinValue()
                        + (int) (metrics.calcRangeFactor(event.getPoint())
                        * control.getRange())   
                    );
                }               
            }
        }
        super.processMouseMotionEvent(event);
    }
    
}
