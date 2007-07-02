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

import java.awt.Graphics;

import javax.swing.border.Border;

import net.sf.nmedit.jtheme.JTContext;

public class JTKnob extends JTControl
{
    /**
     * 
     */
    private static final long serialVersionUID = 7632384382836698474L;
    public static final String uiClassID = "knob";

    public JTKnob(JTContext context)
    {
        super(context);
    }
    
    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public void setBorder(Border border)
    {
        if (border != null)
            throw new UnsupportedOperationException("border not supported");
    }
    
    protected void paintBorder(Graphics g)
    {
        // no border - nothing to paint
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        int size = Math.min(width, height);
        super.setBounds(x, y, size, size);
    }
    
}
