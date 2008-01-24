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
package net.sf.nmedit.jpdl2.parser;

import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl2.PDLBlock;
import net.sf.nmedit.jpdl2.PDLCallback;
import net.sf.nmedit.jpdl2.PDLConditional;
import net.sf.nmedit.jpdl2.PDLConstant;
import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLItem;
import net.sf.nmedit.jpdl2.PDLOptional;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketDecl;
import net.sf.nmedit.jpdl2.PDLPacketRef;
import net.sf.nmedit.jpdl2.PDLPacketRefList;
import net.sf.nmedit.jpdl2.PDLUtils;
import net.sf.nmedit.jpdl2.PDLVariable;
import net.sf.nmedit.jpdl2.PDLVariableList;
import net.sf.nmedit.jpdl2.impl.PDLPacketImpl;

public class PDLBitstreamParser
{

    private BitStream stream;
    protected int reserved;
    private int beginBlockAtIndex = 0;
    
    public PDLPacket parse(BitStream stream, PDLDocument document, String packetName) throws PDLParseException
    {
        PDLPacketDecl packetDecl = document.getPacketDecl(packetName);
        if (packetDecl == null)
            throw new PDLParseException("undefined packet: "+packetName);

        this.stream = stream;
        this.reserved = packetDecl.getMinimumSize();
        this.beginBlockAtIndex = 0;

        try
        {
            PDLPacketImpl packet = new PDLPacketImpl();
            parseBlock(packet, packetDecl);
            return packet;
        }
        finally
        {
            this.stream = null;
        }
    }

    void parseBlock(PDLPacketImpl context, PDLBlock block) throws PDLParseException
    {
        int index = beginBlockAtIndex;
        beginBlockAtIndex = 0;
        
        for (;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);
            
            switch (item.getType())
            {
                case Callback:
                {
                    handleCallback(item.asCallback());
                    break;
                }

                case Label:
                {
                    context.setLabel(item.asLabel().getName(), stream.getPosition());
                    break;
                }
                
                case Constant:
                {
                    PDLConstant constant = item.asConstant();

                    int multiplicity = PDLUtils.getMultiplicity(context, constant.getMultiplicity());
                    final int bitcount = constant.getSize()*multiplicity;
                    ensureBitsAvailable(item, bitcount);

                    while (multiplicity>0)
                    {
                        int value = stream.getInt(constant.getSize());
                        if (value != constant.getValue())
                        {
                            throw new PDLParseException(constant, "constant mismatch: "+value);
                        }
                        
                        multiplicity--;
                    }
                    reserved -= bitcount;
                    break;
                }
                
                case Variable:
                {
                    PDLVariable variable = item.asVariable();
                    ensureBitsAvailable(item, variable.getSize());
                    
                    context.setVariable(variable.getName(), stream.getInt(variable.getSize()));
                    reserved -= variable.getSize();
                    break;
                }
                
                case VariableList:
                {
                    PDLVariableList variable = item.asVariableList(); 

                    int multiplicity = PDLUtils.getMultiplicity(context, variable.getMultiplicity()); 
                    final int bitcount = variable.getSize() * multiplicity;
                    ensureBitsAvailable(item, bitcount);
                    
                    int[] values = new int[multiplicity];
                    for (int i=0;i<multiplicity;i++)
                        values[i] = stream.getInt(variable.getSize());
                    
                    context.setVariableList(variable.getName(), values);
                    reserved -= bitcount;
                    break;
                }
                
                case PacketRef:
                {
                    PDLPacketRef packetRef = item.asPacketRef();
                    PDLPacketDecl packetDecl = packetRef.getReferencedPacket();
                    int bitcount = packetDecl.getMinimumSize();
                    ensureBitsAvailable(item, bitcount);

                    PDLPacketImpl context2 = new PDLPacketImpl();
                    try
                    {
                        parseBlock(context2, packetDecl);
                    }
                    catch (PDLParseException pdle)
                    {
                        throw new PDLParseException(pdle, packetRef);
                    }
                    
                    context.setPacket(packetRef.getBinding(), context2);
                    
                    break;
                }
                
                case PacketRefList:
                {
                    PDLPacketRefList packetRefList = item.asPacketRefList();
                    PDLPacketDecl packetDecl = packetRefList.getReferencedPacket();
                    int multiplicity = PDLUtils.getMultiplicity(context, packetRefList.getMultiplicity());
                    int bitcount = packetDecl.getMinimumSize()*multiplicity;
                    ensureBitsAvailable(item, bitcount);
                    PDLPacketImpl[] packetList = new PDLPacketImpl[multiplicity];

                    try
                    {
                        for (int i=0;i<multiplicity;i++)
                        {
                            PDLPacketImpl context2 = new PDLPacketImpl();
                            
                                parseBlock(context2, packetDecl);
                            
                            packetList[i] = context2;
                        }
                    }
                    catch (PDLParseException pdle)
                    {
                        throw new PDLParseException(pdle, packetRefList);
                    }
                    
                    context.setPacketList(packetRefList.getBinding(), packetList);
                    break; 
                }
                
                case Conditional:
                {
                    PDLConditional conditional = item.asConditional();
                    ensureBitsAvailable(item, conditional.getMinimumSize());
                    if (conditional.getCondition().isConditionTrue(context))
                        parseBlock(context, conditional);
                    break;
                }
                
                case Optional:
                {
                    PDLOptional optional = item.asOptional();
                    
                    if (!isAvailable(optional.getMinimumSize()))
                    {
                        // necessary number of bits unavailable
                        break; // case statement
                    }
                    
                    int storeReserved = reserved;
                    int storeAge = context.getCurrentAge();
                    int storePos = stream.getPosition();
                    
                    try
                    {
                        parseBlock(context, optional);
                        
                        // try to parse remaining message
                        beginBlockAtIndex = index+1; // start at next index
                        parseBlock(context, block);

                        // remaining message successfully parsed
                        return ;
                    }
                    catch (PDLParseException pdle)
                    {
                        // undo optional item
                        reserved = storeReserved; // restore
                        context.removeItemsOlderThan(storeAge);
                        stream.setPosition(storePos);
                     
                        // continue parsing
                        break;
                    }
                }
            }
        }
        
    }

    protected final boolean isAvailable(int bitcount)
    {
        return stream.isAvailable(reserved+bitcount);
    }

    private void ensureBitsAvailable(PDLItem item, int bitcount) throws PDLParseException
    {
        if (!isAvailable(bitcount))
            throw new PDLParseException(item, "required number of bits unavailable: "
                    +(bitcount+reserved)+"="+bitcount+" + "+reserved+" (reserved)");
    }

    private void handleCallback(PDLCallback callback)
    {
        // TODO Auto-generated method stub
        
    }

}
