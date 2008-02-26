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

/**
 * Defines a case of the {@link PDLSwitchStatement}.
 */
public interface PDLCaseStatement
{

    /**
     * Returns the case block.
     * @return the case block
     */
    PDLBlock getBlock();
    
    /**
     * Returns true if this is the default case.
     * @return true if this is the default case
     */
    boolean isDefaultCase();
    
    /**
     * Returns the case constant. If {@link #isDefaultCase()}
     * is true, then the return value is undefined.
     * @return the case constant
     */
    int getValue();
    
}
