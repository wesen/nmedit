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
 * Created on Dec 1, 2006
 */
package net.sf.nmedit.jpatch;

/**
 * The exception class used in the JPatch API. 
 * 
 * @author Christian Schneider
 */
public class JPException extends Exception
{

    /**
     * @see Exception#Exception()
     */
    public JPException()
    {
        super();
    }

    /**
     * @see Exception#Exception(java.lang.String)
     */
    public JPException( String message )
    {
        super( message );
    }

    /**
     * @see Exception#Exception(java.lang.String, java.lang.Throwable)
     */
    public JPException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * @see Exception#Exception(java.lang.Throwable)
     */
    public JPException( Throwable cause )
    {
        super( cause );
    }

}
