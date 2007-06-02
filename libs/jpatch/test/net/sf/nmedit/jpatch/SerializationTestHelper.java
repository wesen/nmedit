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
package net.sf.nmedit.jpatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationTestHelper
{

    public boolean testSerialization(Object v1) throws Exception
    {
        Object v2 ;
        File tempFile = File.createTempFile("junit.test", "serialdata");
        try
        {
            // write object
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
            out.writeObject(v1);
            out.flush();
            out.close();
            // read object
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(tempFile));
            v2 = in.readObject();
            in.close();
        }
        finally
        {
            tempFile.delete();
        }
        
        return compareEquals(v1, v2);
        
    }

    protected boolean compareEquals(Object v1, Object v2)
    {
        return v1.equals(v2);
    }
    
}
