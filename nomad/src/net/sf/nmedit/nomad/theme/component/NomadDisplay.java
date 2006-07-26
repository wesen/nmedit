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
 * Created on Jul 26, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class NomadDisplay extends NomadComponent
{

    private final static Color defaultBGColor = Color.decode("#008080");
    private final static Color defaultFGColor = Color.decode("#00FF00");

    private final static Border defaultBorder   =
        BorderFactory.createCompoundBorder(
        NomadBorderFactory.createNordEditor311Border(),
        BorderFactory.createEmptyBorder(2,2,2,2));

    public NomadDisplay()
    {
        setOpaque(true);
        setBackground(defaultBGColor);
        setForeground(defaultFGColor);
        //setBorder(defaultBorder);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // background
        if (isOpaque() && getBackground()!=null)
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
}
