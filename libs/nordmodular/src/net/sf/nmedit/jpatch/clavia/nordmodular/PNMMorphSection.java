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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.impl.PBasicModule;
import net.sf.nmedit.jpatch.impl.PBasicModuleContainer;
import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;

public class PNMMorphSection extends PBasicModuleContainer
{

    public static class Assignments implements Iterable<PParameter>
    {
        
        private List<PParameter> assignmentList;
        private int group;
        private NMPatch patch;
        
        public Assignments (NMPatch patch, int group)
        {
            assignmentList = new ArrayList<PParameter>();
            this.patch = patch;
            this.group = group;
        }
        
        public boolean isFull()
        {
            return assignmentList.size()>=25;
        }
        
        private boolean __add(PParameter parameter)
        {
            if (parameter == null)
                throw new NullPointerException("parameter must not be null");
            if (parameter.getExtensionParameter() == null)
                return false; // no morph support for parameter
            // max capacity/assignments = 25
            if (!isFull()) return assignmentList.add(parameter);
            return false;
        }

        public boolean add(PParameter parameter)
        {
            if (__add(parameter))
            {
            	parameter.setMorphGroup(group);
                fireAssigned(parameter);
                return true;
            }
            else
            {
                return false;
            }
        }

        private void fireDeassigned(PParameter parameter)
        {
            PAssignmentEvent e = new PAssignmentEvent();
            e.morphDeAssigned(group, parameter);
            patch.fireAssignmentEvent(e);
        }
        
        private void fireAssigned(PParameter parameter)
        {
            PAssignmentEvent e = new PAssignmentEvent();
            e.morphAssigned(group, parameter);
            patch.fireAssignmentEvent(e);
        }

        public boolean remove(PParameter parameter)
        {
            if (assignmentList.remove(parameter))
            {
            	parameter.setMorphGroup(-1);
                fireDeassigned(parameter);
                return true;
            }
            else
            {
                return false;
            }
        }
        
        public int size()
        {
            return assignmentList.size();
        }

        public Iterator<PParameter> iterator()
        {
            return new UnmodifiableIterator<PParameter>(assignmentList.iterator());
        }

        public void remove(PModule module)
        {
            if (assignmentList.size()<=0 || module.getParameterCount()<=0)
                return;
            
            for (int i=module.getParameterCount()-1;i>=0;i--)
            {
                PParameter p = module.getParameter(i);
                for (int j=assignmentList.size()-1;j>=0;j--)
                {
                    if (assignmentList.get(j)==p)
                    {
                        fireDeassigned(p);
                        assignmentList.remove(j);
                        break;
                    }
                }
            }
        }
    }
    
    private Assignments[] assignments = new Assignments[4];
    
    public PNMMorphSection(NMPatch patch)
    {
        super(patch, "MorphSection", Format.VALUE_SECTION_MORPH);
        super.add(1, new PBasicModule(patch.getModuleDescriptions().getModuleById("morph")));
        for (int i=0;i<assignments.length;i++)
            assignments[i] = new Assignments(patch, i);
    }

    // assignments
    
    public Assignments getAssignments(int morphIndex)
    {
        return assignments[morphIndex];
    }

    public int getAssignedMorph(PParameter parameter)
    {
        for (int i=0;i<assignments.length;i++)
        {
            if (!assignments[i].assignmentList.isEmpty())
            {
                for (PParameter p: assignments[i])
                {
                    if (p == parameter)
                        return i;
                }
            }
        }
        return -1;
    }

    public boolean assign(int morphIndex, PParameter parameter)
    {
        return getAssignments(morphIndex).add(parameter);
    }

    public boolean deassign(int morphIndex, PParameter parameter)
    {
        return getAssignments(morphIndex).remove(parameter);
    }
    
    // container

    public boolean isMorph(PParameter p)
    {
        PModule module = getMorphModule();
        for (int i=module.getParameterCount()-1;i>=0;i--)
            if (module.getParameter(i)==p)
                return true;
        return false;
    }
    
    protected PConnectionManager createConnectionManager()
    {
        return null;
    }

    public PModule getMorphModule()
    {
        return getModule(1);
    }

    public PParameter getMorph(int index)
    {
        return getMorphModule().getParameterByComponentId("morph"+(index+1));
    }

    public PParameter getKeyboardAssignment(int index)
    {
        return getMorphModule().getParameterByComponentId("mkb"+(index+1));
    }
    
    public boolean add(PModule module)
    {
        throw new UnsupportedOperationException("add("+PModule.class.getName()+")");
    }
    
    public boolean add(int index, PModule module)
    {
        throw new UnsupportedOperationException("add(int,"+PModule.class.getName()+")");
    }
    
    public boolean remove(PModule module)
    {
        throw new UnsupportedOperationException("remove("+PModule.class.getName()+")");
    }

    protected boolean canAdd(int index, PModule module)
    {
        return true;
    }
    
    protected void fireModuleAdded(PModule module)
    {
        // ignore - no events
    }
    
    protected void fireModuleRemoved(PModule module)
    {
        // ignore - no events
    }
    
    public void addModuleContainerListener(PModuleContainerListener l)
    {
        // ignore - no events
    }
    
    public void removeModuleContainerListener(PModuleContainerListener l)
    {
        // ignore - no events
    }

    public int getMorphCount()
    {
        return assignments.length;
    }
    
}
