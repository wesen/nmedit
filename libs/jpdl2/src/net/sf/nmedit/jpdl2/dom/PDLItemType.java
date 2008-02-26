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
 * Identifier of the various item types.
 * 
 * <table>
 * <thead>
 * <tr><th>PDLItemType</th><th>{@link PDLItem}</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>Constant</td><td>{@link PDLConstant}</td></tr>
 * <tr><td>Variable</td><td>{@link PDLVariable}</td></tr>
 * <tr><td>ImplicitVariable</td><td>{@link PDLVariable}</td></tr> 
 * <tr><td>AnonymVariable</td><td>{@link PDLVariable}</td></tr>
 * <tr><td>VariableList</td><td>{@link PDLVariable}</td></tr> 
 * <tr><td>PacketRef</td><td>{@link PDLPacketRef}</td></tr> 
 * <tr><td>InlinePacketRef</td><td>{@link PDLPacketRef}</td></tr>
 * <tr><td>PacketRefList</td><td>{@link PDLPacketRef}</td></tr> 
 * <tr><td>Conditional</td><td>{@link PDLConditional}</td></tr>
 * <tr><td>Block</td><td>{@link PDLBlock}</td></tr>
 * <tr><td>Choice</td><td>{@link PDLChoice}</td></tr>
 * <tr><td>SwitchStatement</td><td>{@link PDLSwitchStatement}</td></tr>
 * <tr><td>Label</td><td>{@link PDLInstruction}</td></tr> 
 * <tr><td>MessageId</td><td>{@link PDLInstruction}</td></tr>
 * <tr><td>Fail</td><td>{@link PDLInstruction}</td></tr> 
 * </tbody>
 * </table>
 */
public enum PDLItemType
{
    /**
     * Label 
     */
    Label, 
    
    /**
     * Constant
     */
    Constant,
    
    /**
     * Variable
     */
    Variable,
    
    /**
     * Implicit Variable / Checksum
     */
    ImplicitVariable,
    
    /**
     * Implicit Variable which is not part of the stream
     */
    AnonymousVariable,

    /**
     * Variable List
     */
    VariableList, 
    
    /**
     * Packet Reference
     */
    PacketRef,

    /**
     * inlined packet
     */
    InlinePacketRef,
    
    /**
     * Packet List
     */
    PacketRefList,
    
    /**
     * Condition
     */
    Conditional,
    
    /**
     * optional block
     */
    Optional,
    
    /**
     * choice-Statement
     */
    Choice,
    
    /**
     * Block
     */
    Block,
    
    /**
     * switch-Statement
     */
    SwitchStatement,
    
    // instructions
    
    /**
     * messageId instruction
     */
    MessageId,
    
    /**
     * fail instruction
     */
    Fail,
    
    /**
     * string definition
     */
    StringDef
}
