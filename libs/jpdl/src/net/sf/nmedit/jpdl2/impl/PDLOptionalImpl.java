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

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpdl2.PDLItem;
import net.sf.nmedit.jpdl2.PDLItemType;
import net.sf.nmedit.jpdl2.PDLOptional;

public class PDLOptionalImpl extends PDLItemImpl implements PDLOptional
{
    
    private PDLItem item;

    public PDLOptionalImpl()
    {
        super();
    }

    public void add(PDLItem item)
    {
        if (this.item != null)
            throw new IllegalStateException("can only add 1 element");
        
        this.item = item;
    }
    
    public PDLItem getItem()
    {
        return item;
    }

    public PDLItemType getType()
    {
        return PDLItemType.Optional;
    }

    public PDLItem getItem(int index)
    {
        if (index != 0)
            throw new IndexOutOfBoundsException("invalid index: "+index);
        return item;
    }

    public int getItemCount()
    {
        return 1;
    }

    public int getMinimumSize()
    {
        return 0;
    }

    public Iterator<PDLItem> iterator()
    {
        return new Iterator<PDLItem>()
        {
            
            PDLItem next = PDLOptionalImpl.this.item;

            public boolean hasNext()
            {
                return next != null;
            }

            public PDLItem next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                PDLItem result = next;
                next = null;
                return result;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
        };
    }

}
