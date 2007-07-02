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

import javax.swing.SwingConstants;

import net.sf.nmedit.jtheme.JTContext;

public class JTSlider extends JTControl
{

    /**
     * 
     */
    private static final long serialVersionUID = 2371454335821522108L;
    public static final String uiClassID = "slider";
    private int orientation = SwingConstants.HORIZONTAL;
    
    public JTSlider(JTContext context)
    {
        super(context);
    }

    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public int getOrientation()
    {
        return orientation;
    }

    public void setOrientation(int orientation)
    {
        if (this.orientation != orientation)
        {
            this.orientation = orientation;
            // revalidate();  TODO how to notify ????
            repaint();
        }
    }
    
}
