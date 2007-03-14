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
 */package net.sf.nmedit.jsynth;

public class DefaultPatchInfo implements PatchInfo
{
    
    protected String patchName;
    private int slotIndex = -1;
    private int bankIndex = -1;
    private int bankPosition = -1;
    private boolean isEmpty = false;

    public DefaultPatchInfo()
    {
        super();
    }

    public DefaultPatchInfo(String patchName)
    {
        this.patchName = patchName;
    }
    
    public int getBankIndex()
    {
        return bankIndex;
    }

    public int getBankPosition()
    {
        return bankPosition;
    }

    public String getPatchName()
    {
        return patchName;
    }

    public void setPatchName(String name)
    {
        this.patchName = name;
    }
    
    public void setBankIndex(int index)
    {
        this.bankIndex = index;
    }
    
    public void setBankPosition(int pos)
    {
        this.bankPosition = pos;
    }
    
    public void setBankIndex(Bank bank)
    {
        if (bank == null)
            setBankIndex(-1);
        else
            setBankIndex(bank.getBankIndex());
    }

    public void setSlotIndex(Slot slot)
    {
        if (slot == null)
            setSlotIndex(-1);
        else
            setSlotIndex(slot.getSlotIndex());
    }
    
    public void setSlotIndex(int index)
    {
        this.slotIndex = index;
    }
    
    public int getSlotIndex()
    {
        return slotIndex;
    }
    
    public String toString()
    {
        return getClass().getSimpleName()+"[slot="+getSlotIndex()+",bank="+bankIndex+"/position="+bankPosition+"]";
    }

    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public void setIsEmpty(boolean empty)
    {
        this.isEmpty = empty;
    }

}
