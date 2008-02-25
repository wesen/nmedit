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
 * Created on Dec 12, 2006
 */
package net.sf.nmedit.jpatch;


public class ImageSource// implements Serializable
{

    private String src;
    private int width;
    private int height;

    public ImageSource(String src, int width, int height)
    {
        this.src = src;
        this.width = width;
        this.height = height;
    }

    public String getSource()
    {
        return src;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }

    public int hashCode()
    {
        return src.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof ImageSource))) return false;
        return src.equals(((ImageSource) o).src);
    }

    public String toString()
    {
        return getClass().getName()+"[source="+src+",width="+width+",height="+height+"]";
    }
    
}
