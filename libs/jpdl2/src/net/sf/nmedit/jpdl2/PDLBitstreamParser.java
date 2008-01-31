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

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpdl2.bitstream.BitStream;
import net.sf.nmedit.jpdl2.impl.PDLMessageImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketImpl;
import net.sf.nmedit.jpdl2.impl.PDLParseContextImpl;

public class PDLBitstreamParser
{

    private Map<String, PDLFunction> implicitFunctions = new HashMap<String, PDLFunction>();
    private BitStream stream;
    protected int reserved;
    private int beginBlockAtIndex = 0;
    private String messageId;
    
    private PDLParseContextImpl context = new PDLParseContextImpl();
    
    public void defineFunction(String name, PDLFunction f)
    {
        implicitFunctions.put(name, f);
    }
    
    public PDLFunction getFunction(String name) 
    {
        return implicitFunctions.get(name);
    }
    
    public String getLatestMessageId()
    {
        return messageId;
    }
    
    public PDLMessage parseMessage(BitStream stream, PDLDocument document) throws PDLException
    {
        PDLPacket packet = parse(stream, document);
        return new PDLMessageImpl(packet, messageId);
    }
    
    public PDLMessage parseMessage(BitStream stream, PDLDocument document, String packetName) throws PDLException
    {
        PDLPacket packet = parse(stream, document, packetName);
        return new PDLMessageImpl(packet, messageId);
    }

    public PDLPacket parse(BitStream stream, PDLDocument document) throws PDLException
    {
        if (document.getStartPacketName() == null)
            throw new PDLException("start packet not defined");
        return parse(stream, document, document.getStartPacketName());
    }
    
    public PDLPacket parse(BitStream stream, PDLDocument document, String packetName) throws PDLException
    {
        PDLPacketDecl packetDecl = document.getPacketDecl(packetName);
        if (packetDecl == null)
            throw new PDLException("undefined packet: "+packetName);

        this.reserved = packetDecl.getMinimumSize();
        if (!stream.isAvailable(reserved))
            throw new PDLException("required number of bits unavailable: "+reserved);
        
        this.stream = stream;
        this.beginBlockAtIndex = 0;
        this.messageId = null;
        context.stream = stream;
        context.clearLabels();

        try
        {
            try
            {
                PDLPacketImpl packet = new PDLPacketImpl();
                parseBlock(packet, packetDecl);
                return packet;
            }
            catch (PDLException pdle)
            {
                throw new PDLException(pdle, packetDecl);
            }
        }
        finally
        {
            this.stream = null;
        }
    }

    void parseBlock(PDLPacketImpl packet, PDLBlock block) throws PDLException
    {
        int index = beginBlockAtIndex;
        beginBlockAtIndex = 0;
        
        for (;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);
            
            switch (item.getType())
            {
                case MessageId:
                {
                    messageId = item.asMessageId().getMessageId();
                    break;
                }

                case Label:
                {
                    context.setLabel(item.asLabel().getName(), packet.incrementAge(), stream.getPosition());
                    break;
                }
                
                case Constant:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLConstant constant = item.asConstant();

                    int multiplicity = PDLUtils.getMultiplicity(packet, constant.getMultiplicity());
                    final int bitcount = constant.getSize()*multiplicity;
                    ensureBitsAvailable(item, bitcount);

                    while (multiplicity>0)
                    {
                        int value = stream.getInt(constant.getSize());
                        if (value != constant.getValue())
                        {
                            throw new PDLException(constant, "constant mismatch: "+value);
                        }
                        
                        multiplicity--;
                    }
                    break;
                }

                case ImplicitVariable:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLImplicitVariable variable = item.asImplicitVariable();
                    ensureBitsAvailable(item, variable.getSize());

                    
                    int value = stream.getInt(variable.getSize());
                    
                    context.packet = packet;
                    checksum(variable, context, value);
                    
                    packet.setVariable(variable.getName(), value);
                    break;
                }
                
                case Variable:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLVariable variable = item.asVariable();
                    ensureBitsAvailable(item, variable.getSize());
                    
