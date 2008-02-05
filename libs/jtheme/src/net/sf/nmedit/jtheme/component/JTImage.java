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
package net.sf.nmedit.jtheme.component;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jtheme.JTContext;

public class JTImage extends JTComponent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1051395748578312099L;
    public static final String uiClassID = "imageui";
    private ImageIcon icon;

    public JTImage(JTContext context)
    {
        super(context);
        setOpaque(false);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    protected boolean retargetMouseEvent(MouseEvent e)
    {
        // retarget mouse events
        MouseEvent me = SwingUtilities.convertMouseEvent(this, (MouseEvent) e, getParent());
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
    
    public ImageIcon getIcon()
    {
        return icon;
    }
    
    public void setIcon(ImageIcon icon)
    {
        if (this.icon != icon)
        {
            this.icon = icon;
            
            if (icon == null)
                setOpaque(false);
            else
                setOpacity(icon.getImage());
        }
    }
    
    private void setOpacity(Image image)
    {
        if (image != null && image instanceof Transparency)
        {
            Transparency t = (Transparency) image;
            
            if (t.getTransparency() == Transparency.OPAQUE)
            {
                setOpaque(true);
                return;
            }
        }
        setOpaque(false);
    }

    public boolean isReducible()
    {
        return true;
    }

}

