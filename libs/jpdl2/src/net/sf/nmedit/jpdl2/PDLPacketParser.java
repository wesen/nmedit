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

import net.sf.nmedit.jpdl2.impl.PDLMessageImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketImpl;
import net.sf.nmedit.jpdl2.impl.PDLParseContextImpl;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;
import net.sf.nmedit.jpdl2.stream.PDLDataSource;
import net.sf.nmedit.jpdl2.utils.StringUtils;

public class PDLPacketParser
{
    
    private static final boolean DEBUG = true;
    public static boolean LOG_ONLY_ERRORS = true;
    
    private PDLDataSource input;
    private BitStream bitstream;
    protected int reserved;
    private int beginBlockAtIndex = 0;
    private String messageId;
    private boolean generate; // == int stream source
    private StringBuilder logString = new StringBuilder();
   
    private PDLParseContextImpl context = new PDLParseContextImpl();
    
    private PDLDocument doc;
    
    public PDLPacketParser()
    {
        this(null);
    }
    
    public PDLPacketParser(PDLDocument doc)
    {
        this.doc = doc;
    }
    
    public String getLatestMessageId()
    {
        return messageId;
    }
    
    public BitStream getBitStream()
    {
        return bitstream;
    }

    public PDLMessage parseMessage(PDLDataSource input) throws PDLException
    {
        return parseMessage(input, doc);
    }
    
    public PDLMessage parseMessage(PDLDataSource input, String packetName) throws PDLException
    {
        return parseMessage(input, doc, packetName);
    }

    public PDLPacket parse(PDLDataSource input) throws PDLException
    {
        return parse(input, doc);
    }
    
    public PDLPacket parse(PDLDataSource input, String packetName) throws PDLException
    {
        return parse(input, doc, packetName);
    }
    
    public PDLMessage parseMessage(PDLDataSource input, PDLDocument document) throws PDLException
    {
        PDLPacket packet = parse(input, document);
        return new PDLMessageImpl(packet, messageId);
    }
    
    public PDLMessage parseMessage(PDLDataSource input, PDLDocument document, String packetName) throws PDLException
    {
        PDLPacket packet = parse(input, document, packetName);
        return new PDLMessageImpl(packet, messageId);
    }

    public PDLPacket parse(PDLDataSource input, PDLDocument document) throws PDLException
    {
        if (document.getStartPacketName() == null)
            throw new PDLException("start packet not defined");

        return parse(input, document, document.getStartPacketName());
    }
    public PDLPacket parse(PDLDataSource input, PDLDocument document, String packetName) throws PDLException
    {
        
        this.bitstream = null;
        this.input = null;
        
        PDLPacketDecl packetDecl = document.getPacketDecl(packetName);
        if (packetDecl == null)
            throw new PDLException("undefined packet: "+packetName);

        if (DEBUG)
        {
            logString.setLength(0);
            tabs=0;
            println("*************************************");
            println("parse new message, start="+packetName);
            
            if (input instanceof BitStream)
                println("bitstream: "+StringUtils.toHexadecimal(((BitStream)input).toByteArray()));
            else
            {
                println("intstream: "+StringUtils.toHexadecimal(((IntStream)input).toArray()));
            }
            
        }

        this.input = input;
        this.reserved = 0;
        this.beginBlockAtIndex = 0;
        this.messageId = null;
        context.input = input;
        
        if (input instanceof BitStream)
        {
            bitstream = (BitStream) input;
            generate = false;
        }
        else if (input instanceof IntStream)
        {
            bitstream = new BitStream();
            generate = true;
        }
        else
        {
            throw new PDLException("input must be IntStream or BitStream: "+input);
        }

        println("generate:"+generate);
        if (!isAvailable(getMinSize(packetDecl)))
            throw new PDLException(ensureBitsAvailableMessage(getMinSize(packetDecl)), packetDecl);
        this.reserved = getMinSize(packetDecl);
        
        context.stream = bitstream; 
        
        context.clearLabels();

        try
        {
            try
            {
                int packetStart = getPaddingStartValue(); 
                
                PDLPacketImpl packet = new PDLPacketImpl(packetDecl, null);
                parseBlock(packet, packetDecl);
                
                padding(packetDecl, packetStart);

                if (DEBUG && (!LOG_ONLY_ERRORS))
                {
                    if (generate)
                        println("generated bitstream: "+StringUtils.toHexadecimal(bitstream.toByteArray()));   
                    println("parsing successful");
                    println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println(logString);
                }
                
                return packet;
            }
            catch (Throwable t)
            {
                if (DEBUG)
                {
                    println("parsing failed");
                    input.setPosition(0);
                    
                    StringBuilder sb = new StringBuilder();
                    int steps = generate ? 1 : 8;

                    while (input.isAvailable(steps))
                        sb.append(Integer.toHexString(input.getInt(steps))+" ");
                    
                    println("message: "+sb.toString());
                    if (generate)
                        println("generated bitstream (incomplete): "+StringUtils.toHexadecimal(bitstream.toByteArray()));
                    println("error: "+toString(t));
                    println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println(logString);
                }
                
                throw new PDLException(toString(t), packetDecl);
            }
        }
        finally
        {
            this.input = null;
        }
    } 
    
