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
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public abstract class AbstractImageResource implements ImageResource, Serializable
{

    private transient ClassLoader customClassLoader;
    private transient String srcURI;
    private transient URL srcURL;
    private transient URL resolvedURL;
    private transient boolean urlResolved = false;
    private transient ImageCache cache;
    private transient int hashCode = 0;
    
    protected AbstractImageResource()
    {
        super();
        initState();
    }

    public ImageCache getImageCache()
    {
        return cache;
    }
    
    public void setImageCache(ImageCache cache)
    {
        this.cache = cache;
    }
    
    public void setCustomClassLoader(ClassLoader loader)
    {
        if (this.customClassLoader != loader)
        {
            this.customClassLoader = loader;
            initState();
        }
    }
    
    public AbstractImageResource(URL imageURL)
    {
        this.srcURL = imageURL;
        initState();
    }
    
    public AbstractImageResource(String srcURI)
    {
        this(srcURI, null);
    }
    
    public AbstractImageResource(String srcURI, ClassLoader customClassLoader)
    {
        this.srcURI = srcURI;
        this.customClassLoader = customClassLoader;
        initState();
    }
    
    protected void initState()
    {
        this.resolvedURL = null;
        this.urlResolved = false;
    }

    public URL getResolvedURL()
    {
        if (!(urlResolved))
        {
            urlResolved = true;
            if (srcURI != null)
            {
                ClassLoader loader = getResourceClassLoader();
                resolvedURL = loader.getResource(srcURI);
            }
            else
            {
                resolvedURL = srcURL;
            }
        }
        return resolvedURL;
    }

    public ClassLoader getCustomClassLoader()
    {
        return customClassLoader;
    }
    
    public ClassLoader getResourceClassLoader()
    {
        return customClassLoader != null ? customClassLoader : getClass().getClassLoader();
    }
    
    public Image getImage()
    {
        return getImage(-1, -1);
    }
    
    public Image getImage(Dimension preferredSize)
    {
        return getImage(preferredSize.width, preferredSize.height);
    }
    
    protected URL getEnsureResolvedURL() throws IOException
    {
        URL url = getResolvedURL();
        if (url == null)
            throw new IOException("could not find resource "+(srcURI!=null?srcURI:srcURL));
        return url;
    }
    
    protected InputStream createInputStream() throws IOException
    {
        return getEnsureResolvedURL().openStream();
    }
    
    public abstract Image getImage(int width, int height);

    public abstract int getType();

    public String getSource()
    {
        return String.valueOf(getResolvedURL());
    }
    
    public void flush()
    {
        resolvedURL = null;
    }
    
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        Object src = srcURI != null ? srcURI : srcURL;
        out.writeObject(src);
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        Object src = in.readObject();
        if (src instanceof String)
            this.srcURI = (String) src;
        else if (src instanceof URL)
            this.srcURL = (URL) src;
        hashCode = 0; // reset hashCode
        initState();
    }
    
    public String toString()
    {
        return getClass().getName()+"[src="+(srcURI!=null?srcURI:srcURL)+",resolved="+getResolvedURL()+",loader="+getResourceClassLoader()+"]";
    }

    public int hashCode()
    {
        if (hashCode<=0)
        {
            URL src = getResolvedURL();
            hashCode = src == null ? 0 : src.toString().hashCode();
        }
        return hashCode;
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof AbstractImageResource))) return false;
        
        AbstractImageResource b = (AbstractImageResource) o;
        
        if (eq(customClassLoader, b.customClassLoader)
        && (eq(srcURI, b.srcURI)||eq(srcURL, b.srcURL)))
            return true;

        if (eq(getResolvedURL(), b.getResolvedURL())) return true;
        return false;
    }
    
    private boolean eq(Object a, Object b)
    {
        return a==b || (a!=null && a.equals(b));
    }
    
}
