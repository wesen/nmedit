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

public abstract class AbstractBank<S extends Synthesizer> implements Bank<S>
{

    private int bankIndex;
    
    private S synth;

    protected AbstractBank(S synth, int bankIndex)
    {
        this.synth = synth;
        this.bankIndex = bankIndex;
    }

    public String getName()
    {
        return "Bank "+(bankIndex+1);
    }

    public S getSynthesizer()
    {
        return synth;
    }

    public int getBankIndex()
    {
        return bankIndex;
    }

    
/*
    private int bankIndex;
    
    protected DefaultPatchInfo[] patchInfoList;

    private S synth;

    protected AbstractBank(S synth, int bankIndex, int capacity)
    {
        this.synth = synth;
        this.bankIndex = bankIndex;
        createInfoList(capacity);
    }
    
    public int getPatchCount()
    {
        return patchInfoList.length;
    }

    protected void createInfoList(int capacity)
    {
        patchInfoList = new DefaultPatchInfo[capacity];
        for (int i=0;i<patchInfoList.length;i++)
        {
            DefaultPatchInfo info = new DefaultPatchInfo();
            info.setBankIndex(bankIndex);
            info.setBankPosition(i);
            patchInfoList[i] = info;
        }
    }

    public DefaultPatchInfo getPatchInfo(int index)
    {
        return patchInfoList[index];
    }

    protected void checkIndex(int index)
    {
        if (index<0 || index>=getPatchCount())
            throw new IndexOutOfBoundsException("invalid index: "+index);
    }

    protected void checkRange(int start, int end)
    {
        checkIndex(start);

        int lastIndex = start+end-1;
        if (end<start || lastIndex<0 || lastIndex>=getPatchCount())
            throw new IllegalArgumentException("invalid range ["+start+".."+end+"]");
    }
    
    public boolean isPatchInfoAvailable(int index)
    {
        if (index<0 || index>=getPatchCount())
            throw new IndexOutOfBoundsException("invalid index: "+index);
        
        return true;
    }

    public boolean isPatchInfoAvailable(int start, int end)
    {
        checkRange(start, end);
        
        for (int i=start;i<end;i++)
            if (!isPatchInfoAvailable(i))
                return false;
        
        return true;
    }

    public void requestPatchInfo(int start, int end) throws SynthException
    {
        checkRange(start, end);
    }
*/
}