    private void padding(PDLPacketDecl packetDecl, int startPos) throws PDLException
    {
        if (packetDecl.getPadding()>1)
        {
            int packetSize = (generate?bitstream.getSize():bitstream.getPosition())-startPos;
            int tail = packetSize % packetDecl.getPadding();
            if (tail>0)
            {
                int extraBits = packetDecl.getPadding() - tail;
                if (generate)
                {
                    if (DEBUG) println(">>> adding padding bits in packet "+packetDecl+": 0:"+extraBits);
                    while (extraBits>0)
                    {
                        bitstream.append(0, extraBits);
                        extraBits-=32;
                    }
                }
                else
                {
                    if (DEBUG) println(">>> skipping padding bits in packet "+packetDecl+": 0:"+extraBits);
                    while (extraBits>0)
                    {
                        int bits = input.getInt(extraBits);
                        
                        //if (bits != 0)
                        //    throw new PDLException("padding bits must be zero: "+bits, packetDecl);
                        
                        extraBits-=32;
                    }
                }
            }
        }
    }

    private static String toString(Throwable t)
    {
        StringBuilder msg = new StringBuilder();
        
        while (t != null)
        {
            StackTraceElement[] aste = t.getStackTrace();
            StackTraceElement ste = aste[0];
            msg.append("\tat "+t.getMessage()+" ("+ste.getFileName()+":"+ste.getLineNumber()+")"); 
            t=t.getCause();
            if (t != null) msg.append('\n');
        }
        return msg.toString();
    }
    
    private void addReserved(int value) throws PDLException
    {
        reserved+=value;
        if (reserved < 0)
            throw new InternalError("reserved negative: "+reserved);
    }
    
    private int getStreamPosition()
    {
        return generate ? bitstream.getSize() : bitstream.getPosition();
    }

    private void printItem(PDLItem item, String suffix)
    {
        println("--- item "+item+" \t(reserved:"+reserved+", pos:"+input.getPosition()+", size:"+input.getSize()+") "+suffix);
    }
    
    private void printItem(PDLItem item)
    {
        printItem(item, "");
    }
    
