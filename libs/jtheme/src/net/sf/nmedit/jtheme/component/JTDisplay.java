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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.util.JThemeUtils;

public class JTDisplay extends JTComponent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2772033221761972736L;
    public static final String uiClassID = "DisplayUI";
    
    public JTDisplay(JTContext context)
    {
        super(context);
        setOpaque(true);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
    
    protected void processEvent(AWTEvent e)
    {
        Component parent = getParent();
        if (parent != null && e instanceof MouseEvent)
        {
            // retarget mouse events
            MouseEvent me = JThemeUtils.convertMouseEvent(this, (MouseEvent) e, parent);
            parent.dispatchEvent(me);
        }
        else
        {
            super.processEvent(e);
        }
    }

    public String getUIClassID()
    {
        return uiClassID;
    }
    
}
