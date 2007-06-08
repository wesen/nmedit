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
package net.sf.nmedit.jtheme.image;

import java.awt.Dimension;
import java.awt.Image;


public class SVGStringRessource implements ImageResource
{
    
    private String svgData;
    private transient Image cachedImage;
    
    public SVGStringRessource(String svgData)
    {
        if (svgData == null)
            throw new NullPointerException();
        this.svgData = svgData;
    }

    public Image getImage(int width, int height)
    {
        
        if (cachedImage != null)
        {
            int iw = cachedImage.getWidth(null);
            int ih = cachedImage.getHeight(null);
            if (iw==width && ih == height)
                return cachedImage;
        }
        return cachedImage = SVGImageResource.renderSVGImage(svgData, width, height);
    }

    public int getType()
    {
        return SCALABLE_IMAGE;
    }

    public String getSource()
    {
        return null;
    }
    
    public int hashCode()
    {
        return svgData.hashCode();
    }
    
    public boolean equals(Object o)
    {
        return o == this;
    }

    public void flush()
    {
        cachedImage = null;
    }

    public ClassLoader getCustomClassLoader()
    {
        return null;
    }

    public Image getImage()
    {
        return getImage(-1, -1);
    }

    public Image getImage(Dimension preferredSize)
    {
        return getImage(preferredSize.width, preferredSize.height);
    }

    public ClassLoader getResourceClassLoader()
    {
        return getClass().getClassLoader();
    }
    
}
