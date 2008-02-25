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
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLChoice;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;

public class PDLChoiceImpl extends PDLItemImpl implements PDLChoice
{
    
    private List<PDLBlock> elements = new ArrayList<PDLBlock>(2);
    private List<PDLBlock> publicCollection = Collections.unmodifiableList(elements);
    private int minimumCount = -1;
    private int minimumSize = -1;
    
    public PDLChoiceImpl()
    {
        super();
    }

    private PDLBlock blockItem(PDLItem o)
    {
        PDLBlock block = new PDLBlockImpl();
        block.add(o);
        return block;
    }
    
    public PDLChoiceImpl(PDLItem a, PDLItem b)
    {
        if (a == null || b ==null)
            throw new NullPointerException("element can not be null");
        elements.add(blockItem(a));
        elements.add(blockItem(b));
    }
    
    public void add(PDLItem i)
    {
        if (i == null)
            throw new NullPointerException("element can not be null");
        elements.add(blockItem(i));
        this.minimumCount = -1;
        this.minimumSize = -1;
    }

    public Iterator<PDLBlock> iterator()
    {
        return publicCollection.iterator();
    }
    
    public List<PDLBlock> getItems()
    {
        return publicCollection;
    }

    public int getMinimumCount()
    {
        if (minimumCount<0)
        {
            int items = 0;
            minimumCount = Integer.MAX_VALUE;
            boolean oneItem = false;
            for (PDLItem item: elements)
            {
                switch(item.getType())
                {
                    case Optional: minimumCount = 0; break;
                    case Conditional: minimumCount = 0; break;
                    default:
                    {
                        items++;
                        minimumCount = Math.min(minimumCount, item.getMinimumCount());
                        oneItem = true;
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
        if (minimumSize<0)
        {
            int items = 0;
            minimumSize = Integer.MAX_VALUE;
            for (PDLItem item: elements)
            {
                switch(item.getType())
                {
                    case Optional: minimumSize = 0; break;
                    case Conditional:minimumSize = 0; break;
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
        return PDLItemType.Choice;
    }

    public PDLChoice asChoice()
    {
        return this;
    }
    
    public String toString()
    {
        return PDLChoice.class.getSimpleName()+" (itemcount: "+elements.size()+")";
    }
    
}
