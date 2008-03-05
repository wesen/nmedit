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

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;

/**
 * Implements {@link PTTransformations}.
 * 
 * @author Christian Schneider
 */
public class PTBasicTransformations implements PTTransformations
{

    /**
     * Empty list.
     */
    private static final List<PTModuleSelector> EMPTY = Collections.<PTModuleSelector>emptyList();

    /**
     * Empty array.
     */
    private static final PParameterDescriptor[] EMPTYP = new PParameterDescriptor[0];

    /**
     * Empty array.
     */
    private static final PConnectorDescriptor[] EMPTYC = new PConnectorDescriptor[0];

    /**
     * Sorts the specified array. The elements are ordered
     * descending by the {@link PTModuleMapping#getCovering()} value.
     * @param mappings the elements to sort
     */
    public static void sort(PTModuleMapping[] mappings)
    {
        Arrays.sort(mappings, new MappingOrder());
    }

    /**
     * Compares two mappings and orders them
     * descending by the {@link PTModuleMapping#getCovering()} value.
     */
    private static class MappingOrder implements Comparator<PTModuleMapping>, Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 6633285776526783275L;

        /**
         * Compares two mappings and orders them
         * descending by the {@link PTModuleMapping#getCovering()} value.
         */
        public int compare(PTModuleMapping o1, PTModuleMapping o2)
        {
            double c1 = o1.getCovering();
            double c2 = o2.getCovering();
            return (int) Math.signum(c2-c1);
        }

        private void writeObject(java.io.ObjectOutputStream out)
            throws IOException
        {
            out.defaultWriteObject();
        }
        
        private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException
        {
            in.defaultReadObject();
        }
    }
    
    private PTModuleSelector[] selectors;
    
    private SoftReference<Map<Object, List<PTModuleSelector>>> mapRef;

    public PTBasicTransformations(PTModuleSelector[] selectors)
    {
        this.selectors = selectors;
    }
    
    private Map<Object, List<PTModuleSelector>> getMap()
    {
        Map<Object, List<PTModuleSelector>> map = mapRef == null ? null : mapRef.get();
        if (map == null)
        {
            map = new HashMap<Object, List<PTModuleSelector>>(selectors.length);
            mapRef = new SoftReference<Map<Object,List<PTModuleSelector>>>(map);
        }
        return map;
    }
    
    private List<PTModuleSelector> getGraph(PModuleDescriptor module)
    {
        Map<Object, List<PTModuleSelector>> map = getMap();
        List<PTModuleSelector> list = map.get(module.getComponentId());
        if (list != null) return list;
        
        PTModuleSelector selector = null;
        
        for (int i=selectors.length-1;i>=0;i--)
        {
            PTModuleSelector s = selectors[i];
            if (s.getDescriptor().getComponentId().equals(module.getComponentId()))
            {
                selector = s;
                break;
            }
        }
        
        if (selector == null)
        {
            map.put(module.getComponentId(), EMPTY);
            return EMPTY;
        }
        
        list = new ArrayList<PTModuleSelector>();
        
        for (PTModuleSelector m: selectors)
            if (m.intersects(selector))
                list.add(m);
            
        if (list.size() <= 1)
            list = EMPTY;
        map.put(module.getComponentId(), list);
        return list;
    }
    
    private PTModuleMapping getMapping(List<PTModuleSelector> g, PModuleDescriptor src, PModuleDescriptor dst)
    {
        // find the source/target selector
        
        PTModuleSelector ssrc = null;
        PTModuleSelector sdst = null;
        for (PTModuleSelector s: g)
        {
            Object id = s.getDescriptor().getComponentId(); 
            if (src.getComponentId().equals(id))
            {
                ssrc = s;
                if (sdst != null) break;
            } 
            else if (dst.getComponentId().equals(id))
            {
                sdst = s;
                if (ssrc != null) break;
            }
        }
        
        if (ssrc == null || sdst == null) 
            return null;
        
        // now collect matching pairs
        List<PParameterDescriptor> plist = null;
        List<PConnectorDescriptor> clist = null;
        
        
        for (PTSelector a: ssrc)
        {
            for (PTSelector b: sdst)
            {
                if (a.getSelectorId() == b.getSelectorId())
                {
                    if (a.getType() == PTSelector.CONNECTOR)
                    {
                        if (clist == null)
                            clist = new LinkedList<PConnectorDescriptor>();
                        clist.add(a.getConnector());
                        clist.add(b.getConnector());
                    }
                    else if (a.getType() == PTSelector.PARAMETER)
                    {
                        if (plist == null)
                            plist = new LinkedList<PParameterDescriptor>();
                        
                        plist.add(a.getParameter());
                        plist.add(b.getParameter());
                    }
                }
            }
        }
        
        if (plist == null && clist == null)
            return null;
        
        return new PTModuleMapping(ssrc.getDescriptor(), sdst.getDescriptor(),
                plist == null ? EMPTYP : plist.toArray(new PParameterDescriptor[plist.size()]),
                clist == null ? EMPTYC : clist.toArray(new PConnectorDescriptor[clist.size()])
        );
    }

    public PTModuleMapping getMapping(PModuleDescriptor source,
            PModuleDescriptor destination)
    {
        List<PTModuleSelector> g = getGraph(source);
        if (g != null) return getMapping(g, source, destination);
        return null;
    }

    public PTModuleMapping getMapping(PModule source,
            PModuleDescriptor destination)
    {
        return getMapping(source.getDescriptor(), destination);
    }

    public PTModuleMapping[] getMappings(PModuleDescriptor source)
    {
        List<PTModuleSelector> g = getGraph(source);
        if (g == null || g.isEmpty()) return new PTModuleMapping[0];
        
        List<PTModuleMapping> mappings = new ArrayList<PTModuleMapping>(g.size());
        
        for (PTModuleSelector sel: g)
        {
            if (!sel.getDescriptor().getComponentId().equals(source.getComponentId()))
            {
                PTModuleMapping m = getMapping(g, source, sel.getDescriptor());
                if (m != null)
                    mappings.add(m);
            }
        }
        
        return mappings.toArray(new PTModuleMapping[mappings.size()]);
    }

    public PModuleDescriptor[] getTargets(PModuleDescriptor source)
    {
        List<PTModuleSelector> list = getGraph(source);
        PModuleDescriptor[] res = new PModuleDescriptor[list.size()];
        for (int i=list.size()-1;i>=0;i--)
            res[i] = list.get(i).getDescriptor();
        return res;
    }

}
