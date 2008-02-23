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
package net.sf.nmedit.jsynth.worker;

public class PatchLocation
{
    
    private int bank = -1;
    private int position = -1;
    private int slot = -1;
    
    public PatchLocation(int bank, int position)
    {
        this.bank = bank;
        this.position = position;
    }
    
    public PatchLocation(int slot)
    {
        this.slot = slot;
    }
    
    public boolean inSlot()
    {
        return slot>=0;
    }
    
    public boolean inBank()
    {
        return bank>=0 && position>=0;
    }

    public int getBank()
    {
        return bank;
    }

    public int getPosition()
    {
        return position;
    }
    
    public int getSlot()
    {
        return slot;
    }
    
    public String toString()
    {
        return getClass().getName()+"[bank="+bank+",position="+position+",slot="+slot+"]";
    }
    
}
