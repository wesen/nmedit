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

import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Math2;
import net.sf.nmedit.jpatch.formatter.Formatter;
import net.sf.nmedit.jpatch.PParameter;

/**
 * @author Christian Schneider
 * @hidden
 */
public class DrumHz  implements Formatter
{

    public String getString( PParameter parameter, int value )
    {
        double aFloat = 20.0d * Math.pow(2.0, value/24.0d);

        if (aFloat<100)
            return ""+Math2.roundTo(aFloat, -1)+" Hz";
        
        else
            return ""+Math2.roundTo(aFloat, 0)+" Hz";
    }

}

