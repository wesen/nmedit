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


public class InvalidDescriptorException extends PRuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -5477077620719270598L;
    private PDescriptor descriptor;
    
    public InvalidDescriptorException(PDescriptor descriptor, String message)
    {
        super(message);
        this.descriptor = descriptor;
    }
    
    public PDescriptor getInvalidDescriptor()
    {
        return descriptor;
    }
    
}
