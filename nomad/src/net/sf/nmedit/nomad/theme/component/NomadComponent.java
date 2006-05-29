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
 * Created on Jan 4, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.theme.property.ComponentLocationProperty;
import net.sf.nmedit.nomad.theme.property.ComponentSizeProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;


/**
 * @author Christian Schneider
 * TODO check opacity - disable where appropriate 
 * TODO property support for im- and export
 */
public class NomadComponent extends JComponent {
	
	private boolean flagHasDynamicOverlay = false;
	private Module module = null;
	private boolean sizePropertyEnabled=true;
    private int defaultWidth  = -1;
    private int defaultHeight = -1;

    //static int cnt = 0;
    
	public NomadComponent() 
    {
		setOpaque(false);
		setDoubleBuffered(false);
        /*
        cnt ++;
        if (cnt%100 == 0)
            System.out.println(cnt);*/
	}
    
	public void setDefaultSize(int w, int h) {
        defaultWidth = w;
        defaultHeight = h;
	}

	public void setDefaultSize(Dimension size) {
		setDefaultSize(size.width, size.height);
	}
	
	public Dimension getDefaultSize() {
		return new Dimension(defaultWidth, defaultHeight);
	}
/*
    public void paintBorder(Graphics g) {
    	// do not paint border here
    }

    public void paintNomadBorder(Graphics2D g2) {
    	// but we still want to be able to paint the border
		super.paintBorder(g2);
    }*/
    
    public void setSizePropertyEnabled(boolean enable) {
    	sizePropertyEnabled = enable;
    }
    
    public boolean isSizePropertyEnabled() {
    	return sizePropertyEnabled;
    }
    
	public void registerProperties(PropertySet set) {
		set.add(new ComponentLocationProperty());
		if (sizePropertyEnabled)
			set.add(new ComponentSizeProperty());
	}
	
	public void paintDecoration(Graphics2D g2) 
    {
        super.paintComponent(g2);
        /*
		if (isOpaque() && getBackground()!=null) {
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(),getHeight());
			//paintNomadBorder(g2);
		}
        */
	}

	public void paintDynamicOverlay(Graphics2D g2) {
		// no dynamic painting here
	}
	
	public boolean hasDynamicOverlay() {
		return flagHasDynamicOverlay; // default
	}
	
	public void setDynamicOverlay(boolean enable) {
		this.flagHasDynamicOverlay = enable;
	}
/*
	public void deleteAlternativeBackground() {
		alternativeBackground.dispose();
	}*/

    private boolean dirtyDisplay = true;
    
    public boolean isDisplayDirty()
    {
        return dirtyDisplay;
    }
    
    public void repaint()
    {
        dirtyDisplay = true;
        super.repaint();
    }

	public NomadComponent getNomadComponent(int i) {
		return (NomadComponent) getComponent(i);
	}

    public void paint(Graphics g)
    {
        try
        {
            super.paint(g);
        }
        finally
        {
            dirtyDisplay = false;
        }
    }

	public void paintComponent(Graphics g) {

        int w = getWidth(); 
        int h = getHeight();
		if (w<=0 || h <=0) return;

        Graphics2D g2 = (Graphics2D) g;
        
        paintDecoration(g2);
        if (hasDynamicOverlay()) 
        {
            paintDynamicOverlay(g2);
        }
	}

	public Module getModule() {
		if ((module == null) && (getParent() instanceof ModuleUI)) {
			module = ((ModuleUI)getParent()).getModule();
		}

		return module;
	}

	public void link(Module module) {
		//
	}
	
	public void unlink() {
		//
	}
	
}
