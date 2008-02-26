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
 * Parser instructions.
 */
public interface PDLInstruction extends PDLItem
{
 
    /**
     * Returns the string value.
     * 
     * If the {@link PDLItem#getType() type} of this item is
     * {@link PDLItemType#MessageId MessageId} then the 
     * message-id string value is returned.
     * 
     * If the {@link PDLItem#getType() type} of this item is
     * {@link PDLItemType#Label Label} then the 
     * label name is returned.
     * 
     * If the {@link PDLItem#getType() type} of this item is
     * {@link PDLItemType#StringDef StringDef} then the 
     * string name is returned.
     * 
     * Otherwise the <code>null</code> is returned.
     * @return the string value
     */
    String getString();
    
    /**
     * If the {@link PDLItem#getType() type} of this item is
     * {@link PDLItemType#StringDef StringDef} then the 
     * string value is returned.
     * 
     * Otherwise <code>null</code> is returned.
     * @return the string value
     */
    String getString2();
    
    /**
     * Returns zero.
     * @return returns zero
     */
    int getMinimumSize();

    /**
     * Returns zero.
     * @return returns zero
     */
    int getMinimumCount();
    
    /**
     * Returns one of the valid instruction item types:
     * {@link PDLItemType#Label},
     * {@link PDLItemType#MessageId},
     * {@link PDLItemType#Fail}
     * {@link PDLItemType#StringDef}
     * @return returns the type of this item.
     */
    PDLItemType getType();
    
}
