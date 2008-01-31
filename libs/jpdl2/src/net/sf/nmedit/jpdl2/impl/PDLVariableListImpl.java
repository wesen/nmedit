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
import net.sf.nmedit.jpdl2.PDLMultiplicity;
import net.sf.nmedit.jpdl2.PDLUtils;
import net.sf.nmedit.jpdl2.PDLVariable;
import net.sf.nmedit.jpdl2.PDLVariableList;

public class PDLVariableListImpl extends PDLItemImpl implements PDLVariableList
{
    private PDLMultiplicity multiplicity;
    private PDLVariable variable;
    private int terminal;
    private boolean hasTerminal = false; 

    public PDLVariableListImpl(PDLVariable variable, PDLMultiplicity multiplicity)
    {
        this.variable = variable;
        this.multiplicity = multiplicity;
    }

    public PDLVariableListImpl(String name, int size, PDLMultiplicity multiplicity)
    {
        this(new PDLVariableImpl(name, size), multiplicity);
    }

    public PDLMultiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public PDLVariableList asVariableList()
    {
        return this;
    }

    public PDLItemType getType()
    {
        return PDLItemType.VariableList;
    }

    public PDLVariable getVariable()
    {
        return variable;
    }

    public String getName()
    {
        return variable.getName();
    }

    public int getSize()
    {
        return variable.getSize();
    }

    public int getMinimumSize()
    {
        return PDLUtils.getMinMultiplicity(multiplicity) * variable.getMinimumSize();
    }

    public String toString()
    {
        return String.valueOf(multiplicity)+"*"+String.valueOf(variable);
    }

    public void setTerminal(Integer terminal)
    {
        if (terminal != null)
        {
            this.terminal = terminal;
            this.hasTerminal = true;
        }
        else
        {
            this.terminal = -1;
            this.hasTerminal = false;
        }
    }
    
    public boolean hasTerminal()
    {
        return hasTerminal;
    }
    
    public int getTerminal()
    {
        return terminal;
    }
    
}
