/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jpdl;

import java.io.Serializable;
import java.io.IOException;

public class IntStream implements Serializable
{
    
    private static final long serialVersionUID = 1799889660028505749L;
    
    private transient int[] data;
    private int size;
    private int position;

    public IntStream(IntStream src)
    {
        size = src.getSize();
        position = 0;
        data = new int[src.getSize()];
        System.arraycopy(src.data, 0, data, 0, size);
    }
    
    public IntStream(int initialCapacity)
    {
        data = new int[initialCapacity];
        position = 0;
        size = 0;
    }

    public IntStream()
    {
        this(32);
    }
    
    private void ensureCapacity(int newCapacity)
    {
        if (newCapacity>data.length)
        {
            int[] a = new int[(newCapacity*3)/2+1];
            System.arraycopy(data, 0, a, 0, size);
            this.data = a;
        }
    }

    public void append(int data)
    {
        ensureCapacity(size+1);
        this.data[size++] = data;
    }

    public int getPosition()
    {
        return position;
    }
    
    public int getSize()
    {
        return size;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public void setSize(int size)
    {
        if (this.size > size)
            this.size = size;
    	if (position > size) 
    	    position = size;
    }

    public boolean isAvailable(int amount)
    {
        return position+amount <= size;
    }

    public int getInt()
    {
        return data[position++];
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException 
    {
        // write size, position
        out.defaultWriteObject();
        // write data
        for (int i=0;i<size;i++)
            out.writeInt(data[i]);
    }
     
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException 
    {
        // read size, position
        in.defaultReadObject();
        // read data
        data = new int[size];
        for (int i=0;i<size;i++)
            data[i] = in.readInt();
    }
    
    public String toString()
    {
        return getClass().getName()+"[size="+size+",position="+position
        +",capacity="+data.length+"]";
    }
    
}
