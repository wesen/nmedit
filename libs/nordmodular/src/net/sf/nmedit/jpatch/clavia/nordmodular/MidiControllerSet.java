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
 * Created on Apr 19, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;

public class MidiControllerSet extends AbstractList<MidiController>
{
    
    MidiController[] midiControllerList;
    
    private NMPatch patch;

    public NMPatch getPatch()
    {
        return patch;
    }
    
    public MidiControllerSet(NMPatch patch)
    {        
        this.patch = patch;
        midiControllerList = new MidiController[120];
        for (int i=0;i<32;i++)
            midiControllerList[i] = new MidiController(this, i);
        for (int i=33;i<=120;i++)
            midiControllerList[i-1] = new MidiController(this, i);
    }
    

    public static boolean isValidMC(int mc)
    {
        return 0 <= mc && mc<= 120 && mc!=32;
    }

    public int getMidiControllerIndex(PParameter p)
    {
        for (int i=midiControllerList.length-1;i>=0;i--)
            if (midiControllerList[i].getParameter() == p)
                return i;
        return -1;
    }
    
    public boolean deassign(PParameter p)
    {
        int index = getMidiControllerIndex(p);
        if (index>=0)
        {
            midiControllerList[index].setParameter(null);
            return true;
        }
        return false;
    }
    
    public int getIndexForMC(int mc)
    {
        if (0<=mc)
        {
            if (mc<32)
            {
                return mc;
            }
            else if (mc==32)
            {
                return -1;
            }
            else if (mc<=120)
            {
                return mc-1;
            }
        }
        return -1;
    }
    
    public MidiController[] getPrimaryMidicontrollers()
    {
        return new MidiController[]{
                getByMC(1),
                getByMC(11),
                getByMC(7)};
    }
    
    public MidiController getByMC(int mc)
    {
        try
        {
        return midiControllerList[getIndexForMC(mc)];
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Invalid midi controller:"+mc);
        }
    }
    
    public int indexOf(Object obj)
    {
        MidiController mc = (MidiController) obj;
        int index = getIndexForMC(mc.getControlId());
        try 
        {
            if (midiControllerList[index]==mc)
            {
                return index;
            }
        } 
        catch (IndexOutOfBoundsException e)
        { }

        return -1;
    }
    
    @Override
    public MidiController get(int index)
    {
        return midiControllerList[index];
    }
    
    @Override
    public int size()
    {
        return midiControllerList.length;
    }
    
    public MidiController[] getAssignedControllers()
    {
        List<MidiController> list = new ArrayList<MidiController>(midiControllerList.length);
        
        for (int i=0;i<midiControllerList.length;i++)
        {
            MidiController mc = midiControllerList[i];
            if (mc != null && mc.getParameter()!=null)
                list.add(mc);
        }
        
        return list.toArray(new MidiController[list.size()]);
    }
    
    public void remove(PModule module)
    {
        for (int i=midiControllerList.length-1;i>=0;i--)
        {
            MidiController mc = midiControllerList[i];
            PParameter p;
            if (mc != null && (p=mc.getParameter())!=null && p.getParentComponent()==module)
            {
                mc.setParameter(null);
            }
        }
    }

}
