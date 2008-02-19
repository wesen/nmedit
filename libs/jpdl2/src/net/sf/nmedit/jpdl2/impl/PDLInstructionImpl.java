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
package net.sf.nmedit.jpdl2.impl;

import java.util.EnumSet;

import net.sf.nmedit.jpdl2.PDLInstruction;
import net.sf.nmedit.jpdl2.PDLItemType;

public class PDLInstructionImpl extends PDLItemImpl implements PDLInstruction
{
    
    private static final EnumSet<PDLItemType> types;
    static
    {
        types = EnumSet.noneOf(PDLItemType.class);
        types.add(PDLItemType.MessageId);
        types.add(PDLItemType.Fail);
    }

    private PDLItemType type;
    private String string;

    public PDLInstructionImpl(PDLItemType type)
    {
        this(type, null);
    }
    
    public PDLInstructionImpl(PDLItemType type, String stringValue)
    {
        this.type = type;
        this.string = stringValue;
        
        if (!types.contains(type))
            throw new IllegalArgumentException("invalid type: "+type);
        if (type == PDLItemType.MessageId)
        {
            if (stringValue == null) throw new IllegalArgumentException("type "+type+" requires string argument");
        }
        else
        {
            if (stringValue != null) throw new IllegalArgumentException("type "+type+" has no string argument");
        }
    }
    
    public int getMinimumCount()
    {
        return 0;
    }

    public int getMinimumSize()
    {
        return 0;
    }

    public PDLItemType getType()
    {
        return type;
    }

    public String getString()
    {
        return string;
    }

    public String toString()
    {
        switch (type)
        {
            case MessageId:
                return "messageId(\""+string+"\")";
            case Fail:
                return "fail";
            default:
                throw new InternalError("unsupported type: "+type);
        }
    }
    
}
