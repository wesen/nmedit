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
 * Defines a constant.
 * @author christian
 */
public interface PDLConstant extends PDLItem
{

    /**
     * The constant value.
     * @return the constant value
     */
    int getValue();
    
    /**
     * Size of the constant value
     * @return size
     */
    int getSize();
    
    /**
     * Returns the multiplicity of this constant.
     * A null value implies a multiplicity value of 1,
     * otherwise it depends on the mulitplicity value.
     * @return multiplicity of this constant
     */
    PDLMultiplicity getMultiplicity();

    /**
     * Returns {@link PDLItemType#Constant}
     * @return {@link PDLItemType#Constant}
     */
    PDLItemType getType();
    
}
