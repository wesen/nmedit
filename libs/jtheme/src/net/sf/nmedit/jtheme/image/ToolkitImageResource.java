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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ToolkitImageResource extends AbstractImageResource implements Serializable
{

    /**
     * serial version
     */
    private static final long serialVersionUID = 5082872382975908730L;

    private transient BufferedImage image;
    private transient boolean imageInitialized = false;

    public ToolkitImageResource(String srcURI, ClassLoader customClassLoader)
    {
        super(srcURI, customClassLoader);
    }
    
    public ToolkitImageResource(URL imageURL)
    {
        super(imageURL);
    }

    @Override
    public Image getImage(int width, int height)
    {
        if (!(imageInitialized))
        {
            imageInitialized = true;
            try
            {
                image = ImageIO.read(getEnsureResolvedURL());
            }
            catch (IOException e)
            {
                Log log = LogFactory.getLog(getClass());
                if (log.isErrorEnabled())
                    log.error("getImage() failed", e);
            }
        }
        return image;
    }

    @Override
    public int getType()
    {
        return RASTER_IMAGE;
    }
    
    public void flush()
    {
        super.flush();
        image = null;
        this.imageInitialized = false;
    }

    protected void initState()
    {
        super.initState();
        this.imageInitialized = false;
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
