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

public class PDLException extends Exception
{

    public PDLException()
    {
        // TODO Auto-generated constructor stub
    }

    public PDLException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public PDLException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
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
    }

    public PDLException(PDLException parent, PDLPacketDecl packet)
    {
        super("packet "+packet.getName(), parent);
    }

    public PDLException(PDLException parent, PDLItem item)
    {
        super(toString(item), parent);
    }
    
    private static String toString(PDLItem item)
    {
        switch (item.getType())
        {
            case MessageId:
                return "messageid '"+item.asMessageId().getMessageId()+"'";
            case Conditional:
                return "condition "+String.valueOf(item.asConditional().getCondition());
            case Constant:
            {
                PDLConstant constant = item.asConstant();
                // TODO multiplicity
                return "constant "+constant.getValue() + ":" + constant.getSize();
            }
            case Label:
                return "@"+item.asLabel().getName();
            case Optional:
                return "Optional";
            case PacketRef:
            {
                PDLPacketRef packetRef = item.asPacketRef();
                return packetRef.getPacketName()+"$"+packetRef.getBinding();
            }
            case PacketRefList:
            {
                PDLPacketRef packetRef = item.asPacketRefList();
                return "List "+packetRef.getPacketName()+"$"+packetRef.getBinding();
            }
            case Variable:
            {
                PDLVariable variable = item.asVariable();
                return variable.getName()+":"+variable.getSize();
            }
            case VariableList:
            {
                PDLVariable variable = item.asVariableList();
                return "List "+variable.getName()+":"+variable.getSize();                
            }
            case ImplicitVariable:
            {
                PDLImplicitVariable variable = item.asImplicitVariable();
                PDLFunction f = variable.getFunction();
                return variable.getName()+":"+variable.getSize()+"="+f;
            }
            default:
                throw new InternalError("unknown item: "+item);
        }
    }

}
