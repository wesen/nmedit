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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.sf.nmedit.nmutils.graphics.GraphicsToolkit;

public class ImageCache implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -3611843761321258122L;

    private static boolean DEBUG = false;

    private transient Map<Key, Image> cache;
    
    public ImageCache()
    {
        initialize();
    }
    
    private void initialize()
    {
        cache = new HashMap<Key, Image>();
    }

    private Key key(Object key, int width, int height)
    {
        if (width<0 || height<0)
            width = height = -1;
        
        return new Key(key, width, height);
    }
    
    public Image getImage(Object key, int width, int height)
    {
        Image image = cache.get(key(key, width, height));
        if (DEBUG)
        {
            System.out.println(getClass()+" "+(image==null?"miss":"hit"));
        }
        return image;
    }
    
    public void putImage(Object key, int width, int height, Image image)
    {
        cache.put(key(key, width, height), image);
    }
    
    public void remove(Object key, int width, int height)
    {
        cache.remove(key(key, width, height));
    }
    
    public void removeImage(Image image)
    {
        for (Entry<Key, Image> e: cache.entrySet())
        {
            Image i = e.getValue();
            if (i == image || (i!=null && i.equals(image)))
            {
                cache.remove(e.getKey());
                break;
            }
        }
    }

    public void readCacheFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        try
        {
            readData(in);
        }
        finally
        {
            in.close();
        }
    }
    
    public void writeCacheFile(File file) throws FileNotFoundException, IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        try
        {
            writeData(out);
        }
        finally
        {
            out.flush();
            out.close();
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        writeData(out);
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        initialize();
        readData(in);
    }

    private void writeData(ObjectOutputStream out) throws IOException
    {
        int size = cache.size();
        out.writeInt(size);
        
        for (Entry<Key, Image> e: cache.entrySet())
        {
            Key k = e.getKey();
            out.writeObject(k);
            byte[] data = toByteArray(e.getValue(), k.width, k.height);
            out.writeObject(data);
        }
    }

    private void readData(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        
        for (int i=0;i<size;i++)
        {
            Key key = (Key) in.readObject();
            byte[] data = (byte[]) in.readObject();
            Image image = ImageIO.read(new ByteArrayInputStream(data));
            cache.put(key, image);
        }
    }

    private byte[] toByteArray(Image src, int width, int height) throws IOException
    {
        RenderedImage ri = null;
        
        if (src instanceof RenderedImage)
        {
            ri = (RenderedImage) src;
        }
        else
        {
            boolean opaque = false;
            if (src instanceof Transparency)
                opaque = ((Transparency)src).getTransparency() == Transparency.OPAQUE;
         
            
            if (width<0 || height<0)
            {
                Dimension size = GraphicsToolkit.getImageSize(src);
                width = size.width;
                height = size.height;
                // TODO size <0 ???
            }
            
            BufferedImage bi = new BufferedImage(width, height, opaque ? BufferedImage.TYPE_INT_BGR : BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g = bi.createGraphics();
            try
            {
                g.drawImage(src, 0, 0, null);
            }
            finally
            {
                g.dispose();
            }        
            ri = bi;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(width*height);
        ImageIO.write(ri, "png", out);
        return out.toByteArray();
    }

    private static class Key implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1081288232969013401L;
        private Object key;
        private int width;
        private int height;
        private transient int hashCode = 0;
        
        public Key(Object key, int width, int height)
        {
            this.key = key;
            this.width = width;
            this.height = height;
        }
        
        public int hashCode()
        {
            if (hashCode == 0)
            {
                hashCode = key.hashCode() + (width<<10)+height;
            }
            return hashCode;
        }

        public boolean equals(Object o)
        {
            if (o == this) return true;
            if (o == null || (!(o instanceof Key))) return false;
            Key k = (Key) o;
            return key.equals(k.key) && width == k.width && height == k.height;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException
        {
            out.defaultWriteObject();
        }
        
        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
        {
            in.defaultReadObject();
            hashCode = 0;
        }
        
    }
 
}
