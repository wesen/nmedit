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
package net.sf.nmedit.jpdl2.test;

import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.format.Expression;
import net.sf.nmedit.jpdl2.format.Opcodes;
import net.sf.nmedit.jpdl2.impl.PDLBlockImpl;
import net.sf.nmedit.jpdl2.impl.PDLCompiledCondition;
import net.sf.nmedit.jpdl2.impl.PDLConditionalImpl;
import net.sf.nmedit.jpdl2.impl.PDLConstantImpl;
import net.sf.nmedit.jpdl2.impl.PDLFunctionImpl;
import net.sf.nmedit.jpdl2.impl.PDLInstructionImpl;
import net.sf.nmedit.jpdl2.impl.PDLMultiplicityImpl;
import net.sf.nmedit.jpdl2.impl.PDLChoiceImpl;
import net.sf.nmedit.jpdl2.impl.PDLOptionalImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketRefImpl;
import net.sf.nmedit.jpdl2.impl.PDLSwitchStatementImpl;
import net.sf.nmedit.jpdl2.impl.PDLVariableImpl;
import net.sf.nmedit.jpdl2.PDLException;

import org.junit.Test;

public class PDLItemTests
{

    public PDLItem getInstance(PDLItemType type)
    {
        switch (type)
        {
            case Label:
                return new PDLInstructionImpl(PDLItemType.Label, "label");
            case Constant:
                return new PDLConstantImpl(0,0);
            case Variable:
                return PDLVariableImpl.create("name",1);
            case VariableList:
                return PDLVariableImpl.createVariableList("name",1, new PDLMultiplicityImpl(2));
            case Optional:
                return new PDLOptionalImpl();
            case PacketRef:
                return new PDLPacketRefImpl(null, "name", "binding", false);
            case InlinePacketRef:
                return new PDLPacketRefImpl(null, "name", "binding", true);
            case PacketRefList:
                return new PDLPacketRefImpl(null, new PDLPacketRefImpl(null, "name", "binding", false), new PDLMultiplicityImpl(2));
            case Conditional:
                return new PDLConditionalImpl(new PDLCompiledCondition(new Expression(Opcodes.bpush,true)));
            case ImplicitVariable:
                return PDLVariableImpl.createImplicit("name",1, new PDLFunctionImpl(new Expression(Opcodes.vpush, "v")), false);
            case AnonymousVariable:
                return PDLVariableImpl.createImplicit("name",1, new PDLFunctionImpl(new Expression(Opcodes.vpush, "v")), true);
            case MessageId:
                return new PDLInstructionImpl(PDLItemType.MessageId, "messageid");
            case Block:
                return new PDLBlockImpl();
            case StringDef:
                return new PDLInstructionImpl(PDLItemType.StringDef, "name", "value");
            case SwitchStatement:
                return new PDLSwitchStatementImpl(new Expression(Opcodes.ipush, 0));
            case Choice:
                return new PDLChoiceImpl(new PDLConstantImpl(0,0), new PDLConstantImpl(0,0));
            case Fail:
                return new PDLInstructionImpl(PDLItemType.Fail);
            default:
                throw new InternalError("unknown type: "+type);
        }
    }
    
    @Test
    public void createExceptionTest()
    {
        for (PDLItemType type: PDLItemType.values())
        {
            PDLItem instance = getInstance(type);
            new PDLException(instance, "some message");
        }
    }
    
    @Test
    public void oneInstanceForEachItemType() throws Exception
    {
        for (PDLItemType type: PDLItemType.values())
        {
            PDLItem instance = getInstance(type);
            if (instance == null)
                throw new Exception("no instance for "+type);
        }
    }
    
}
