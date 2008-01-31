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

import net.sf.nmedit.jpdl2.PDLFunction;
import net.sf.nmedit.jpdl2.PDLImplicitVariable;
import net.sf.nmedit.jpdl2.PDLItemType;
import net.sf.nmedit.jpdl2.PDLVariable;

public class PDLImplicitVariableImpl extends PDLVariableImpl implements
        PDLImplicitVariable
{
    
    private PDLFunction function;
    
    public PDLImplicitVariableImpl(PDLVariable variable, PDLFunction function)
    {
        this(variable.getName(), variable.getSize(), function);
    }

    public PDLImplicitVariableImpl(String name, int size, PDLFunction function)
    {
        super(name, size);
        this.function = function;
    }
    
    public PDLImplicitVariableImpl(String name, int size)
    {
        this(name, size, null);
    }

    public PDLItemType getType()
    {
        return PDLItemType.ImplicitVariable;
    }
    
    public PDLFunction getFunction()
    {
        return function;
    }

}
