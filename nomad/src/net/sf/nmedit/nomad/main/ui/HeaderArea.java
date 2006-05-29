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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;


public class HeaderArea extends NComponent
{
    
    public static Color SILVER_DARK = Color.decode("#C5C5C5");
    public static Color SILVER_BRIGHT = Color.decode("#F4F4F4"); //#E7E7E7

   // public final static Background defaultBackground =
   // BackgroundFactory.createGradientBackground(SILVER_DARK, SILVER_BRIGHT, GradientBackground.HORIZONTAL, 0.5f);
    
    public HeaderArea()
    {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createHorizontalStrut(4));
        //setBackgroundB(defaultBackground);
    }
/*
    public static Background getDefaultBackground()
    {
        return defaultBackground;
    }*/
    
    public void addSeparator()
    {
        add(new JSeparator(JSeparator.VERTICAL));
    }

    public void addHSpace()
    {
        HeaderSection filler = new HeaderSection(" ");
        filler.setPreferredSize(new Dimension(Short.MAX_VALUE, 0));
        add(filler);
        //add(Box.createGlue());
    }
    
}