    void parseBlock(PDLPacketImpl packet, PDLBlock block) throws PDLException
    {
        tabs++;
        int index = beginBlockAtIndex;
        beginBlockAtIndex = 0;
        
        for (;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);

            if (DEBUG)
            {
                switch (item.getType())
                {
                    case Variable:
                        break;
                    case Conditional:
                        break;
                    default:
                        printItem(item);
                        break;
                }
            }
            
            switch (item.getType())
            {
                case Fail:
                {
                    throw new PDLException(item, "parsing failed");
                }
                case SwitchStatement:
                {
                    addReserved(-getMinSize(item));
                    
                    context.packet = packet; // set packet if not done already
                    PDLSwitchStatement sw = item.asSwitchStatement();
                    int value = sw.getFunction().compute(context);
                    PDLBlockItem bi = sw.getItemForCase(value);
                    if (bi != null)
                    {
                        if (DEBUG)
                        {
                            tabs++;
                            println("--- case '0x"+Integer.toHexString(value)+"' ("+value+" decimal)");
                        }
                        
                        ensureBitsAvailable(bi, getMinSize(bi));
                        addReserved(+getMinSize(bi));
                        parseBlock(packet, bi);
                        
                        if (DEBUG) tabs--;
                    }
                    break;
                }
                case MessageId:
                {
                    messageId = item.asInstruction().getString();
                    break;
                }
                case Label:
                {
                    context.setLabel(item.asLabel().getName(), packet.incrementAge(), getStreamPosition());
                    break;
                }
                
                case Constant:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLConstant constant = item.asConstant();

                    int multiplicity = PDLUtils.getMultiplicity(packet, constant.getMultiplicity());
                    
                    if (generate)
                    {
                        for (int i=0;i<multiplicity;i++)
                            bitstream.append(constant.getValue(), constant.getSize());
                    }
                    else
                    {
                        final int bitcount = constant.getSize()*multiplicity;
                        ensureBitsAvailable(item, bitcount);
    
                        while (multiplicity>0)
                        {
                            int value = input.getInt(constant.getSize());
                            if (value != constant.getValue())
                            {
                                throw new PDLException(constant, "constant mismatch: "+value);
                            }    
                            multiplicity--;
                        }
                    }
                    break;
                }

                case ImplicitVariable:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLImplicitVariable variable = item.asImplicitVariable();
                    int value;
                    if (generate)
                    {
                        value = computeChecksum(variable, context);
                        bitstream.append(value, variable.getSize());
                        if (DEBUG) println("generate checksum: "+value);
                    }
                    else
                    {
                        ensureBitsAvailable(item, generate?1:variable.getSize());
                        value = input.getInt(variable.getSize());
                        context.packet = packet;
                        checksum(variable, context, value);
                    }
                    packet.setVariable(variable.getName(), value);
                    break;
                }
                
                case Variable:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLVariable variable = item.asVariable();
                    ensureBitsAvailable(item, generate?1:variable.getSize());

                    int value = input.getInt(variable.getSize());
 
                    if (DEBUG) printItem(item, "\t=0x"+Integer.toHexString(value)+" (decimal: "+value+")");
                    
                    if (generate)
                    {
                        bitstream.append(value, variable.getSize());
                    }
                    
