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
 * Created on Nov 25, 2006
 */
package net.sf.nmedit.jtheme.component;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.util.JThemeUtils;

public class JTLabel extends JTComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = -4347651399715148385L;
    final public static String uiClassID = "LabelUI";
    private String text = "label";

    public JTLabel( JTContext context )
    {
        super( context );
        setOpaque(false);
        // capture mouse events for retargeting
        enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
    
    protected boolean retargetMouseEvent(MouseEvent e)
    {
        // retarget mouse events
       MouseEvent me = JThemeUtils.convertMouseEvent(this, (MouseEvent) e, getParent());
    	// Work around SwingUtilities bug that doesn't take into account extended modifiers (needed on mac)
       getParent().dispatchEvent(me);
        return true;
    }
    
    protected void processEvent(AWTEvent e)
    {
        Component parent = getParent();
        if (parent != null && e instanceof MouseEvent && retargetMouseEvent((MouseEvent)e))
        {
            // done
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

    public boolean isReducible()
    {
        return true;
    }
    
    public void setText(String text)
    {
        String oldText = this.text;
        
        if (text==null||oldText==null||(!text.equals(oldText)))
        {
            this.text = text;
            
            revalidate();
            repaint();
        }
    }
    
    public String getText()
    {
        return this.text;
    }

}
