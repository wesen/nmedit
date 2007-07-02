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

public interface ImageResource
{

    public static final int UNKNOWN_IMAGE_TYPE = -1;
    public static final int RASTER_IMAGE = 0;
    public static final int SCALABLE_IMAGE = 1;

    ClassLoader getCustomClassLoader();
    
    ClassLoader getResourceClassLoader();
    
    Image getImage();
    
    Image getImage(Dimension preferredSize);
    
    Image getImage(int width, int height);

    int getType();

    Object getSource();
    
    void flush();

    void setCustomClassLoader(ClassLoader loader);

    void setImageCache(ImageCache cache);
    
    ImageCache getImageCache();
    
}
