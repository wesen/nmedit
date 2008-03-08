/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jtheme.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.JTCableManager;

public class JTLayerRoot extends JTBaseComponent
{
	private boolean ignoreMouseEvents = true;
	
    /**
     * 
     */
    private static final long serialVersionUID = 5019537573216055625L;
    public JTLayerRoot(JTContext context)
    {
        super(context);
        setOpaque(false);
        setJTFlag(FLAG_INVALIDATE, true);
        setJTFlag(FLAG_VALIDATE, true);
        setJTFlag(FLAG_REVALIDATE, true);
        setJTFlag(FLAG_VALIDATE_TREE, true);
    }
    
    public boolean contains(int x, int y)
    {
        // ensure this component gets no mouse events or similar events
    	if (ignoreMouseEvents)
    		return false;
    	else
    		return super.contains(x, y);
    }
    
    private JTCableManager cableManager;
    protected void setCableManager(JTCableManager cableManager)
    {
        JTCableManager oldManager = this.cableManager;
        if (oldManager != cableManager)
        {
            if (oldManager != null)
            {
                oldManager.setOwner(null);
                oldManager.setView(null);
            }
            this.cableManager = cableManager;
            if (cableManager != null)
            {
                cableManager.setOwner(this);
                cableManager.setView(this);
            }
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        paintCables(g);
    }
    
    protected void paintCables(Graphics g)
    {
        if (cableManager != null)
        {
            Graphics gs = g.create();
            try
            {
                cableManager.paintCables((Graphics2D) gs);
            }
            finally
            {
                gs.dispose();
            }
        }   
    }
    
    
    public void setSize(Dimension r) {
    	super.setSize(r);
    }
    
    public void ignoreMouseEvents() {
    	ignoreMouseEvents = true;
    }
    
    public void captureMouseEvents() {
    	ignoreMouseEvents = false;
    }
}
