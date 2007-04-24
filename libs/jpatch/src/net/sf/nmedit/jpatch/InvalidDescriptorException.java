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
 * Created on Dec 2, 2006
 */
package net.sf.nmedit.jpatch;

/** 
 * An exception that indicates that a descriptor was invalid (or incompatible).
 */
public class InvalidDescriptorException extends JPRuntimeException
{

    private Descriptor descriptor = null;
    
    public InvalidDescriptorException(Descriptor descriptor)
    {
        super("invalid descriptor "+descriptor);
    }
    
    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public InvalidDescriptorException()
    {
        super();
    }

    public InvalidDescriptorException( String message )
    {
        super( message );
    }

    public InvalidDescriptorException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidDescriptorException( Throwable cause )
    {
        super( cause );
    }

}
