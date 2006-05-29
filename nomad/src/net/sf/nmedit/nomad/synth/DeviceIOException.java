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
 * Created on May 16, 2006
 */
package net.sf.nmedit.nomad.synth;

import net.sf.nmedit.nomad.synth.SynthDevice;

public class DeviceIOException extends SynthException
{

    public DeviceIOException( SynthDevice device )
    {
        super( device );
    }

    public DeviceIOException( SynthDevice device, String message )
    {
        super( device, message );
    }

    public DeviceIOException( SynthDevice device, String message,
            Throwable cause )
    {
        super( device, message, cause );
    }

    public DeviceIOException( SynthDevice device, Throwable cause )
    {
        super( device, cause );
    }

    public void printStackTrace(){
        if (getCause()!=null)
            getCause().printStackTrace();
        
        super.printStackTrace();
    }
    
}
