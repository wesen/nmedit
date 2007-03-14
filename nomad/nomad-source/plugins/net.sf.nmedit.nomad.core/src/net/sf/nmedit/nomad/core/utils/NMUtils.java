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
 * Created on Nov 24, 2006
 */
package net.sf.nmedit.nomad.core.utils;

import java.io.File;

public final class NMUtils
{

    private NMUtils()
    {
        super();
    }

    /**
     * Ensures that the given pathname ends with the path separator character
     * of this operating system.
     * 
     * @param pathName
     * @return
     */
    public final static String ensureEndsWithPathSeparatorChar(String pathName)
    {
        if (pathName.length()==0)
            return File.pathSeparator;
        else if (pathName.charAt(pathName.length()-1)==File.pathSeparatorChar)
            return pathName;
        else
            return pathName+File.pathSeparatorChar;
    }

    /**
     * Ensures that the given pathname does not end with the path separator character
     * of this operating system.
     * 
     * @param pathName
     * @return
     */
    public final static String ensureEndsNotWithPathSeparatorChar(String pathName)
    {
        if (pathName.length()==0)
            return pathName;
        else if (pathName.charAt(pathName.length()-1)!=File.pathSeparatorChar)
            return pathName;
        else
            return pathName.substring(0, pathName.length()-1);
    }
    
}
