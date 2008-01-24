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
package net.sf.nmedit.jpdl2;

public interface PDLItem 
{
    
    /**
     * Returns the minimum size of this item.
     * @return the minimum size of this item
     * 
     * TODO ensure that no infinite recursion occures 
     */
    int getMinimumSize();

    /**
     * Returns the type of this item 
     * @return the type of this item
     */
    PDLItemType getType();
    
    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLLabel asLabel();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLConstant asConstant();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLVariable asVariable();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLVariableList asVariableList();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLPacketRef asPacketRef();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLPacketRefList asPacketRefList();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLConditional asConditional();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLCallback asCallback();

    /**
     * Casts this item to the desired item type.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLOptional asOptional();
    
}
