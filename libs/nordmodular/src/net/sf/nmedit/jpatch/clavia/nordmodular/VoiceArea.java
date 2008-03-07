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

import net.sf.nmedit.jpatch.CopyOperation;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainerDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection.Assignments;
import net.sf.nmedit.jpatch.clavia.nordmodular.misc.Cycles;
import net.sf.nmedit.jpatch.impl.PBasicModuleContainer;

public class VoiceArea extends PBasicModuleContainer
{

    private static final String ATTRIBUTE_CYCLES = "cycles";
    
    private Cycles cycles = new Cycles();
    
    protected void registerModule(PModule module)
    {
        super.registerModule(module);
        registerCycles(module);
        
        if (module.getLightCount()>0)
            lightProcessor.registerModule(module);
    }

    protected void unregisterModule(PModule module)
    {
        super.unregisterModule(module);
        unregisterCycles(module);
        unregisterAssignments(module);
        
        if (module.getLightCount()>0)
            lightProcessor.unregisterModule(module);
    }

    private void unregisterAssignments(PModule module)
    {
        
        NMPatch patch = getPatch();
        // knobs
        KnobSet knobs = patch.getKnobs();
        
        for (int i=knobs.size()-1;i>=0;i--)
        {
            Knob k = knobs.get(i);
            PParameter p = k.getParameter();
            if (p != null)
            {
                PModule m = p.getParentComponent();
                if (m==module)
                {
                    k.setParameter(null);
                }   
            }
        }
        // morphs
        PNMMorphSection morph = patch.getMorphSection();
        for (int i=morph.getMorphCount()-1;i>=0;i--)
        {
            Assignments a = morph.getAssignments(i);
            a.remove(module);
        }
        
        // midi controller
        MidiControllerSet mcset = patch.getMidiControllers();
        mcset.remove(module);
    }

    public static final int UPDATE =-1;
    public static final int MOVE = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    
    private int impWidth;
    private int impHeight;

    private LightProcessor lightProcessor;

    private boolean polyVa;

    public VoiceArea(NMPatch patch, boolean polyVa, LightProcessor lp, String name, int componentIndex)
    {
        super(patch, name, componentIndex);
        this.polyVa = polyVa;
        this.lightProcessor = lp;
        //modules.setMinKey(1);
        impWidth = 0;
        impHeight = 0;
    }

    public VoiceArea(NMPatch patch, boolean polyVa, LightProcessor lp, PModuleContainerDescriptor descriptor, int componentIndex)
    {
        super(patch, descriptor, componentIndex);
        this.polyVa = polyVa;
        this.lightProcessor = lp;
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
        return polyVa;
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
    
    public String toString()
    {
        return ((isPolyVoiceArea()) ? "PolyVoiceArea":"CommonVoiceArea")
        +"[modules="+getModuleCount()+"]";
            
    }

    public MoveOperation createMoveOperation()
    {
        return new NMMoveOperation(this);
    }
    
    public CopyOperation createCopyOperation()
    {
    	return new NMCopyOperation(this);
    }

    public boolean canAdd(PModuleDescriptor descriptor)
    {
        return super.canAdd(descriptor) &&
        !(isPolyVoiceArea() && descriptor.getBooleanAttribute("cva-only", false));
    }
    
}
