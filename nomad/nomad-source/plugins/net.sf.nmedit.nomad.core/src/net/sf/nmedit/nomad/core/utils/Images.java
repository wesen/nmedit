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
import java.util.MissingResourceException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class Images
{

    public abstract Image getImage(String baseName);

    /**
     * Removes cached images if there are any. 
     * (But does not flush them images themselves using {@link Image#flush()})).
     * 
     * This method always leaves the cache in a state such that it can 
     * be reconstructed.
     */
    public abstract void flush();
    
    /**
     * Returns true when the underlying implementation caches images.
     * @return
     */
    public abstract boolean isCaching();

    /**
     * @param baseName
     * @throws MissingResourceException
     */
    protected final void resourceNotFound(String baseName)
        throws MissingResourceException
    {
        throw new MissingResourceException("Resource not found", Image.class.getName(), baseName);
    }

    public Images getSubset(String baseNamePrefix)
    {
        return new ImagesSubset(this, baseNamePrefix);
    }
    
    public Icon getIcon(String baseName)
    {
        return getImageIcon(baseName);
    }
    
    public ImageIcon getImageIcon(String baseName)
    {
        return new ImageIcon(getImage(baseName));
    }
    
    private static class ImagesSubset extends Images
    {

        private String baseNamePrefix;
        private Images images;

        public ImagesSubset(Images images, String baseNamePrefix)
        {
            this.images = images;
            this.baseNamePrefix = baseNamePrefix;
        }

        public Image getImage( String baseName )
        {
            return images.getImage(baseNamePrefix + baseName);
        }

        public void flush()
        {
            images.flush();
        }

        public boolean isCaching()
        {
            return images.isCaching();
        }

    }
    
}
