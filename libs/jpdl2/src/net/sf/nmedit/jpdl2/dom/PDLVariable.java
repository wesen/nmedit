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
 * A variable, implicit variable, anonymous variable or a variable list.
 */
public interface PDLVariable extends PDLItem
{
    /**
     * Returns the minimum number of bits required for this item.
     * 
     * The return value depends on the {@link #getType() type} of this item.
     * <table>
     * <thead><tr><th>{@link PDLItemType ItemType}</th><th>Return Value</th></tr></thead>
     * <tbody>
     *  <tr><td>{@link PDLItemType#Variable}</td><td>{@link #getSize() size}</td></tr>
     *  <tr><td>{@link PDLItemType#ImplicitVariable}</td><td>{@link #getSize() size}</td></tr>
     *  <tr><td>{@link PDLItemType#AnonymousVariable}</td><td>0</td></tr>
     *  <tr><td style="vertical-align:top;">{@link PDLItemType#VariableList}</td><td>
     *  
     *  <table style="border:solid black 1px;">
     *  <thead><tr><th>multiplicity</th><th>minimum size</th></tr></thead>
     *  <tbody>
     *    <tr><td>variable</td><td>0</td></tr>
     *    <tr><td>constant, no terminal</td><td>constant*{@link #getSize() size}</td></tr>
     *    <tr><td>constant &gt;0, with terminal</td><td>size</td></tr>
     *    <tr><td>constant==0, with terminal</td><td>0</td></tr>
     *  </tbody>
     *  <tbody><tr>
     *  </table>
     * </td></tr>
     * </tbody>
     * </table>
     * 
     * @return the minimum number of bits required for this item
     */
    int getMinimumSize();

    /**
     * Returns the minimum number of values required for this item.
     * 
     * The return value depends on the {@link #getType() type} of this item.
     * <table>
     * <thead><tr><th>{@link PDLItemType ItemType}</th><th>Return Value</th></tr></thead>
     * <tbody>
     *  <tr><td>{@link PDLItemType#Variable}</td><td>1</td></tr>
     *  <tr><td>{@link PDLItemType#ImplicitVariable}</td><td>0</td></tr>
     *  <tr><td>{@link PDLItemType#AnonymousVariable}</td><td>0</td></tr>
     *  <tr><td style="vertical-align:top;">{@link PDLItemType#VariableList}</td><td>
     *  
     *  <table style="border:solid black 1px;">
     *  <thead><tr><th>multiplicity</th><th>minimum size</th></tr></thead>
     *  <tbody>
     *    <tr><td>variable</td><td>0</td></tr>
     *    <tr><td>constant, no terminal</td><td>constant</td></tr>
     *    <tr><td>constant &gt;0, with terminal</td><td>1</td></tr>
     *    <tr><td>constant==0, with terminal</td><td>0</td></tr>
     *  </tbody>
     *  <tbody><tr>
     *  </table>
     * </td></tr>
     * </tbody>
     * </table>
     * 
     * @return the minimum number of values required for this item
     */
    int getMinimumCount();

    /**
     * Returns {@link PDLItemType#Variable},
     * {@link PDLItemType#ImplicitVariable},
     * {@link PDLItemType#AnonymousVariable} or
     * {@link PDLItemType#VariableList}
     * @return {@link PDLItemType#Variable},
     * {@link PDLItemType#ImplicitVariable},
     * {@link PDLItemType#AnonymousVariable} or
     * {@link PDLItemType#VariableList}
     */
    PDLItemType getType();
    
    // shared properties

    /**
     * Returns the name of this variable.
     * @return the name of this variable
     */
    String getName();
    
    /**
     * Returns the size of this variable.
     * @return the size of this variable
     */
    int getSize();

    // **** implicit variable properties 
    
    /**
     * Returns the function assigned to this implicit variable.
     * 
     * If the {@link #getType() item type} is not {@link PDLItemType#ImplicitVariable ImplicitVariable}
     * the return value is undefined.
     * 
     * @return the function assigned to this implicit variable
     */
    PDLFunction getFunction();

    // **** variable list properties
    
    /**
     * Returns the {@link PDLMultiplicity multiplicity} of the variable list.
     * 
     * If the {@link #getType() item type} is not {@link PDLItemType#VariableList}
     * the return value is undefined.
     * 
     * @return the {@link PDLMultiplicity multiplicity} of the variable list
     */
    PDLMultiplicity getMultiplicity();

    /**
     * Returns true if the variable list has a terminal value.
     * 
     * If the {@link #getType() item type} is not {@link PDLItemType#VariableList}
     * the return value is undefined.
     * 
     * @return true if the variable list has a terminal value
     */
    boolean hasTerminal();

    /**
     * Returns the terminal value.
     * 
     * If the {@link #getType() item type} is not {@link PDLItemType#VariableList}
     * or {@link #hasTerminal()} is false the return value is undefined.
     * 
     * @return the terminal value
     */
    int getTerminal();

}
