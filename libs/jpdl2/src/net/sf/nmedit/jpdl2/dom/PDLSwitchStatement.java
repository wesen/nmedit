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
package net.sf.nmedit.jpdl2.dom;

import java.util.List;

/**
 * Defines several cases, depending on the function result the appropriate case
 * is picked.
 */
public interface PDLSwitchStatement extends PDLItem, Iterable<PDLCaseStatement>
{
    
    /**
     * Returns the function which selects a specific case.
     * @return the function which selects a specific case 
     */
    PDLFunction getFunction();
    
    /**
     * Returns all case statements including the default case
     * (if declared).
     * @return all case statements
     */
    List<PDLCaseStatement> getItems();

    /**
     * Returns the item for the specified value.
     * @param value the case value
     * @return the item for the specified value
     */
    PDLBlock getItemForCase(int value);

    /**
     * Returns {@link PDLItemType#SwitchStatement}
     * @return {@link PDLItemType#SwitchStatement}
     */
    PDLItemType getType();
    
}
