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
 * Created on Sep 12, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopy
{

    private File source;
    private File target;

    public FileCopy(File source, File target)
    {
        this.source = source;
        this.target = target;
    }

    public void copy()
    {
        validate();
        try
        {
            copyFiles();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void copyFiles() throws IOException
    {
        FileChannel sourceChannel = 
            new FileInputStream(source).getChannel();
        try
        {
            FileChannel destinationChannel = 
                new FileOutputStream(target).getChannel();
            try
            {
                sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
            }
            finally
            {
                destinationChannel.close();
            }
        }
        finally
        {
            sourceChannel.close();
        }
    }
    
    public void validate()
    {
        if (!source.exists())
            throw new RuntimeException("source does not exist: "+source);
        if (target.exists())
            throw new RuntimeException("target already exists: "+target);
    }
    
}
