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
package net.sf.nmedit.nomad.patch.builder;

import java.io.PrintWriter;

import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.misc.Record;

/**
 * Builder for the {@link java.io.PrintWriter} targets. 
 * 
 * After building the target you should not forget to call {@link java.io.PrintWriter#flush()}.
 * 
 * @author Christian Schneider
 */
public class StreamBuilder implements PatchBuilder
{
    
    /**
     * String containing the line termination character sequence.
     * This should be "\r\n" to avoid conflicts with the official editor.
     */
    private String nl;
    
    /**
     * The {@link PrintWriter} used to write the data. 
     */
    private PrintWriter writer;

    /**
     * Creates a builder using a print writer as target.
     * @param writer target
     */
    public StreamBuilder(PrintWriter writer)
    {
        this.nl = "\r\n";
        this.writer = writer;
    }

    /**
     * Creates a builder with no target set.
     * The writer can be set using {@link #setWriter(PrintWriter)}
     */
    public StreamBuilder()
    {
        this((PrintWriter)null);
    }

    /**
     * Sets the target.
     * @param writer target
     */
    public void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }
    
    /**
     * Returns the target
     * @return the target
     */
    public PrintWriter getWriter()
    {
        return writer;
    }
    
    /**
     * Sets the line termination string
     * @param nl termination string
     */
    public void setNewLineString(String nl)
    {
        if (nl == null)
            throw new NullPointerException();
        this.nl = nl;
    }
    
    /**
     * Returns the line termination string.
     * @return the line termination string
     */
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
