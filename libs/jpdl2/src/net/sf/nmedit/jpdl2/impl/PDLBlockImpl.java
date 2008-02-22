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
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;

public class PDLBlockImpl extends PDLItemImpl implements PDLBlock
{
    
    private List<PDLItem> items;
    private int minimumSize = -1;
    private int minimumCount = -1;

    public PDLBlockImpl()
    {
        this.items = new ArrayList<PDLItem>();
    }
    
    public void add(PDLItem item)
    {
        items.add(item);
        this.minimumSize = -1;
        this.minimumCount = -1;
    }

    public PDLItem getItem(int index)
    {
        return items.get(index);
    }

    public int getItemCount()
    {
        return items.size();
    }

    public Iterator<PDLItem> iterator()
    {
        return items.iterator();
    }

    public int getMinimumCount()
    {
        if (minimumCount < 0)
        {
            minimumCount = 0;
            for (PDLItem item: items)
            {
                switch (item.getType())
                {
                    case Conditional:
                        break;
                    case Optional:
                        break;
                    default:
                        minimumCount+=item.getMinimumCount();
                        break;
                }
            }
        }
        return minimumCount;
    }

    public int getMinimumSize()
    {
        if (minimumSize < 0)
        {
            minimumSize = 0;
            for (PDLItem item: items)
            {
                switch (item.getType())
                {
                    case Conditional:
                        break;
                    case Optional:
                        break;
                    default:
                        minimumSize+=item.getMinimumSize();
                        break;
                }
            }
        }
        return minimumSize;
    }

    public PDLBlock asBlock()
    {
        return this;
    }

    public PDLItemType getType()
    {
        return PDLItemType.Block;
    }

}
