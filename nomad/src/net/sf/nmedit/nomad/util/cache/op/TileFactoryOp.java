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
package net.sf.nmedit.nomad.util.cache.op;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class TileFactoryOp implements Reusable<BufferedImage>, Transparency
{

    private int tw ;
    private int th ;
    private int transparency;

    public TileFactoryOp(Dimension tileSize, int transparency)
    {
        this(tileSize.width, tileSize.height, transparency);
    }
    
    public TileFactoryOp(int tw, int th, int transparency)
    {
        this.tw = tw;
        this.th = th;
        this.transparency = transparency;
    }
    
    public int getWidth()
    {
        return tw;
    }

    public int getHeight()
    {
        return th;
    }

    public boolean isValid( BufferedImage t )
    {
        return (t.getWidth()==tw) && (t.getHeight()==th) && (t.getTransparency()==getTransparency());
    }
    
    public void reuse( BufferedImage t )
    {
        if (getTransparency()!=OPAQUE)
        {
            Graphics2D g2 = t.createGraphics();
            GraphicsToolkit.clearRegion(g2, 0, 0, tw, th);
            g2.dispose();
        }
    }

    public BufferedImage recover()
    {
        return GraphicsToolkit.createCompatibleBuffer(getWidth(), getHeight(), getTransparency());
    }

    public int getTransparency()
    {
        return transparency;
    }

}
