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
 * Created on Jan 16, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import java.util.Collection;
import java.util.Iterator;
import net.sf.nmedit.jsynth.AbstractBank;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.GetPatchListWorker;

public class NmBank extends AbstractBank<NordModular>
{
    
    public static final int PATCH_COUNT = 99;
    
    private PatchInfo[] patchList = new PatchInfo[PATCH_COUNT];

    public NmBank(NordModular synth, int bankIndex)
    {
        super(synth, bankIndex);
        for (int i=0;i<patchList.length;i++)
            patchList[i] = new PatchInfo();
    }

    public String getPatchLocationName(int position)
    {
        if ( getSynthesizer().isMicroModular() )
        {
            if (position<9)
                return "0"+(1+position);
            else
                return Integer.toString(1+position);
        }
        return Integer.toString(getSection()*100+1+position);
    }

    public String getName()
    {    
        int section = getSection();
        if ( getSynthesizer().isMicroModular() )
            section --;
        section = section * 100;
        return Integer.toString(section+1)+"-"+Integer.toString(section+99);
    }

    public int getPatchCount()
    {
        return PATCH_COUNT;
    }

    public int getSection()
    {
        return getBankIndex()+1;
    }

    public boolean containsPatch(int index)
    {
        return patchList[index].containsPatch();
    }

    public String getPatchName(int index)
    {
        return patchList[index].name;
    }

    public boolean isPatchInfoAvailable(int index)
    {
        return patchList[index].valid;
    }
    
    private static class PatchInfo
    {
        String name = null;
        boolean valid = false;
        boolean containsPatch()
        {
            return valid && name != null;
        }
    }

    public void update(int beginIndex, int endIndex)
    {
        GetPatchListWorker w = new GetPatchListWorker(this, beginIndex, endIndex);
        
        NordModular synth = getSynthesizer();
        if (!synth.isConnected())
            throw new RuntimeException("not connected");
        
        w.sendRequest();
    }

    public void updatePatchList(int beginIndex, Collection<String> patches)
    {
        if (beginIndex<0 || beginIndex>=PATCH_COUNT)
            throw new IllegalArgumentException("invalid index: "+beginIndex);
        int endIndex = Math.min(beginIndex+patches.size(), PATCH_COUNT);
        
        Iterator<String> iter = patches.iterator();
        for (int i=beginIndex;i<endIndex;i++)
        {
            String p = iter.next();
            PatchInfo info = patchList[i];
            info.valid = true;
            info.name = p;
        }
        
        fireBankUpdateEvent(beginIndex, endIndex);
    }

}
