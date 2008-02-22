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

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLChoice;
import net.sf.nmedit.jpdl2.dom.PDLConditional;
import net.sf.nmedit.jpdl2.dom.PDLConstant;
import net.sf.nmedit.jpdl2.dom.PDLInstruction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLOptional;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;
import net.sf.nmedit.jpdl2.dom.PDLSwitchStatement;
import net.sf.nmedit.jpdl2.dom.PDLVariable;

public abstract class PDLItemImpl implements PDLItem
{
    
    public PDLItemImpl()
    {
        super();
    }
    
    public PDLConstant asConstant()
    {
        return PDLConstant.class.cast(this);
    }

    public PDLVariable asVariable()
    {
        return PDLVariable.class.cast(this);
    }

    public PDLPacketRef asPacketRef()
    {
        return PDLPacketRef.class.cast(this);
    }

    public PDLBlock asBlock()
    {
        return PDLBlock.class.cast(this);
    }

    public PDLConditional asConditional()
    {
        return PDLConditional.class.cast(this);
    }

    public PDLOptional asOptional()
    {
        return PDLOptional.class.cast(this);
    }

    public PDLChoice asChoice()
    {
        return PDLChoice.class.cast(this);
    }

    public PDLInstruction asInstruction()
    {
        return PDLInstruction.class.cast(this);
    }

    public PDLSwitchStatement asSwitchStatement()
    {
        return PDLSwitchStatement.class.cast(this);
    }

}
