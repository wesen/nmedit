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

import net.sf.nmedit.jpdl2.dom.PDLConstant;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLConstantImpl extends PDLItemImpl implements PDLConstant
{
    
    private int value;
    private int size;
    private PDLMultiplicity multiplicity;

    public PDLConstantImpl(int value, int size)
    {
        this.value = value;
        this.size = size;
    }

    public int getSize()
    {
        return size;
    }

    public int getValue()
    {
        return value;
    }

    public PDLItemType getType()
    {
        return PDLItemType.Constant;
    }

    public PDLMultiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public void setMultiplicity(PDLMultiplicity multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    public int getMinimumSize()
    {
        return PDLUtils.getMinMultiplicity(multiplicity) * size;
    }

    public int getMinimumCount()
    {
        return 0;
    }
    
    public String toString()
    {
        return (multiplicity != null ? (String.valueOf(multiplicity) + "*") : "") + value+":"+size;
    }

    public PDLConstant asConstant()
    {
        return this;
    }

}
