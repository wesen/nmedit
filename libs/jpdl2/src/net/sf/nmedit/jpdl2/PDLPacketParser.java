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

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLChoice;
import net.sf.nmedit.jpdl2.dom.PDLConditional;
import net.sf.nmedit.jpdl2.dom.PDLConstant;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLInstruction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLOptional;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;
import net.sf.nmedit.jpdl2.dom.PDLSwitchStatement;
import net.sf.nmedit.jpdl2.dom.PDLVariable;
import net.sf.nmedit.jpdl2.impl.PDLMessageImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketImpl;
import net.sf.nmedit.jpdl2.impl.PDLParseContextImpl;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;
import net.sf.nmedit.jpdl2.stream.PDLDataSource;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLPacketParser
{
    
    private static final boolean DEBUG = false;
    private static final boolean LOG_ONLY_ERRORS = true;
    
    private PDLDataSource input;
    private BitStream bitstream;
    protected int reserved;
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
    
    public PDLDocument getDocument()
    {
        return doc;
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

        if (packetDecl.isInlined())
            throw new PDLException("packet can only be used inline: "+packetName);
        
        if (DEBUG)
        {
            logString.setLength(0);
            tabs=0;
            println("*************************************");
            println("parse new message, start="+packetName);
            
            if (input instanceof BitStream)
                println("bitstream: "+PDLUtils.toHexadecimal(((BitStream)input).toByteArray()));
            else
            {
                println("intstream: "+PDLUtils.toHexadecimal(((IntStream)input).toArray()));
            }
            
        }

        pstack.clear();
        this.input = input;
        this.reserved = 0;
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
                parseBlock(packet, packetDecl, 0);
                
                padding(packetDecl, packetStart);

                if (DEBUG && (!LOG_ONLY_ERRORS))
                {
                    if (generate)
                        println("generated bitstream: "+PDLUtils.toHexadecimal(bitstream.toByteArray()));   
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
                        println("generated bitstream (incomplete): "+PDLUtils.toHexadecimal(bitstream.toByteArray()));
                    println("error: "+toString(t));
                    println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println(logString);
                }
                
                PDLException pe = new PDLException(toString(t), packetDecl);
                // pe.initCause(t);
                throw pe;
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
            
            msg.append("\tat "+t+" ("+ste.getFileName()+":"+ste.getLineNumber()+")"); 
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
    
    private class ParseState
    {
        PDLPacketImpl packet;
        PDLBlock block;
        int index;
        
        public ParseState(PDLPacketImpl packet, PDLBlock block)
        {
            this.packet = packet;
            this.block = block;
            this.index = 0;
        }
        boolean recover() { return false; }
        void complete() throws PDLException {};
    }
    
    private class BlockState extends ParseState
    {
        public BlockState(PDLPacketImpl packet, PDLBlock block)
        {
            super(packet, block);
        }
    }
    
    private class PacketState extends ParseState
    {
        private PDLPacketImpl insertInto;
        private int packetStart;
        private String binding;
        
        public PacketState(PDLPacketImpl packet, PDLPacketRef ref, PDLPacketDecl pdecl, PDLPacketImpl insertInto, int packetStart)
        {
            super(packet, pdecl);
            this.binding = ref.getBinding();
            this.insertInto = insertInto;
            this.packetStart = packetStart;
        }
        void complete() throws PDLException
        {
            padding((PDLPacketDecl)block, packetStart);
            if (insertInto != null)
                insertInto.setPacket(binding, packet);
        }
    }
    
    private class RestorableParseState extends ParseState
    {
        
        int st_reserved;
        int st_age;
        int st_inputPos;
        int st_streamSize;
        int st_tabs;
        String st_messageId;
        int stackSize;
        
        public RestorableParseState(PDLPacketImpl packet)
        {
            super(packet, null);
            init();
        }
        
        void init()
        {
            this.st_age = packet.getCurrentAge();
            this.st_reserved = PDLPacketParser.this.reserved;
            this.st_inputPos = PDLPacketParser.this.input.getPosition();
            this.st_streamSize = PDLPacketParser.this.bitstream.getSize();
            this.st_tabs = PDLPacketParser.this.tabs;
            this.st_messageId = PDLPacketParser.this.messageId;
            this.stackSize = pstack.size();
        }
        
        void restore()
        {
            PDLPacketParser.this.tabs = st_tabs;
            PDLPacketParser.this.reserved = st_reserved; // restore
            this.packet.removeItemsOlderThan(st_age);
            PDLPacketParser.this.input.setPosition(st_inputPos);
            PDLPacketParser.this.messageId = st_messageId;
            PDLPacketParser.this.context.deleteLabelsOlderThan(st_age);
            PDLPacketParser.this.bitstream.setSize(st_streamSize);
            while (stackSize<pstack.size()) pop();
        }
        
    }
    
    List<ParseState> pstack = new ArrayList<ParseState>(1000);
    
    private void push(ParseState state)
    {
        pstack.add(state);
    }
    
    private ParseState pop()
    {
        if (pstack.isEmpty())
            throw new IllegalStateException("stack is empty");    
        return pstack.remove(pstack.size()-1);
    }
    
    private ParseState peek()
    {
        if (pstack.isEmpty())
            throw new IllegalStateException("stack is empty");
        return pstack.get(pstack.size()-1);
    }
    
    void parseState(int stackSizeThreshold) throws PDLException
    {
        while (pstack.size()>=stackSizeThreshold)
        {
            ParseState state = peek();
            PDLBlock block = state.block;
            if (state.index>=block.getItemCount())
            {
                // block completed
                state.complete();
                pop();
                continue;
            }

            context.packet = state.packet; // set packet if not done already, TODO no need to set this every time
            int index = state.index;
            PDLItem item = block.getItem(index);
            boolean internallyHandled = parseItem(item, state.packet, state.block, index);
            if (internallyHandled)
            {
                state.complete();
                pop();
            }
            state.index++;
        }
    }

    void parseBlock(PDLPacketImpl packet, PDLBlock block, int startIndex) throws PDLException
    {
        tabs++;

        /*
        for (int index=startIndex;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);
            context.packet = packet;
            boolean internallyHandled = parseItem(item, packet, block, index);
            if (internallyHandled) return;
        }
        */
        
        
        /* We are using both a recursive and an iterative (using a stack) approach.
         * TODO completely eliminate recursion to avoid possible StackOverflow errors
         * TODO cache instances of ParseState and it's subclasses
         */
        
        
     
        insertBlock(packet, block, startIndex);
        parseState(pstack.size());
        tabs--;
    }
    
    void insertBlock(PDLPacketImpl packet, PDLBlock block)
    {
        insertBlock(packet, block, 0);
    }
    
    void insertBlock(PDLPacketImpl packet, PDLBlock block, int startIndex)
    {
        BlockState state = new BlockState(packet, block);
        state.index = startIndex;
        push(state);
    }
    
    void insertState(PacketState state)
    {
        push(state);
    }
    
    private int tabs = 0;
    
    boolean parseItem(final PDLItem item, final PDLPacketImpl packet, final PDLBlock block, final int index) throws PDLException
    {
        // returns true if block is internally handled
        
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
                
                PDLSwitchStatement sw = item.asSwitchStatement();
                int value = sw.getFunction().compute(context);
                PDLBlock bi = sw.getItemForCase(value);
                if (bi != null)
                {
                    if (DEBUG)
                    {
                        tabs++;
                        println("--- case '0x"+Integer.toHexString(value)+"' ("+value+" decimal)");
                    }
                    
                    ensureBitsAvailable(bi, getMinSize(bi));
                    addReserved(+getMinSize(bi));
                    // parseBlock(packet, bi);
                    insertBlock(packet, bi);
                    
                    if (DEBUG) tabs--;
                }
                break;
            }
            case MessageId:
            {
                messageId = item.asInstruction().getString();
                break;
            }
            
            case StringDef:
            {
                PDLInstruction ins = item.asInstruction();
                packet.setString(ins.getString(), ins.getString2());
                break;
            }
            
            case Label:
            {
                context.setLabel(item.asInstruction().getString(), packet.incrementAge(), getStreamPosition());
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
                
                PDLVariable variable = item.asVariable();
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
                    context.packet = packet; // set packet if not done already
                    checksum(variable, context, value);
                }
                packet.setVariable(variable.getName(), value);
                break;
            }

            case AnonymousVariable:
            {
                context.packet = packet; // set packet if not done already
                // only add value to packet, not part of stream
                PDLVariable variable = item.asVariable();
                int value = computeChecksum(variable, context);
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
                
                PDLVariable variable = item.asVariable(); 
    
    			// this is a hack and will be removed later
    			final String HACK = "HACK";
    
                boolean hack = false;
                int multiplicity;
                
                PDLMultiplicity vm = variable.getMultiplicity();
                
                if (vm != null && vm.getType() == PDLMultiplicity.Type.Variable
                        && HACK.equals(vm.getVariable()))
                {
                    hack = true;
                    int hackedValue;
                    if (generate)
                    {
                        hackedValue = input.getSize()-input.getPosition(); // remaining stream
                    }
                    else
                    {
                        hackedValue = (input.getSize()-input.getPosition())/8-2; // remaining stream with out last two byte
                    }
                    packet.setVariable(HACK, hackedValue);
                    if (DEBUG) println("HACKED VALUE="+hackedValue);
                }
                
                multiplicity = PDLUtils.getMultiplicity(packet, variable.getMultiplicity());
                
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

            case InlinePacketRef:
            {
                // do not modify 'reserved' 
                PDLPacketRef packetRef = item.asPacketRef();
                PDLPacketDecl packetDecl = packetRef.getReferencedPacket();
                //int bitcount = packetDecl.getMinimumSize();
                //ensureBitsAvailable(item, bitcount);

                int packetStart = getPaddingStartValue(); 
                /*try
                {
                    // use the same packet rather then a new one
                    parseBlock(packet, packetDecl);
                }
                catch (PDLException pdle)
                {
                    throw new PDLException(pdle, packetRef);
                }
                padding(packetDecl, packetStart);*/
                
                PacketState state = new PacketState(packet, packetRef, packetDecl, null, packetStart);
                insertState(state);

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
                /*try
                {
                    parseBlock(context2, packetDecl);
                }
                catch (PDLException pdle)
                {
                    throw new PDLException(pdle, packetRef);
                }*/

                PacketState state = new PacketState(context2, packetRef, packetDecl, packet, packetStart);
                insertState(state);
/*
                packet.setPacket(packetRef.getBinding(), context2);
                
                padding(packetDecl, packetStart);*/

                break;
            }
            
            case PacketRefList:
            {
                addReserved(-getMinSize(item));
                
                PDLPacketRef packetRefList = item.asPacketRef();
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

                            parseBlock(context2, packetDecl, 0);
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

                    insertBlock(packet, conditional);
                    //parseBlock(packet, conditional);
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
                
                RestorableParseState state = new RestorableParseState(packet);
                addReserved(+getMinSize(item));
                try
                {
                    parseBlock(packet, optional, 0);

                    // try to parse remaining items in this block 
                    parseBlock(packet, block, index+1// start at next index
                            );
                    
                    // try to parse the tails
                    
                    
                    /*
                    if (input.getPosition()<input.getSize())
                        throw new PDLException(item, "parsing message incomplete in this branch "
                                +" input(position:"+input.getPosition()+","+input.getSize()+")");
                    */
                    // remaining message successfully parsed
                    return true;
                }
                catch (PDLException pdle)
                {
                    state.restore();
                    if (DEBUG) println("optional failed: "+toString(pdle));
                    // continue parsing
                }
                break;
            }
            case Choice:
            {
                addReserved(-getMinSize(item));
                
                PDLChoice mchoice = item.asChoice();
                
                ensureBitsAvailable(mchoice, getMinSize(mchoice));

                RestorableParseState state = new RestorableParseState(packet);                
                for (PDLBlock choice: mchoice)
                {
                    if (isAvailable(getMinSize(choice)))
                    {
                        addReserved(+getMinSize(choice));                    
                        try
                        {
                            parseBlock(packet, choice, 0);

                            // try to parse remaining items in this block
                            parseBlock(packet, block, index+1// start at next index
                            );
                            // accepted
                            return true;
                        }
                        catch (PDLException pdle)
                        {
                            state.restore();
                            if (DEBUG) println("not chosen ("+choice+"), reason:"+toString(pdle));
                        }
                    }
                }
                
                throw new PDLException(mchoice, "no elements were chosen");
                
            }
            case Block:
            {
                PDLBlock b = item.asBlock();
                insertBlock(packet, b);
                //parseBlock(packet, b);
                break;
            }
            default:
            {
                throw new InternalError("unsupported item: "+item.getType());
            }
        }
        
        return false;
    }
    
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
    
    private int computeChecksum(PDLVariable item, PDLParseContext context) throws PDLException
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

    private void checksum(PDLVariable item, PDLParseContext context, final int value)
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
