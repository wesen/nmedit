/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jpatch.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;

/**
 * Selects a module and some of its parameters and connectors.
 * 
 * @author Christian Schneider
 */
public class PTModuleSelector implements Iterable<PTSelector>
{
    
    /**
     * the selected module
     */
    private PModuleDescriptor descriptor;

    /**
     * the selected parameters
     */
    private Map<Integer, PTSelector> selectorMap;

    /**
     * Selects the specified module.
     * 
     * @author Christian Schneider
     */
    public PTModuleSelector(PModuleDescriptor descriptor)
    {
        this.descriptor = descriptor;
        this.selectorMap = new HashMap<Integer, PTSelector>();
    }
    
    public boolean containsKey(int selectorId)
    {
        return selectorMap.containsKey(selectorId);
    }
    
    public PTSelector getSelector(int selectorId)
    {
        return selectorMap.get(selectorId);
    }
    
    public boolean intersects(PTModuleSelector s)
    {
        for (Integer id: selectorMap.keySet())
            if (s.containsKey(id.intValue()))
                return true;
        return false;
    }
    
    /**
     * Returns the selected parameters
     * @return the selected parameters
     */
    public Iterator<PTSelector> parameters()
    {
        return new SelectorIterator(selectorMap.values().iterator(), PTSelector.PARAMETER);
    }

    /**
     * Returns the selected connectors
     * @return the selected connectors
     */
    public Iterator<PTSelector> connectors()
    {
        return new SelectorIterator(selectorMap.values().iterator(), PTSelector.CONNECTOR);
    }

    /**
     * Returns the selected components
     * @return the selected connectors
     */
    public Iterator<PTSelector> iterator()
    {
        return selectorMap.values().iterator();
    }
    
    private static class SelectorIterator implements Iterator<PTSelector>
    {
        
        private Iterator<PTSelector> iter;
        private int type;
        private PTSelector next;

        public SelectorIterator(Iterator<PTSelector> iter, int type)
        {
            this.iter = iter;
            this.type = type;
        }
        
        private void align()
        {
            while (next==null && iter.hasNext())
            {
                PTSelector s = iter.next();
                if (s.getType() == type)
                    next = s;
            }
        }
        
        public boolean hasNext()
        {
            align();
            return next != null;
        }

        public PTSelector next()
        {
            if (!hasNext())
                throw new NoSuchElementException();
            PTSelector s = next;
            next = null;
            return s;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
    }
    
    public PModuleDescriptor getDescriptor()
    {
        return descriptor;
    }
    
    /**
     * Ensures that the descriptor belongs to the module
     * @param descriptor the verified descriptor
     * @param selector the selector used for the exception message
     * @throws PRuntimeException if the verification failed
     */
    private void checkAdd(PDescriptor descriptor, Object selector)
    {
        if (!(this.descriptor == descriptor || this.descriptor.equals(descriptor.getParentDescriptor())))
            throw new PRuntimeException("invalid selector "+selector);
    }
    
    /**
     * Selects a component in the module.
     * @param selector the selected parameter
     * @throws PRuntimeException if the component does not belong to the selected module
     */
    void add(PTSelector selector)
    {
        checkAdd(selector.getDescriptor(), selector);
        if (selectorMap.containsKey(selector.getSelectorId()))
            throw new PRuntimeException("id already defined");
        selectorMap.put(selector.getSelectorId(), selector);
    }

    void rebuildMap()
    {
        List<PTSelector> remap = null;
        for(Iterator<PTSelector> iter=selectorMap.values().iterator();iter.hasNext();)
        {
            PTSelector s = iter.next();
            if (selectorMap.get(s.getSelectorId())!=s)
            {
                if (remap == null)
                    remap = new ArrayList<PTSelector>(selectorMap.size());
                
                remap.add(s);
                iter.remove();
            }
        }
        if (remap != null)
            for (PTSelector s: remap) add(s);
    }
    
    /**
     * Returns the class name and the module descriptor.
     * @see Object#toString()
     */
    public String toString()
    {
        return getClass().getSimpleName()+"[descriptor="+descriptor+"]";
    }

    public int getSelectorCount()
    {
        return selectorMap.size();
    }
    
}
