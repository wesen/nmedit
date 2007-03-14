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
package net.sf.nmedit.jtheme.store.helpers;

import java.io.IOException;
import java.io.OutputStream;

public class ByteBuffer extends OutputStream
{

    private byte[] bytes = new byte[1000];
    private int size = 0;
    
    @Override
    public void write(int b) throws IOException
    {
        if (size>=bytes.length)
        {
            byte[] nbytes = new byte[bytes.length*2];
            System.arraycopy(bytes, 0, nbytes, 0, size);
            bytes = nbytes;
        }
        bytes[size++] = (byte) (b&0xFF);
    }
    
    public byte[] getBytes()
    {
        byte[] copy = new byte[size];
        System.arraycopy(bytes, 0, copy, 0, size);
        return copy;
    }

}
