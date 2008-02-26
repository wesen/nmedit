/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package net.sf.nmedit.jpdl2.stream;

/**
 * The data source.
 */
public interface PDLDataSource
{

    /**
     * Returns the data size.
     * @return the data size
     */
    int getSize();

    /**
     * Returns the current position.
     * @return the current position
     */
    int getPosition();

    /**
     * Sets the current position.
     * @param position the new position
     */
    void setPosition(int position);
    
    /**
     * Returns true if the specified amount of data is available. 
     * @param size amount of data
     * @return true if the specified amount of data is available
     */
    boolean isAvailable(int size);
    
    /**
     * Returns the next integer of the specified number of bits.
     * 
     * @param bitcount size of the integer (0 &lt;= bitcount &lt;= 32)
     * @return the next integer of the specified number of bits
     */
    int getInt(int bitcount);
    
}
