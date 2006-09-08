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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.property.ComponentLocationProperty;
import net.sf.nmedit.nomad.theme.property.ComponentSizeProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;


/**
 * @author Christian Schneider
 * TODO check opacity - disable where appropriate 
 * TODO property support for im- and export
 */
public class NomadComponent extends JComponent {
	
	private Module module = null;
	private boolean sizePropertyEnabled=true;
    private int defaultWidth  = -1;
    private int defaultHeight = -1;
    private HashMap<String, DParameter> parameters = null;

    //static int cnt = 0;

//  Overridden for performance reasons.
    public void validate() {
    }

    public void revalidate() {
    }
    protected void firePropertyChange(String propertyName, Object oldValue,
            Object newValue) {
    }

    public void firePropertyChange(String propertyName, byte oldValue,
            byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue,
            char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue,
            short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue,
            int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue,
            long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue,
            float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue,
            double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue,
            boolean newValue) {
    }

	public NomadComponent() 
    {
		setOpaque(true);
		setDoubleBuffered(true);
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

    public void repaint()
    {
      //  dirtyDisplay = true;
        Component c = getParent();
        if (c instanceof ModuleUI)
        {
            ((ModuleUI)c).registerDirtyComponent(this);
        }
        
        super.repaint();
    }

	public NomadComponent getNomadComponent(int i) {
		return (NomadComponent) getComponent(i);
	}

	public void paintComponent(Graphics g) 
    {/*
        int w = getWidth(); 
        int h = getHeight();
		if (w<=0 || h <=0) return;*/
        super.paintComponent(g);
	}

	public Module getModule() {
		if ((module == null) && (getParent() instanceof ModuleUI)) 
        {
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

    public final DParameter getParameterInfo( String name )
    {
        return parameters == null ? null: parameters.get(name);
    }

    public void setParameterInfo( String name, DParameter p )
    {
        if (p==null)
        {
            if (parameters!=null)
            {
                parameters.remove(name);
                if (parameters.size()==0)
                    parameters = null;
            }
        }
        else {
            if (parameters==null)
                parameters = new HashMap<String, DParameter>();
            parameters.put(name, p);
        }
    }
	
}