                    packet.setVariable(variable.getName(), stream.getInt(variable.getSize()));
                    break;
                }
                
                case VariableList:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLVariableList variable = item.asVariableList(); 

                    int multiplicity = PDLUtils.getMultiplicity(packet, variable.getMultiplicity()); 
                    final int bitcount = variable.getSize() * multiplicity;
                    ensureBitsAvailable(item, bitcount);
                    
                    int[] values = new int[multiplicity];
                    
                    if (!variable.hasTerminal())
                    {
                        for (int i=0;i<multiplicity;i++)
                            values[i] = stream.getInt(variable.getSize());
                    }
                    else
                    {
                        for (int i=0;i<multiplicity;i++)
                        {
                            int value = stream.getInt(variable.getSize());
                            if (value == variable.getTerminal())
                            {
                                // shrink array
                                int[] array = new int[i];
                                System.arraycopy(values, 0, array, 0, array.length);
                                values = array;
                                break;
                            }
                            values[i] = value;
                        }
                    }
                    
                    packet.setVariableList(variable.getName(), values);
                    break;
                }
                
                case PacketRef:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLPacketRef packetRef = item.asPacketRef();
                    PDLPacketDecl packetDecl = packetRef.getReferencedPacket();
                    int bitcount = packetDecl.getMinimumSize();
                    ensureBitsAvailable(item, bitcount);

                    PDLPacketImpl context2 = new PDLPacketImpl();
                    try
                    {
                        parseBlock(context2, packetDecl);
                    }
                    catch (PDLException pdle)
                    {
                        throw new PDLException(pdle, packetRef);
                    }
                    
                    packet.setPacket(packetRef.getBinding(), context2);
                    
                    break;
                }
                
                case PacketRefList:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLPacketRefList packetRefList = item.asPacketRefList();
                    PDLPacketDecl packetDecl = packetRefList.getReferencedPacket();
                    int multiplicity = PDLUtils.getMultiplicity(packet, packetRefList.getMultiplicity());
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
                    catch (PDLException pdle)
                    {
                        throw new PDLException(pdle, packetRefList);
                    }
                    
                    packet.setPacketList(packetRefList.getBinding(), packetList);
                    break; 
                }
                
                case Conditional:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLConditional conditional = item.asConditional();
                    ensureBitsAvailable(item, conditional.getMinimumSize());
                    context.packet = packet; // set the packet field
                    if (conditional.getCondition().isConditionTrue(context))
                        parseBlock(packet, conditional);
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
                    int storeAge = packet.getCurrentAge();
                    int storePos = stream.getPosition();
                    String storeMessageId = this.messageId;

                    reserved -= item.getMinimumSize();
                    
                    try
                    {
                        parseBlock(packet, optional);
                        
                        // try to parse remaining message
                        beginBlockAtIndex = index+1; // start at next index
                        parseBlock(packet, block);

                        // remaining message successfully parsed
                        return ;
                    }
                    catch (PDLException pdle)
                    {
                        // undo optional item
                        reserved = storeReserved; // restore
                        packet.removeItemsOlderThan(storeAge);
                        stream.setPosition(storePos);
                        this.messageId = storeMessageId;
                        context.deleteLabelsOlderThan(storeAge);
                        // continue parsing
                    }
                    break;
                }
            }
        }
        
    }

    private void checksum(PDLImplicitVariable item, PDLParseContext context, final int value)
        throws PDLException
    {
        PDLFunction function = item.getFunction();
        
        int savePos = stream.getPosition();
        int expected;
        try
        {
            expected = function.compute(context);
        }
        catch (PDLException pdle)
        {
            throw new PDLException(pdle);
        }
        finally
        {
            stream.setPosition(savePos);
        }
        
        if (value != expected)
        {
            throw new PDLException(item, "implicit value different from stream value: "+
                    value+" (stream), "+expected+" (expected)");
        }
        
    }

    protected final boolean isAvailable(int bitcount)
    {
        return stream.isAvailable(reserved+bitcount);
    }

    private void ensureBitsAvailable(PDLItem item, int bitcount) throws PDLException
    {
        if (!isAvailable(bitcount))
            throw new PDLException(item, "required number of bits unavailable: "
                    +(bitcount+reserved)+"="+bitcount+" + "+reserved+" (reserved)");
    }

}
