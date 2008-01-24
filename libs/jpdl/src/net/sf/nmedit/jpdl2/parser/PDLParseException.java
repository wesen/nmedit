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

import net.sf.nmedit.jpdl2.PDLConstant;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLFunctionRef;
import net.sf.nmedit.jpdl2.PDLImplicitVariable;
import net.sf.nmedit.jpdl2.PDLItem;
import net.sf.nmedit.jpdl2.PDLPacketDecl;
import net.sf.nmedit.jpdl2.PDLPacketRef;
import net.sf.nmedit.jpdl2.PDLVariable;

public class PDLParseException extends PDLException
{

    /**
     * 
     */
    private static final long serialVersionUID = -3144817878163587872L;

    public PDLParseException(String message)
    {
        super(message);
    }
    
    public PDLParseException(PDLException e)
    {
        super(e);
    }

    public PDLParseException(PDLItem item, String message)
    {
        super(toString(item)+": "+message);
    }

    public PDLParseException(PDLException parent, PDLPacketDecl packet)
    {
        super("packet "+packet.getName(), parent);
    }

    public PDLParseException(PDLException parent, PDLItem item)
    {
        super(toString(item), parent);
    }
    
    private static String toString(PDLItem item)
    {
        switch (item.getType())
        {
            case Callback:
                return "callback '"+item.asCallback().getMethodName()+"'";
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
                PDLFunctionRef f = variable.getFunctionRef();
                return variable.getName()+":"+variable.getSize()
                +"="+f.getFunctionName()+"(@"+f.getStartLabel()+",@"+f.getEndLabel()+")";
            }
            default:
                throw new InternalError("unknown item: "+item);
        }
    }

}
