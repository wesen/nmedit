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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.PParameter;

public class TransformTool
{

    private PTTransformations transformations;
    private PModuleDescriptor source;
    private transient List<PTModule> targets;
    
    public TransformTool(PTTransformations transformations, 
            PModuleDescriptor source)
    {
        this.source = source;
        this.transformations = transformations;
    }

    public PTTransformations getTransformations()
    {
        return transformations;
    }

    public PModuleDescriptor getSourceDescriptor()
    {
        return source;
    }

    public Collection<PTGroup> getTargetPTGroups()
    {
        return transformations.getTargets(source);
    }

    public List<PTModule> getTargets()
    {
        if (targets == null)
        {
            List<PTModule> list =
                new LinkedList<PTModule>();
            
            for (PTGroup g: getTargetPTGroups())
            {
                if (g.containsTarget(source))
                {
                    for (PTModule m: g)
                    {
                        list.add(m);
                    }   
                }
            }
        
            targets = Collections.unmodifiableList(list);
        }
        return targets;
    }
    
    private PTModule getSourceTransformable(PTModule target, PModule source)
    {
        List<PTModule> list = getTargets();
        
        if (!list.contains(target))
            throw new IllegalArgumentException("invalid target: "+target);
        
        PTGroup g = target.getGroup();
        
        return g.getTransformable(source.getDescriptor());
    }

    public PModule transform(PTModule target, PModule source)
    {
        PTModule sourceTransformable = getSourceTransformable(target, source);
        return transform(target, sourceTransformable, source);
    }

    public void transformReplace(PTModule targetTransformable, PModule source)
    {
        History history = source.getPatch().getHistory();
        
        if (history != null)
            history.beginRecord();
        try
        {
            PTModule sourceTransformable = getSourceTransformable(targetTransformable, source);
            
            PModule target = transform(targetTransformable, sourceTransformable, source);
            
            List<Pair> list = listConnections(source);
            
            PModuleContainer mc = source.getParentComponent();
            
            mc.remove(source);
            
            mc.add(target);
            
            buildConnections(targetTransformable, target, sourceTransformable, list);
        }
        finally
        {
            if (history != null)
                history.endRecord();
        }
    }
    
    private void buildConnections(PTModule targetTransformable, PModule target, PTModule sourceTransformable, List<Pair> list)
    {
        PConnectionManager cm = target.getParentComponent().getConnectionManager();
        
        if (cm == null) return;
        
        Iterator<PTTransformable<PConnectorDescriptor>> iter = targetTransformable.connectors();
        
        while (iter.hasNext())
        {
            PTTransformable<PConnectorDescriptor> tcTarget = iter.next();       
            PTTransformable<PConnectorDescriptor> tcSource = sourceTransformable.getConnectorByName(tcTarget.getName());

            if (tcSource != null)
            {
                for (Pair p: list)
                {
                    if (tcSource.getTarget() == p.d)
                    {
                        PConnector a = p.c;
                        if (a != null)
                        {
                            PConnector b = target.getConnector(tcTarget.getTarget());
                            cm.add(a, b);
                        }
                        else
                        {
                            // connector a is in the target module
                            // todo
                        }
                    }
                }       
            }            
        }
    }

    private List<Pair> listConnections(PModule source)
    {
        PConnectionManager cm = source.getParentComponent().getConnectionManager();
        
        List<Pair> list = new LinkedList<Pair>();
        
        if (cm != null)
        {
            for (PConnection con: cm.connections(source))
            {
                PConnector c;
                PConnectorDescriptor d;
                if (con.getA() == source)
                {
                    d = con.getA().getDescriptor();
                    c = con.getB();
                }
                else
                {
                    d = con.getA().getDescriptor();
                    c = con.getB();
                }
                
                if (c.getParentComponent() == source)
                {
                    //list.add(new Pair(c.getDescriptor(), d));
                    System.err.println("connections on the same module not implemented yet");
                }
                else
                {
                    list.add(new Pair(c, d));
                }
                cm.remove(con);
            }
        }
        return list;
    }
    
    private static class Pair
    {
        PConnector c;
        PConnectorDescriptor cd;
        PConnectorDescriptor d;
        
        Pair(PConnectorDescriptor cd, PConnectorDescriptor d)
        {
            this.c = null;
            this.cd = cd;
            this.d = d;
        }
        
        Pair(PConnector c, PConnectorDescriptor d)
        {
            this.c = c;
            this.cd = c.getDescriptor();
            this.d = d;
        }
    }

    private PModule transform(PTModule targetTransformable, 
            PTModule sourceTransformable, PModule source)
    {
        PModule target = source.getParentComponent().createModule(targetTransformable.getTarget());
        
        target.setTitle(source.getTitle());
        target.setScreenLocation(source.getScreenLocation());
        
        Iterator<PTTransformable<PParameterDescriptor>> iter = targetTransformable.parameters();
        
        while (iter.hasNext())
        {
            PTTransformable<PParameterDescriptor> tpTarget = iter.next();       
            PTTransformable<PParameterDescriptor> tpSource = sourceTransformable.getParameterByName(tpTarget.getName());
            
            if (tpSource != null)
                transform(target, tpTarget.getTarget(), source, tpSource.getTarget());
        }
        
        return target;
    }

    private void transform(PModule target, PParameterDescriptor pdTarget, 
            PModule source, PParameterDescriptor pdSource)
    {
        PParameter pTarget = target.getParameter(pdTarget);
        PParameter pSource = source.getParameter(pdSource);
        pTarget.setFloatValue(pSource.getFloatValue());
    }

}
