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
 * Created on Jun 27, 2006
 */
package net.sf.nmedit.nomad.main.ui.fix;

import java.awt.Color;

public interface TreeStripes
{

    public Color getColorAt(int row);

    public Color getEmptyRowColorAt( int i );
    
    public static class AlternatingStripes implements TreeStripes
    {
        
        private final Color row0;
        private final Color row1;
        public final static Color SOFT_BLUE = Color.decode("#EFF3FE");
        
        public static TreeStripes createSoftBlueStripes()
        {
            return new AlternatingStripes(SOFT_BLUE, Color.WHITE);
        }
        
        public AlternatingStripes(Color row0, Color row1)
        {
            this.row0 = row0;
            this.row1 = row1;
        }

        public Color getColorAt( int row )
        {
            return row % 2 == 0 ? row0 : row1;
        }

        public Color getEmptyRowColorAt( int row )
        {
            return getColorAt(row+1);
        }
        
    }

}
