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

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpdl2.bitstream.BitStream;
import net.sf.nmedit.jpdl2.PDLBlock;
import net.sf.nmedit.jpdl2.PDLConditional;
import net.sf.nmedit.jpdl2.PDLConstant;
import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLFunction;
import net.sf.nmedit.jpdl2.PDLImplicitVariable;
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

    private Map<String, PDLFunction> implicitFunctions = new HashMap<String, PDLFunction>();
    private BitStream stream;
    protected int reserved;
    private int beginBlockAtIndex = 0;
    
    public void defineFunction(String name, PDLFunction f)
    {
        implicitFunctions.put(name, f);
    }
    
    public PDLFunction getFunction(String name) 
    {
        return implicitFunctions.get(name);
    }

    public PDLPacket parse(BitStream stream, PDLDocument document) throws PDLParseException
    {
        if (document.getStartPacketName() == null)
            throw new PDLParseException("start packet not defined");
        return parse(stream, document, document.getStartPacketName());
    }
    
    public PDLPacket parse(BitStream stream, PDLDocument document, String packetName) throws PDLParseException
    {
        PDLPacketDecl packetDecl = document.getPacketDecl(packetName);
        if (packetDecl == null)
            throw new PDLParseException("undefined packet: "+packetName);

        this.reserved = packetDecl.getMinimumSize();
        if (!stream.isAvailable(reserved))
            throw new PDLParseException("required number of bits unavailable: "+reserved);
        
        this.stream = stream;
        this.beginBlockAtIndex = 0;

        try
        {
            try
            {
                PDLPacketImpl packet = new PDLPacketImpl();
                parseBlock(packet, packetDecl);
                return packet;
            }
            catch (PDLParseException pdle)
            {
                throw new PDLParseException(pdle, packetDecl);
            }
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
                case MessageId:
                {
                    //  TODO store messageid
                    break;
                }

                case Label:
                {
                    context.setLabel(item.asLabel().getName(), stream.getPosition());
                    break;
                }
                
                case Constant:
                {
                    reserved -= item.getMinimumSize();
                    
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
                    break;
                }

                case ImplicitVariable:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLImplicitVariable variable = item.asImplicitVariable();
                    ensureBitsAvailable(item, variable.getSize());

                    
                    int value = stream.getInt(variable.getSize());
                    
                    checksum(variable, context, value);
                    
                    context.setVariable(variable.getName(), value);
                    reserved -= variable.getSize();
                    break;
                }
                
                case Variable:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLVariable variable = item.asVariable();
                    ensureBitsAvailable(item, variable.getSize());
                    
                    context.setVariable(variable.getName(), stream.getInt(variable.getSize()));
                    reserved -= variable.getSize();
                    break;
                }
                
                case VariableList:
                {
                    reserved -= item.getMinimumSize();
                    
                    PDLVariableList variable = item.asVariableList(); 

                    int multiplicity = PDLUtils.getMultiplicity(context, variable.getMultiplicity()); 
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
                    
                    context.setVariableList(variable.getName(), values);
                    reserved -= bitcount;
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
                    catch (PDLParseException pdle)
                    {
                        throw new PDLParseException(pdle, packetRef);
                    }
                    
                    context.setPacket(packetRef.getBinding(), context2);
                    
                    break;
                }
                
                case PacketRefList:
                {
                    reserved -= item.getMinimumSize();
                    
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
                    reserved -= item.getMinimumSize();
                    
                    PDLConditional conditional = item.asConditional();
                    ensureBitsAvailable(item, conditional.getMinimumSize());
                    if (conditional.getCondition().isConditionTrue(context))
                        parseBlock(context, conditional);
                    break;
                }
                
                case Optional:
                {

                    reserved -= item.getMinimumSize();
                    
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

    private void checksum(PDLImplicitVariable item, PDLPacketImpl context, final int value)
        throws PDLParseException
    {
        PDLFunction function = item.getFunction();
        
        int savePos = stream.getPosition();
        int expected;
        try
        {
            expected = function.compute(context, stream);
        }
        catch (PDLException pdle)
        {
            throw new PDLParseException(pdle);
        }
        finally
        {
            stream.setPosition(savePos);
        }
        
        if (value != expected)
        {
            throw new PDLParseException(item, "implicit value different from stream value: "+
                    value+" (stream), "+expected+" (expected)");
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

}
