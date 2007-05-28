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
package net.sf.nmedit.jpatch.clavia.nordmodular.formatter;

import net.sf.nmedit.jpatch.formatter.Formatter;
import net.sf.nmedit.jpatch.PParameter;

/**
 * @author Christian Schneider
 * @hidden
 */
public class Note  implements Formatter
{
	
	private final static String[] NOTES = 
		new String[] {"C","C","D","D","E","F","F","G","G","A","A","B"};
	private final static String[] SHARPS = 
		new String[] {" ","#"," ","#"," "," ","#"," ","#"," ","#"," "};

    public String getString( PParameter parameter, int value )
    {
        int v12 = value % 12;
        return NOTES[v12]+((value/12) -1)+SHARPS[v12];
    }

}

