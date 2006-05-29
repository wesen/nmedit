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
 * Created on May 1, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Image;

public class TiledBackground extends AbstractTiledBackground
{

    private Image image;
    private int iw;
    private int ih;

    public TiledBackground(Image image, int dx, int dy)
    {
        super(dx, dy);
        this.image = image;
        iw = image.getWidth(null);
        ih = image.getHeight(null);

        if (iw<=0||ih<=0)
        {
            // the image is not loaded yet - wait for it

            while ((iw = image.getWidth(null))<0) ;
            while ((ih = image.getHeight(null))<0) ;
            
            if (iw<=0||ih<=0)
            {
                throw new IllegalStateException("invalid image size: <=0");
            }   
        }
    }

    public Image getImage()
    {
        return image;
    }

    public int getWidth()
    {
        return iw;
    }

    public int getHeight()
    {
        return ih;
    }

}
