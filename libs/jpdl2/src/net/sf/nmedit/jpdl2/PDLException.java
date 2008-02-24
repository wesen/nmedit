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

import java.util.Iterator;

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLChoice;
import net.sf.nmedit.jpdl2.dom.PDLConstant;
import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;
import net.sf.nmedit.jpdl2.dom.PDLVariable;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1441057047102733845L;
    private Object item;
    
    public Object getItem()
    {
        return item;
    }
    
    public PDLException()
    {
        super();
    }

    public PDLException(Throwable cause)
    {
        super(cause);
    }

    public PDLException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PDLException(String message)
    {
        super(message);
    }
    
    public PDLException(PDLException e)
    {
        super(e);
    }

    public PDLException(PDLItem item, String message)
    {
        super(toString(item)+": "+message);
        this.item = item;
    }

    public PDLException(PDLException parent, PDLPacketDecl packet)
    {
        super("packet "+packet.getName(), parent);
        this.item = packet;
    }

    public PDLException(String message, PDLPacketDecl packet)
    {
        super("packet "+packet.getName()+"\n"+message);
        this.item = packet;
    }

    public PDLException(PDLException parent, PDLItem item)
    {
        super(toString(item), parent);
        this.item = item;
    }
    
    private static String toString(PDLItem item)
    {
        switch (item.getType())
        {
            case MessageId:
                return "messageid '"+item.asInstruction().getString()+"'";
            case Conditional:
                return "condition "+String.valueOf(item.asConditional().getCondition());
            case Constant:
            {
                PDLConstant constant = item.asConstant();
                // TODO multiplicity
                return "constant "+constant.getValue() + ":" + constant.getSize();
            }
            case Label:
                return "@"+item.asInstruction().getString();
            case Optional:
                return "Optional";
            case StringDef:
                return String.valueOf(item);
            case InlinePacketRef:
            {
                PDLPacketRef packetRef = item.asPacketRef();
                return packetRef.getPacketName()+"$$";
            }
            case PacketRef:
            {
                PDLPacketRef packetRef = item.asPacketRef();
                return packetRef.getPacketName()+"$"+packetRef.getBinding();
            }
            case PacketRefList:
            {
                PDLPacketRef packetRef = item.asPacketRef();
                return packetRef.getMultiplicity()+"*"+packetRef.getPacketName()+"$"+packetRef.getBinding();
            }
            case Variable:
            {
                PDLVariable variable = item.asVariable();
                return variable.getName()+":"+variable.getSize();
            }
            case VariableList:
            {
                PDLVariable variable = item.asVariable();
                return "List "+variable.getName()+":"+variable.getSize();                
            }
            case ImplicitVariable:
            {
                PDLVariable variable = item.asVariable();
                PDLFunction f = variable.getFunction();
                return variable.getName()+":"+variable.getSize()+"="+f;
            }
            case AnonymousVariable:
            {
                PDLVariable variable = item.asVariable();
                PDLFunction f = variable.getFunction();
                return "%"+variable.getName()+":"+variable.getSize()+"="+f;
            }
            case Choice:
            {
                StringBuilder sb = new StringBuilder();
                sb.append('(');
                PDLChoice m = item.asChoice();
                Iterator<PDLBlock> iter = m.getItems().iterator();
                
                sb.append(toString(iter.next()));
                while(iter.hasNext())
                {
                    sb.append(" | ");
                    sb.append(toString(iter.next()));
                }
                
                sb.append(')');
                return sb.toString();
            }
            case Block:
            {
                PDLBlock block = item.asBlock();
                if (block.getItemCount() == 1)
                    return toString(block.getItem(0));
                
                StringBuilder sb = new StringBuilder();
                sb.append('{');
                for (PDLItem i: block)
                    sb.append(" "+toString(i));
                sb.append('}');
                return sb.toString();
            }
            case Fail:
            {
                return "fail";
            }
            case SwitchStatement:
            {
                return "switch("+item.asSwitchStatement().getFunction()+")";
            }
            default:
                PDLUtils.unknownItemTypeError(item);
                return null; // never reacheds
        }
    }

}
