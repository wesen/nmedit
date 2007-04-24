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
package net.sf.nmedit.jpatch.transformation.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.transformation.Group;
import net.sf.nmedit.jpatch.transformation.TransformTool;
import net.sf.nmedit.jpatch.transformation.TransformableConnector;
import net.sf.nmedit.jpatch.transformation.TransformableModule;
import net.sf.nmedit.jpatch.transformation.TransformableParameter;
import net.sf.nmedit.jpatch.transformation.Transformations;

public class DefaultTransformationToolImpl implements TransformTool
{

    private Transformations transformations;
    private ModuleDescriptor source;
    private transient List<TransformableModule> targets;
    
    public DefaultTransformationToolImpl(Transformations transformations, 
            ModuleDescriptor source)
    {
        this.source = source;
        this.transformations = transformations;
    }

    public Transformations getTransformations()
    {
        return transformations;
    }

    public ModuleDescriptor getSourceDescriptor()
    {
        return source;
    }

    public Collection<Group> getTargetGroups()
    {
        return transformations.getTargets(source);
    }

    public List<TransformableModule> getTargets()
    {
        if (targets == null)
        {
            List<TransformableModule> list =
                new LinkedList<TransformableModule>();
            
            for (Group g: getTargetGroups())
            {
                if (g.containsTarget(source))
                {
                    for (TransformableModule m: g)
                    {
                        list.add(m);
                    }   
                }
            }
        
            targets = Collections.unmodifiableList(list);
        }
        return targets;
    }
    
    private TransformableModule getSourceTransformable(TransformableModule target, Module source)
    {
        List<TransformableModule> list = getTargets();
        
        if (!list.contains(target))
            throw new IllegalArgumentException("invalid target: "+target);
        
        Group g = target.getGroup();
        
        return g.getTransformable(source.getDescriptor());
    }

    public Module transform(TransformableModule target, Module source)
    {
        TransformableModule sourceTransformable = getSourceTransformable(target, source);
        return transform(target, sourceTransformable, source);
    }

    public void transformReplace(TransformableModule targetTransformable, Module source)
    {
        History history = source.getPatch().getHistory();
        
        if (history != null)
            history.beginRecord();
        try
        {
            TransformableModule sourceTransformable = getSourceTransformable(targetTransformable, source);
            
            Module target = transform(targetTransformable, sourceTransformable, source);
            
            List<Pair> list = listConnections(source);
            
            ModuleContainer mc = source.getParent();
            
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
    
    private void buildConnections(TransformableModule targetTransformable, Module target, TransformableModule sourceTransformable, List<Pair> list)
    {
        ConnectionManager cm = target.getParent().getConnectionManager();
        
        for (int i=targetTransformable.getConnectorCount()-1;i>=0;i--)
        {
            TransformableConnector tcTarget = targetTransformable.getConnector(i);       
            TransformableConnector tcSource = sourceTransformable.getConnectorById(tcTarget.getId());

            if (tcSource != null)
            {
                for (Pair p: list)
                {
                    if (tcSource.getTarget() == p.d)
                    {
                        Connector a = p.c;
                        if (a != null)
                        {
                            Connector b = target.getConnector(tcTarget.getTarget());
                            cm.connect(a, b);
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

    private List<Pair> listConnections(Module source)
    {
        ConnectionManager cm = source.getParent().getConnectionManager();
        
        List<Pair> list = new LinkedList<Pair>();
        
        for (Connection con: cm.getConnections(source))
        {
            Connector c;
            ConnectorDescriptor d;
            if (con.getDestinationModule() == source)
            {
                d = con.getDestination().getDescriptor();
                c = con.getSource();
            }
            else
            {
                d = con.getSource().getDescriptor();
                c = con.getDestination();
            }
            
            if (c.getOwner() == source)
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
        return list;
    }
    
    private static class Pair
    {
        Connector c;
        ConnectorDescriptor cd;
        ConnectorDescriptor d;
        
        Pair(ConnectorDescriptor cd, ConnectorDescriptor d)
        {
            this.c = null;
            this.cd = cd;
            this.d = d;
        }
        
        Pair(Connector c, ConnectorDescriptor d)
        {
            this.c = c;
            this.cd = c.getDescriptor();
            this.d = d;
        }
    }

    private Module transform(TransformableModule targetTransformable, 
            TransformableModule sourceTransformable, Module source)
    {
        Module target = source.getParent().createModule(targetTransformable.getTarget());
        
        target.setTitle(source.getTitle());
        target.setScreenLocation(source.getScreenLocation());
        
        for (int i=targetTransformable.getParameterCount()-1;i>=0;i--)
        {
            TransformableParameter tpTarget = targetTransformable.getParameter(i);       
            TransformableParameter tpSource = sourceTransformable.getParameterById(tpTarget.getId());
            
            if (tpSource != null)
                transform(target, tpTarget.getTarget(), source, tpSource.getTarget());
        }
        
        return target;
    }

    private void transform(Module target, ParameterDescriptor pdTarget, 
            Module source, ParameterDescriptor pdSource)
    {
        Parameter pTarget = target.getParameter(pdTarget);
        Parameter pSource = source.getParameter(pdSource);
        
        pTarget.setNormalizedValue(pSource.getNormalizedValue());
    }

}
