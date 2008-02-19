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

import net.sf.nmedit.jpdl2.PDLItemType;
import net.sf.nmedit.jpdl2.PDLVariable;

public class PDLVariableImpl extends PDLItemImpl implements PDLVariable
{
    
    private String name;
    private int size;
    
    public PDLVariableImpl(String name, int size)
    {
        if (name == null)
            throw new NullPointerException("name must not be null");
        this.name = name;
        this.size = size;
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    public PDLVariable asVariable()
    {
        return this;
    }

    public PDLItemType getType()
    {
        return PDLItemType.Variable;
    }

    public int getMinimumSize()
    {
        return size;
    }
    
    public String toString()
    {
        return name+":"+size;
    }

    public int getMinimumCount()
    {
        return 1;
    }

}
