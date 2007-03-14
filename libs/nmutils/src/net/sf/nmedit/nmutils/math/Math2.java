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
 * Created on Jun 20, 2006
 */
package net.sf.nmedit.nmutils.math;

public class Math2
{

    /**
     * Rounds d until digit '-to'. For example d=123, to=2 returns 120.
     * If d=123.456 and to=-2 then 123.45 is returned.
     * 
     * @param d Value to round
     * @param to decimal position
     * @return rounded value of d
     */
    public static double roundTo(double d, int to) {
        if (to == 0)
            return Math.round(d);

        double norm = Math.pow(10.0, -to);
        return Math.round(d*norm) / norm;
    }

    /**
     * Returns the value of d as a String. If d is a natural number,
     * it is returned without decimal point.
     * @param d value that should be converted to a string representation
     * @return d as string
     */
    public static String doubleToStr(double d) {
        double truncated = (int) d;
        if (d==truncated)
            return Integer.toString((int)d);
        else
            return Double.toString(d);
    }

}
