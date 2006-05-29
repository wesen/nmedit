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

import java.awt.Image;

import net.sf.nmedit.nomad.util.cache.Cache;
import net.sf.nmedit.nomad.util.cache.ImageRecoveringOp;
import net.sf.nmedit.nomad.util.cache.RecoverableCache;
import net.sf.nmedit.nomad.util.cache.WeakCache;

public class RenderableTiledBackground extends AbstractTiledBackground
{
    
    private RecoverableCache<Image> cache;
    private ImageRecoveringOp recoveringOp; 

    public RenderableTiledBackground( int dx, int dy, ImageRecoveringOp<Image> recoveringOp )
    {
        this(dx, dy, new WeakCache<Image>(), recoveringOp);
    }
    
    public RenderableTiledBackground( int dx, int dy, Cache<Image> cache, ImageRecoveringOp<Image> recoveringOp )
    {
        super( dx, dy );
        cache = new RecoverableCache<Image>(cache, recoveringOp);
        this.recoveringOp = recoveringOp;
    }
    
    public ImageRecoveringOp getRecoveringOp()
    {
        return recoveringOp;
    }

    public Image getImage()
    {
        return cache.get();
    }
    
    public int getWidth()
    {
        return getRecoveringOp().getWidth();
    }
    
    public int getHeight()
    {
        return getRecoveringOp().getHeight();
    }
    
    public int getTransparency()
    {
        return getRecoveringOp().getTransparency();
    }

}
