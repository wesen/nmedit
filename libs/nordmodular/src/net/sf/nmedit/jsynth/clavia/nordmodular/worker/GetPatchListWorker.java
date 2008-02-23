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
package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jnmprotocol2.GetPatchListMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.PatchListEntry;
import net.sf.nmedit.jnmprotocol2.PatchListMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmBank;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;

public class GetPatchListWorker extends NmProtocolListener implements ScheduledWorker
{
    
    private NordModular synth;
    private NmBank bank;
    private int beginIndex;
    private int endIndex;
    private int nextIndex;
    private boolean error = false;
    private List<String> patches;
    private int state = REQUEST_PATCH_LIST;
    private static final int REQUEST_PATCH_LIST = 0;
    private static final int AWAIT_REPLY = 1;
    private static final int COMPLETE = 2;
    private long timeout = 0;
    private boolean replyAccepted = false;
    private boolean installed = false;

    public GetPatchListWorker(NmBank bank, int beginIndex, int endIndex)
    {
        if (endIndex<beginIndex || beginIndex<0 || endIndex>NmBank.PATCH_COUNT)
            throw new IllegalArgumentException("invalid beginIndex:"+beginIndex+", endIndex:"+endIndex);

        this.synth = bank.getSynthesizer();
        this.bank = bank;
        this.beginIndex = beginIndex;
        this.nextIndex = beginIndex;
        this.endIndex = endIndex;
        this.patches = new ArrayList<String>(endIndex-beginIndex);
    }
    
    private void install()
    {
        if (!installed)
        {
            installed = true;
            bank.getSynthesizer().addProtocolListener(this);
        }
    }
    
    private void uninstall()
    {
        if (installed)
        {
            installed = false;
            bank.getSynthesizer().removeProtocolListener(this);
        }
    }

    public void aborted()
    {
        error = true;
        uninstall();
    }

    public void sendRequest()
    {
        install();
        bank.getSynthesizer().getScheduler().offer(this);
    }

    public boolean isWorkerFinished()
    {
        // TODO Auto-generated method stub
        return error || (!synth.isConnected()) || requestComplete();
    }

    public void runWorker() throws SynthException
    {
        if (state == COMPLETE)
            return;
        
        if (state == REQUEST_PATCH_LIST)
        {
            timeout = System.currentTimeMillis()+3000;
            state = AWAIT_REPLY;
            replyAccepted = false;
            GetPatchListMessage get = new GetPatchListMessage(bank.getBankIndex(), nextIndex);
            try
            {
                synth.getProtocol().send(get);
            }
            catch (Exception e)
            {
                error = true;
                e.printStackTrace();
            }
            return;
        }

        // await reply
        if (!replyAccepted)
        {
            if (System.currentTimeMillis()>timeout)
                throw new SynthException("timeout while waiting for patch list: "+3000);
            return;
        }
        
        // reply accepted
        if (!requestComplete())
        {
            state = REQUEST_PATCH_LIST;
            return;
        }
        
        // request complete
        state = COMPLETE;
        
        SwingUtilities.invokeLater(new Runnable(){
         public void run(){
            uninstall();
            // should all work through callbacks from messages
            updateBank();
        }
        }   
        );
    }
    
    private int updateBeginIndex = -1;
    private void updateBank()
    {
        if (updateBeginIndex<0)
            updateBeginIndex = beginIndex;
     
        int updateEndIndex = beginIndex+patches.size();
        
        if (updateBeginIndex<updateEndIndex)
        {
            Collection<String> c = patches.subList(updateBeginIndex-beginIndex, updateEndIndex-beginIndex);
            
            if (updateBeginIndex+c.size()<=bank.getPatchCount()) // avoid IllegalArgumentException
                bank.updatePatchList(updateBeginIndex, c);
            //else
                // TODO debug this case: happens if storing patch over existing bank position
        }
        updateBeginIndex = updateEndIndex;
    }
    
    private boolean requestComplete()
    {
        return nextIndex>=endIndex;
    }
    
    private int previousNextIndex = Integer.MIN_VALUE;

    public void messageReceived(PatchListMessage message) 
    {
        if (state != AWAIT_REPLY)
            return;
        
        replyAccepted = true;
        List<PatchListEntry> list = message.getEntries();
        
        if (previousNextIndex >= nextIndex || list.isEmpty())
        {            
            fillList(endIndex);
            nextIndex = endIndex;
            return;
        }

        previousNextIndex = nextIndex;
        
        for (PatchListEntry e: list)
        {
            if (e.getPosition()<0 || e.getSection()<0 || (e.getSection()>bank.getBankIndex()))
            {
                // positions containing no patches
                fillList(endIndex);
                nextIndex = endIndex;
                return;
                // complete
            }
            // positions containing no patches
            fillList(e.getPosition());
            String name = e.getName();
            if (name == null) name = "";
            patches.add(name);
            nextIndex++;
        }

        updateBank();
    }
    
    private void fillList(int end)
    {
        while (nextIndex<end)
        {
            patches.add(null);
            nextIndex++;
        }
    }
    
}
