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
 * Created on Apr 6, 2006
 */
package net.sf.nmedit.patch.transcoder;

import java.io.PrintWriter;

import net.sf.nmedit.patch.Format;
import net.sf.nmedit.patch.PatchBuilder;
import net.sf.nmedit.patch.Record;

public class StreamEncoding implements PatchBuilder
{
    
    private String nl;
    private PrintWriter writer;

    public StreamEncoding(PrintWriter writer)
    {
        this.nl = "\r\n";
        this.writer = writer;
    }

    public StreamEncoding()
    {
        this(null);
    }

    public void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }
    
    public PrintWriter getWriter()
    {
        return writer;
    }
    
    public void setNewLineString(String nl)
    {
        if (nl == null)
            throw new NullPointerException();
        this.nl = nl;
    }
    
    public String getNewLineString()
    {
        return nl;
    }
    
    public void beginSection( int ID ) 
    {
        writer.print("["+Format.getSectionName(ID)+"]"+nl);
    }

    public void endSection( int ID )
    {
        writer.print("[/"+Format.getSectionName(ID)+"]"+nl);
    }

    public void record( Record r )
    {
        for (int i=0;i<r.getValueCount();i++)
        {
            writer.print(r.getValue(i)+" ");
        }
        writer.print(r.getString());
        writer.print(nl);
    }
    
}
