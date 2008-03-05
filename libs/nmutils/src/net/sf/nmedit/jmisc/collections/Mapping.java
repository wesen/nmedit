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
 * Created on Sep 2, 2006
 */
package net.sf.nmedit.jmisc.collections;

import java.io.Serializable;

public interface Mapping extends Serializable
{

    public int getIndex(Object o);
    
    public static class IDMapping implements Mapping
    {
        /**
         * 
         */
        private static final long serialVersionUID = -2933401665316464339L;

        public int getIndex( Object o )
        {
            return o == null ? -1 : ((Indexed)o).getIndex();
        }
    }
    
}
