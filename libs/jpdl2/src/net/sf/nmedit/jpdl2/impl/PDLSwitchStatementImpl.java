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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLCaseStatement;
import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLSwitchStatement;
import net.sf.nmedit.jpdl2.format.Expression;

public class PDLSwitchStatementImpl extends PDLItemImpl implements PDLSwitchStatement
{
    
    private List<PDLCaseStatement> elements = new ArrayList<PDLCaseStatement>(2);
    private List<PDLCaseStatement> publicCollection = Collections.unmodifiableList(elements);
    private Map<Integer, PDLCaseStatement> caseList = new HashMap<Integer, PDLCaseStatement>();
    private PDLCaseStatement defaultCase = null;
    
    private int minimumCount = -1;
    private int minimumSize = -1;
    private PDLFunction function;
    
    public PDLSwitchStatementImpl(Expression e)
    {
        this(new PDLFunctionImpl(e));
    }
    
    public PDLSwitchStatementImpl(PDLFunction function)
    {
        this.function = function;
        if (function == null)
            throw new NullPointerException();
    }
    
    public PDLFunction getFunction()
    {
        return function;
    }

    public void add(PDLCaseStatement c)
    {
        if (c == null)
            throw new NullPointerException("element can not be null");

        this.minimumCount = -1;
        this.minimumSize = -1;
        
        if (c.isDefaultCase())
        {
            for (PDLCaseStatement c2: elements)
                if (c2.isDefaultCase())
                    throw new IllegalArgumentException("multiple default cases");
            
            this.defaultCase = c;
        }
        else
        {
            // not default case
            caseList.put(c.getValue(), c);
        }

        // ensure order, default is always at the end, other elements are ordered by value
        for (int i=0;i<elements.size();i++)
        {
            if (elements.get(i).isDefaultCase())
            {
                elements.add(i, c);
                return;
            }
            if (elements.get(i).getValue()<c.getValue())
            {
                elements.add(i+1, c);
                return;
            }
        }
        elements.add(c);
    }

    public Iterator<PDLCaseStatement> iterator()
    {
        return publicCollection.iterator();
    }
    
    public List<PDLCaseStatement> getItems()
    {
        return publicCollection;
    }

    public int getMinimumCount()
    {
        if (defaultCase == null)
            return 0;
        
        if (minimumCount<0)
        {
            int items = 0;
            minimumCount = Integer.MAX_VALUE;
            for (PDLCaseStatement casestmt: elements)
            {
                PDLItem item = casestmt.getBlock();
                switch(item.getType())
                {
                    case Optional: minimumCount = 0; break;
                    case Conditional: minimumCount = 0; break;
                    default:
                    {
                        items++;
                        minimumCount = Math.min(minimumCount, item.getMinimumCount());
                        break;
                    }
                }
                if (minimumCount == 0)
                    break;
            }
            
            if (items == 0)
                minimumCount = 0;
        }
        return minimumCount;
    }

    public int getMinimumSize()
    {
        if (defaultCase == null)
            return 0;
        
        if (minimumSize<0)
        {
            int items = 0;
            minimumSize = Integer.MAX_VALUE;
            for (PDLCaseStatement casestmt: elements)
            {
                PDLItem item = casestmt.getBlock();
                switch(item.getType())
                {
                    case Optional: minimumSize = 0; break;
                    case Conditional: minimumSize = 0; break;
                    case Fail: break; // invalid path
                    default:
                    {
                        items++;
                        minimumSize = Math.min(minimumSize, item.getMinimumSize());
                        break;
                    }
                }
                if (minimumSize == 0)
                    break;
            }
            
            if (items == 0)
                minimumSize = 0;
        }
        
        return minimumSize;
    }

    public PDLItemType getType()
    {
        return PDLItemType.SwitchStatement;
    }

    public PDLSwitchStatement asSwitchStatement()
    {
        return this;
    }
    
    public String toString()
    {
        return "switch ("+function+")";
    }

    public PDLBlock getItemForCase(int value)
    {
        PDLCaseStatement cs = caseList.get(value);
        if (cs != null) return cs.getBlock();
        if (defaultCase != null) return defaultCase.getBlock();
        return null;
    }
    
}
