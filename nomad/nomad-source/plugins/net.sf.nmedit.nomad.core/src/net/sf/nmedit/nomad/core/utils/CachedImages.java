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
 * Created on Nov 24, 2006
 */
package net.sf.nmedit.nomad.core.utils;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;


public class CachedImages extends Images
{
    
    private Map<String,Image> cache;
    private Images images;

    public CachedImages(Images images)
    {
        this.cache = new HashMap<String,Image>();
        this.images = images;
    }
    
    public Image getImage( String baseName )
    {
        Image img = cache.get(baseName);
        if (img == null)
        {
            img = images.getImage(baseName);
            cache.put(baseName, img);
        }
        return img;
    }

    public void flush()
    {
        cache.clear();
        images.flush();
    }

    public boolean isCaching()
    {
        return true;
    }
    
}
