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

/*
 * Created on Apr 7, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainerDescriptor;
import net.sf.nmedit.jpatch.clavia.nordmodular.misc.Cycles;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.impl.PBasicModuleContainer;

public class VoiceArea extends PBasicModuleContainer
{

    private static final String ATTRIBUTE_CYCLES = "cycles";
    
    private Cycles cycles = new Cycles();
    
    protected void fireModuleAdded(PModule module)
    {
        super.fireModuleAdded(module);
        registerCycles(module);
    }

    protected void fireModuleRemoved(PModule module)
    {
        super.fireModuleRemoved(module);
        unregisterCycles(module);
    }

    protected boolean canAdd(int index, PModule module)
    {
        return super.canAdd(index, module)
        && !(isPolyVoiceArea() && module.getBooleanAttribute("cva-only", false));
    }
    
    public static final int UPDATE =-1;
    public static final int MOVE = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    
    private int impWidth;
    private int impHeight;

    public VoiceArea(NMPatch patch, String name, int componentIndex)
    {
        super(patch, name, componentIndex);
        //modules.setMinKey(1);
        impWidth = 0;
        impHeight = 0;
    }

    public VoiceArea(NMPatch patch, PModuleContainerDescriptor descriptor, int componentIndex)
    {
        super(patch, descriptor, componentIndex);
        //modules.setMinKey(1);
        impWidth = 0;
        impHeight = 0;
    }

    private void registerCycles(double value)
    {
        cycles.add(value);
    }

    private void unregisterCycles(double value)
    {
        cycles.subtract(value);
    }

    public double getTotalCycles()
    {
        return cycles.getCycles();
    }
    
    private void registerCycles(PModule module)
    {
        registerCycles(((Double)module.getAttribute(ATTRIBUTE_CYCLES)).doubleValue());
    }
    
    private void unregisterCycles(PModule module)
    {
        unregisterCycles(((Double)module.getAttribute(ATTRIBUTE_CYCLES)).doubleValue());
    }
    
    public boolean isPolyVoiceArea()
    {
        return getPatch().getPolyVoiceArea()==this;
    }
    
    public NMPatch getPatch()
    {
        return (NMPatch) super.getPatch();
    }


    public int getImpliedWidth()
    {
        return impWidth;
    }

    public int getImpliedHeight()
    {
        return impHeight;
    }
    
    private EventListenerList eventListeners = null;
    private transient ModuleContainerEvent mce = null;
    
    private ModuleContainerEvent getModuleContainerEvent()
    {
        if (mce == null)
            mce = new ModuleContainerEvent(this);
        return mce;
    }

    public void addModuleContainerListener( ModuleContainerListener l )
    {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(ModuleContainerListener.class, l);
    }

    public void removeModuleContainerListener( ModuleContainerListener l )
    {
        if (eventListeners != null)
            eventListeners.remove(ModuleContainerListener.class, l);
    }

    public String toString()
    {
        return ((isPolyVoiceArea()) ? "PolyVoiceArea":"CommonVoiceArea")
        +"[modules="+getModuleCount()+"]";
            
    }

    public MoveOperation createMoveOperation()
    {
        return new NMMoveOperation(this);
    }

}