                    packet.setVariable(variable.getName(), value);
                    break;
                }
                
                case VariableList:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLVariableList variable = item.asVariableList(); 

                    int multiplicity = PDLUtils.getMultiplicity(packet, variable.getMultiplicity()); 
                    final int bitcount = (generate?1:variable.getSize()) * multiplicity;
                    
                    if (!variable.hasTerminal())
                        ensureBitsAvailable(item, bitcount);
                    
                    int[] values = new int[multiplicity];
                    
                    if (!variable.hasTerminal())
                    {
                        for (int i=0;i<multiplicity;i++)
                            values[i] = input.getInt(variable.getSize());
                    }
                    else
                    {
                        for (int i=0;i<multiplicity;i++)
                        {
                            int sz = generate ? 1 : variable.getSize();
                            if (!isAvailable(sz))
                                throw new PDLException(variable, ensureBitsAvailableMessage(sz));
                            
                            int value = input.getInt(variable.getSize());
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
                    
                    if (generate)
                    {
                        int size = variable.getSize();
                        for (int i=0;i<values.length;i++)
                            bitstream.append(values[i], size);
                        if (size<multiplicity && variable.hasTerminal())
                            bitstream.append(variable.getTerminal(), size);
                    }
                    
                    packet.setVariableList(variable.getName(), values);
                    break;
                }
                
                case PacketRef:
                {
                    // do not modify 'reserved' 
                    PDLPacketRef packetRef = item.asPacketRef();
                    PDLPacketDecl packetDecl = packetRef.getReferencedPacket();
                    //int bitcount = packetDecl.getMinimumSize();
                    //ensureBitsAvailable(item, bitcount);

                    int packetStart = getPaddingStartValue(); 
                    PDLPacketImpl context2 = new PDLPacketImpl(packetDecl, packetRef.getBinding());
                    try
                    {
                        parseBlock(context2, packetDecl);
                    }
                    catch (PDLException pdle)
                    {
                        throw new PDLException(pdle, packetRef);
                    }
                    
                    packet.setPacket(packetRef.getBinding(), context2);
                    
                    padding(packetDecl, packetStart);

                    break;
                }
                
                case PacketRefList:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLPacketRefList packetRefList = item.asPacketRefList();
                    PDLPacketDecl packetDecl = packetRefList.getReferencedPacket();
                    int multiplicity = PDLUtils.getMultiplicity(packet, packetRefList.getMultiplicity());
                    int bitcount = getMinSize(packetDecl)*multiplicity;
                    ensureBitsAvailable(item, bitcount);
                    addReserved(bitcount);
                    PDLPacketImpl[] packetList = new PDLPacketImpl[multiplicity];

                    try
                    {
                        tabs++; 
                        if (DEBUG) println("--- packet reference list "+packetRefList+" (reserved:"+reserved+",pos:"+input.getPosition()+",size:"+input.getSize()+")");
                        for (int i=0;i<multiplicity;i++)
                        {
                            int packetStart = getPaddingStartValue(); 
                            PDLPacketImpl context2 = new PDLPacketImpl(packetDecl, packetRefList.getBinding());

                                parseBlock(context2, packetDecl);
                            
                            packetList[i] = context2;
                            padding(packetDecl, packetStart);
                        }
                        tabs--;
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
                    PDLConditional conditional = item.asConditional();
                    context.packet = packet; // set the packet field
                    if (conditional.getCondition().isConditionTrue(context))
                    {
                        if (DEBUG) printItem(item);
                        ensureBitsAvailable(item, getMinSize(conditional));
                        addReserved(+getMinSize(item));

                        parseBlock(packet, conditional);
                    }
                    break;
                }

                case Optional:
                {

                    PDLOptional optional = item.asOptional();
                    
                    if (!isAvailable(getMinSize(optional)))
                    {
                        if (DEBUG) println(">>> optional ignored: not enough data in stream. minsize:"+getMinSize(item)
                                +"+"+reserved+" (reserved)"
                                +" input(position:"+input.getPosition()+",size:"+input.getSize()+")");
                        // necessary number of bits unavailable
                        break; // case statement
                    }
                    else
                    {
                        if (DEBUG) println(">>> trying optional block. minsize:"+getMinSize(item)
                                +"+"+reserved+" (reserved)"
                                +" input(position:"+input.getPosition()+",size:"+input.getSize()+")");
                    }
                    
                    int st_reserved = reserved;
                    int st_age = packet.getCurrentAge();
                    int st_inputPos = input.getPosition();
                    int st_streamSize = bitstream.getSize();
                    int st_tabs = tabs;
                    String st_messageId = this.messageId;

                    addReserved(+getMinSize(item));                    
                    try
                    {
                        parseBlock(packet, optional);
 
                        // try to parse remaining items in this block
                        beginBlockAtIndex = index+1; // start at next index
                        parseBlock(packet, block);

                        // try to parse the tails
                        
                        
                        /*
                        if (input.getPosition()<input.getSize())
                            throw new PDLException(item, "parsing message incomplete in this branch "
                                    +" input(position:"+input.getPosition()+","+input.getSize()+")");
                        */
                        // remaining message successfully parsed
                        return ;
                    }
                    catch (PDLException pdle)
                    {
                        tabs = st_tabs;
                        if (DEBUG) println("optional failed: "+toString(pdle));
                        
                        // undo optional item
                        reserved = st_reserved; // restore
                        packet.removeItemsOlderThan(st_age);
                        input.setPosition(st_inputPos);
                        this.messageId = st_messageId;
                        context.deleteLabelsOlderThan(st_age);
                        bitstream.setSize(st_streamSize);
                        // continue parsing
                    }
                    break;
                }
                case MutualExclusion:
                {
                    addReserved(-getMinSize(item));
                    
                    PDLMutualExclusion mexclusion = item.asMutualExclusion();
                    
                    ensureBitsAvailable(mexclusion, getMinSize(mexclusion));
                    int st_reserved = reserved;
                    int st_age = packet.getCurrentAge();
                    int st_inputPos = input.getPosition();
                    int st_streamSize = bitstream.getSize();
                    int st_tabs = tabs;
                    String st_messageId = this.messageId;
                    
                    for (PDLBlockItem choice: mexclusion)
                    {
                        if (isAvailable(getMinSize(choice)))
                        {
                            addReserved(+getMinSize(choice));                    
                            try
                            {
                                println("********* "+choice);
                                
                                parseBlock(packet, choice);

                                println("********* 2");
                                
                                // try to parse remaining items in this block
                                beginBlockAtIndex = index+1; // start at next index
                                parseBlock(packet, block);

                                println("********* 3");
                                // accepted
                                return;
                            }
                            catch (PDLException pdle)
                            {
                                println("********* 4");
                                tabs = st_tabs;
                                if (DEBUG) println("not chosen ("+choice+"), reason:"+toString(pdle));
                                
                                // undo optional item
                                reserved = st_reserved; // restore
                                packet.removeItemsOlderThan(st_age);
                                input.setPosition(st_inputPos);
                                this.messageId = st_messageId;
                                context.deleteLabelsOlderThan(st_age);
                                bitstream.setSize(st_streamSize);
                                // continue parsing
                            }
                        }
                    }
                    
                    throw new PDLException(mexclusion, "no elements were chosen");
                    
                }
                case Block:
                {
                    PDLBlockItem b = item.asBlock();
                    parseBlock(packet, b);
                    break;
                }
                default:
                {
                    throw new InternalError("unsupported item: "+item.getType());
                }
            }
        }
        tabs--;
    }
    private int tabs = 0;
    
    private int getPaddingStartValue()
    {
        return generate ? bitstream.getSize() : bitstream.getPosition();
    }
    
    private void println(String string)
    {
        for (int i=0;i<tabs;i++) logString.append(' ');
        logString.append(string);
        logString.append("\n");
    }
    
    private final int getMinSize(PDLPacketDecl packet)
    {
        return generate ? packet.getMinimumCount() : packet.getMinimumSize();
    }
    
    private final int getMinSize(PDLItem item)
    {
        return generate ? item.getMinimumCount() : item.getMinimumSize();
    }
    
    private int computeChecksum(PDLImplicitVariable item, PDLParseContext context) throws PDLException
    {
        PDLFunction function = item.getFunction();
        
        int savePos = bitstream.getPosition();
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
            bitstream.setPosition(savePos);
        }
        return expected;
    }

    private void checksum(PDLImplicitVariable item, PDLParseContext context, final int value)
        throws PDLException
    {
        int expected = computeChecksum(item, context);
        if (value != expected)
        {
            throw new PDLException(item, "implicit value different from stream value: "+
                    value+" (stream), "+expected+" (expected)");
        }
        
    }

    protected final boolean isAvailable(int size)
    {
        return input.isAvailable(reserved+size);
    }

    private String ensureBitsAvailableMessage(int size) throws PDLException
    {
        return "data unavailable: "
                    +(size+reserved)+"="+size+" + "+reserved+" (reserved) (remaining:"+
                    (input.getSize()-input.getPosition())+")";
    }

    private void ensureBitsAvailable(PDLItem item, int size) throws PDLException
    {
        if (!isAvailable(size))
            throw new PDLException(item, ensureBitsAvailableMessage(size));
    }

}
