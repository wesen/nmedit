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

import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

/**
 * A item/statement in a packet declaration.
 */
public interface PDLItem 
{
    
    /**
     * Returns the minimum number of bits required to parse this item (and it's children).
     * 
     * The return value is used by the packet parser to estimate the number of required
     * bits in the {@link BitStream BitStream}.
     * 
     * Conditional children are not included in the calculation.
     * 
     * @return the minimum size of this item and it's children 
     */
    int getMinimumSize();

    /**
     * Returns the minimum number of values required to parse this item (and it's children).
     * 
     * The return value is used by the packet parser to estimate the number of required
     * values in the {@link IntStream IntStream}.
     * 
     * Conditional children are not included in the calculation.
     * 
     * @return the minimum number of values of this item and it's children
     */
    int getMinimumCount();
    
    /**
     * Returns the {@link PDLItemType type} of this item.
     * @return the type of this item
     */
    PDLItemType getType();
    
    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLConstant asConstant();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLVariable asVariable();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLPacketRef asPacketRef();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLConditional asConditional();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLOptional asOptional();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLChoice asChoice();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLBlock asBlock();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLInstruction asInstruction();

    /**
     * Casts this item to the desired item type.
     * Casting this item directly to the desired item type is not guaranteed to be successful.
     * @throws ClassCastException if the item is not of the desired type
     */
    PDLSwitchStatement asSwitchStatement();
    
}
