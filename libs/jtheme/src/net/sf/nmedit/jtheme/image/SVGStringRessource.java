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
import java.io.IOException;
import java.io.Serializable;


public class SVGStringRessource implements ImageResource, Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -7915560452708298768L;
    private String svgData;
    private transient ImageCache cache;
    
    public SVGStringRessource(String svgData)
    {
        if (svgData == null)
            throw new NullPointerException();
        this.svgData = svgData;
    }

    public ImageCache getImageCache()
    {
        return cache;
    }
    
    public void setImageCache(ImageCache cache)
    {
        this.cache = cache;
    }
    
    public Image getImage(int width, int height)
    {
        return SVGImageResource.getSVGImage(getImageCache(), svgData, width, height);
    }

    public void setCustomClassLoader(ClassLoader loader)
    {
        // we do not use a custom classloader - ignore
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
        // no op
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

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    
}
