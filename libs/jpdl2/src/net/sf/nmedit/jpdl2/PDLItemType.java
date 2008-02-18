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

/**
 * Identifier of the various {@link PDLItem item} types.
 * 
 * <table>
 * <thead>
 * <tr><th>PDLItemType</th><th>PDLItem</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>Label</td><td>PDLLabel</td></tr> 
 * <tr><td>Constant</td><td>PDLConstant</td></tr>
 * <tr><td>Variable</td><td>PDLVariable</td></tr>
 * <tr><td>ImplicitVariable</td><td>PDLImplicitVariable</td></tr>
 * <tr><td>VariableList</td><td>PDLVariableList</td></tr> 
 * <tr><td>PacketRef</td><td>PDLPacketRef</td></tr>
 * <tr><td>PacketRefList</td><td>PDLPacketRefList</td></tr> 
 * <tr><td>Conditional</td><td>PDLConditional</td></tr>
 * <tr><td>MessageId</td><td>PDLMessageId</td></tr> 
 * <tr><td>Optional</td><td>PDLOptional</td></tr>
 * </tbody>
 * </table>
 */
public enum PDLItemType
{
    Label, 
    Constant,
    ImplicitVariable,
    Variable,
    VariableList, 
    PacketRef,
    PacketRefList, 
    Conditional, 
    Optional,
    MutualExclusion,
    Block,
    SwitchStatement,
    
    // instructions
    MessageId,
    Break,
    Fail
}
