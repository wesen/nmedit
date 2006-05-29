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
 * Created on May 3, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;

import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public abstract class AbstractTiledBackground implements ImageBackground
{

    private int dx, dy;
    
    public AbstractTiledBackground(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx()
    {
        return dx;
    }
    
    public int getDy()
    {
        return dy;
    }
    
    public void paintBackground( Component c, Graphics g, int x, int y, int width, int height )
    {
        GraphicsToolkit.paintTiles(g, getImage(), getWidth(), getHeight(), dx, dy, x, y, width, height);
    }

    /**
     * Overwrite for better implementation
     */
    public int getTransparency()
    {
        Image i = getImage();
        if (i instanceof Transparency)
            return ((Transparency) i).getTransparency();
        else
            return TRANSLUCENT;
    }
    
    public int getWidth()
    {
        return getImage().getWidth(null);
    }
    
    public int getHeight()
    {
        return getImage().getHeight(null);
    }
    
}
